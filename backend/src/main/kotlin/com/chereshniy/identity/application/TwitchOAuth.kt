package com.chereshniy.identity.application

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.ApplicationConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface TwitchOAuthGateway {
    fun authorizationUrl(
        settings: TwitchOAuthSettings,
        state: String,
    ): String

    suspend fun exchangeCodeForStreamer(
        settings: TwitchOAuthSettings,
        code: String,
    ): TwitchSignInResult
}

data class TwitchOAuthSettings(
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
    val scopes: List<String>,
    val authorizationHost: String = "id.twitch.tv",
    val tokenUrl: String = "https://id.twitch.tv/oauth2/token",
    val usersUrl: String = "https://api.twitch.tv/helix/users",
) {
    fun authorizationUrl(state: String): String = URLBuilder(
        protocol = URLProtocol.HTTPS,
        host = authorizationHost,
    ).apply {
        appendPathSegments("oauth2", "authorize")
        parameters.append("client_id", clientId)
        parameters.append("redirect_uri", redirectUri)
        parameters.append("response_type", "code")
        parameters.append("scope", scopes.joinToString(" "))
        parameters.append("state", state)
    }.buildString()

    companion object {
        fun fromEnvironment(config: ApplicationConfig): TwitchOAuthSettings = TwitchOAuthSettings(
            clientId = config.propertyOrNull("twitch.oauth.clientId")?.getString()
                ?: System.getenv("TWITCH_CLIENT_ID").orEmpty(),
            clientSecret = config.propertyOrNull("twitch.oauth.clientSecret")?.getString()
                ?: System.getenv("TWITCH_CLIENT_SECRET").orEmpty(),
            redirectUri = config.propertyOrNull("twitch.oauth.redirectUri")?.getString()
                ?: System.getenv("TWITCH_REDIRECT_URI").orEmpty().ifBlank {
                    "http://localhost:8080/api/auth/twitch/callback"
                },
            scopes = listOf("channel:manage:redemptions"),
        )
    }
}

data class TwitchSignInResult(
    val twitchUserId: String,
    val twitchLogin: String,
    val displayName: String,
    val accessToken: String,
    val refreshToken: String,
)

class DefaultTwitchOAuthGateway(
    private val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    },
) : TwitchOAuthGateway {
    override fun authorizationUrl(
        settings: TwitchOAuthSettings,
        state: String,
    ): String = settings.authorizationUrl(state)

    override suspend fun exchangeCodeForStreamer(
        settings: TwitchOAuthSettings,
        code: String,
    ): TwitchSignInResult {
        require(settings.clientId.isNotBlank()) { "TWITCH_CLIENT_ID is required for Twitch OAuth." }
        require(settings.clientSecret.isNotBlank()) { "TWITCH_CLIENT_SECRET is required for Twitch OAuth." }

        val tokenResponse = httpClient.post(settings.tokenUrl) {
            setBody(
                FormDataContent(
                    Parameters.build {
                        append("client_id", settings.clientId)
                        append("client_secret", settings.clientSecret)
                        append("code", code)
                        append("grant_type", "authorization_code")
                        append("redirect_uri", settings.redirectUri)
                    },
                ),
            )
        }.body<TwitchTokenResponse>()

        val usersResponse = httpClient.get(settings.usersUrl) {
            header("Client-Id", settings.clientId)
            header(HttpHeaders.Authorization, "Bearer ${tokenResponse.accessToken}")
        }.body<TwitchUsersResponse>()
        val user = usersResponse.data.firstOrNull()
            ?: error("Twitch did not return an identity for the authorized Streamer.")

        return TwitchSignInResult(
            twitchUserId = user.id,
            twitchLogin = user.login,
            displayName = user.displayName,
            accessToken = tokenResponse.accessToken,
            refreshToken = tokenResponse.refreshToken,
        )
    }
}

@Serializable
private data class TwitchTokenResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
)

@Serializable
private data class TwitchUsersResponse(
    val data: List<TwitchUserResponse>,
)

@Serializable
private data class TwitchUserResponse(
    val id: String,
    val login: String,
    @SerialName("display_name")
    val displayName: String,
)
