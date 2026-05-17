package com.chereshniy.identity.web

import com.chereshniy.identity.application.ApplicationSession
import com.chereshniy.identity.application.IdentityModule
import com.chereshniy.identity.application.TwitchOAuthState
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import java.security.SecureRandom
import java.util.Base64

fun Route.identityRoutes(identityModule: IdentityModule) {
    get("/api/auth/twitch/start") {
        val state = newOauthState()
        call.sessions.set(TwitchOAuthState(state))
        call.respondRedirect(identityModule.twitchAuthorizationUrl(state))
    }

    get("/api/auth/twitch/callback") {
        val code = call.request.queryParameters["code"]
        val state = call.request.queryParameters["state"]
        val expectedState = call.sessions.get<TwitchOAuthState>()?.value

        if (code.isNullOrBlank() || state.isNullOrBlank() || state != expectedState) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(ErrorBody("invalid_twitch_oauth_callback", "Invalid Twitch sign-in callback.")),
            )
            return@get
        }

        val applicationSession = identityModule.signInWithTwitch(code)
        call.sessions.clear<TwitchOAuthState>()
        call.sessions.set(applicationSession)
        call.respondRedirect("/")
    }

    get("/api/admin/session") {
        val applicationSession = call.sessions.get<ApplicationSession>()
        if (applicationSession == null) {
            call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse(ErrorBody("not_signed_in", "Sign in with Twitch to continue.")),
            )
            return@get
        }

        val sessionView = identityModule.currentSession(applicationSession)
        if (sessionView == null) {
            call.sessions.clear<ApplicationSession>()
            call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse(ErrorBody("not_signed_in", "Sign in with Twitch to continue.")),
            )
            return@get
        }

        call.respond(sessionView)
    }

    post("/api/admin/logout") {
        call.sessions.clear<ApplicationSession>()
        call.respond(HttpStatusCode.NoContent)
    }
}

private fun newOauthState(): String {
    val bytes = ByteArray(32)
    secureRandom.nextBytes(bytes)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
}

private val secureRandom = SecureRandom()

@kotlinx.serialization.Serializable
private data class ErrorResponse(val error: ErrorBody)

@kotlinx.serialization.Serializable
private data class ErrorBody(
    val code: String,
    val message: String,
)
