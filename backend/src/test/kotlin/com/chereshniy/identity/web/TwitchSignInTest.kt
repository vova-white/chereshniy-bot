package com.chereshniy.identity.web

import com.chereshniy.app.configureApplication
import com.chereshniy.identity.application.IdentityModule
import com.chereshniy.identity.application.TwitchOAuthGateway
import com.chereshniy.identity.application.TwitchOAuthSettings
import com.chereshniy.identity.application.TwitchSignInResult
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TwitchSignInTest {
    @Test
    fun `twitch sign in creates streamer auction and application session without exposing tokens`() = testApplication {
        val twitchGateway = FakeTwitchOAuthGateway()
        val identityModule = IdentityModule.inMemory(
            twitchOAuthGateway = twitchGateway,
            oauthSettings = testOauthSettings,
        )

        application {
            configureApplication(identityModule = identityModule)
        }

        val client = createClient {
            followRedirects = false
            install(HttpCookies)
        }

        val startResponse = client.get("https://localhost/api/auth/twitch/start")
        assertEquals(HttpStatusCode.Found, startResponse.status)

        val twitchAuthorizeUrl = Url(assertNotNull(startResponse.headers[HttpHeaders.Location]))
        assertEquals("id.twitch.tv", twitchAuthorizeUrl.host)
        assertEquals("test-client-id", twitchAuthorizeUrl.parameters["client_id"])
        assertEquals("code", twitchAuthorizeUrl.parameters["response_type"])
        assertEquals("http://localhost:8080/api/auth/twitch/callback", twitchAuthorizeUrl.parameters["redirect_uri"])
        assertEquals("channel:manage:redemptions", twitchAuthorizeUrl.parameters["scope"])
        assertTrue(twitchAuthorizeUrl.parameters["state"].orEmpty().isNotBlank())

        val callbackResponse = client.get(
            "https://localhost/api/auth/twitch/callback?code=oauth-code&state=${twitchAuthorizeUrl.parameters["state"]}",
        )
        assertEquals(HttpStatusCode.Found, callbackResponse.status)
        assertEquals("/", callbackResponse.headers[HttpHeaders.Location])
        assertContains(callbackResponse.headers.getAll(HttpHeaders.SetCookie).orEmpty().joinToString("\n"), "APPLICATION_SESSION=")
        assertContains(callbackResponse.headers.getAll(HttpHeaders.SetCookie).orEmpty().joinToString("\n"), "HttpOnly")
        assertContains(callbackResponse.headers.getAll(HttpHeaders.SetCookie).orEmpty().joinToString("\n"), "SameSite=lax")

        val sessionResponse = client.get("https://localhost/api/admin/session")
        assertEquals(HttpStatusCode.OK, sessionResponse.status)
        val sessionJson = sessionResponse.bodyAsText()
        assertContains(sessionJson, """"displayName":"Cherry Streamer"""")
        assertContains(sessionJson, """"publicAuctionSlug":"cherry_streamer"""")
        assertContains(sessionJson, """"auctionTitle":"Point Auction"""")
        assertContains(sessionJson, """"statusLabel":"Closed Bidding Status"""")
        assertContains(sessionJson, """"lotCount":0""")
        assertContains(sessionJson, """"pendingBidCount":0""")
        assertTrue("access-token" !in sessionJson)
        assertTrue("refresh-token" !in sessionJson)
    }

    @Test
    fun `log out clears the application session without revoking twitch authorization`() = testApplication {
        val twitchGateway = FakeTwitchOAuthGateway()
        val identityModule = IdentityModule.inMemory(
            twitchOAuthGateway = twitchGateway,
            oauthSettings = testOauthSettings,
        )

        application {
            configureApplication(identityModule = identityModule)
        }

        val client = createClient {
            followRedirects = false
            install(HttpCookies)
        }

        val startResponse = client.get("https://localhost/api/auth/twitch/start")
        val state = Url(assertNotNull(startResponse.headers[HttpHeaders.Location])).parameters["state"]
        client.get("https://localhost/api/auth/twitch/callback?code=oauth-code&state=$state")

        val logoutResponse = client.post("https://localhost/api/admin/logout") {
            headers.append("X-CSRF-Token", "test")
            headers.append(HttpHeaders.Origin, "http://localhost:8080")
            headers.append(HttpHeaders.Host, "localhost:8080")
        }
        assertEquals(HttpStatusCode.NoContent, logoutResponse.status, logoutResponse.bodyAsText())
        assertTrue(twitchGateway.revokedCodes.isEmpty())

        val sessionResponse = client.get("https://localhost/api/admin/session")
        assertEquals(HttpStatusCode.Unauthorized, sessionResponse.status)
    }

    @Test
    fun `successful repeated twitch sign in refreshes the public auction slug`() = testApplication {
        val twitchGateway = FakeTwitchOAuthGateway()
        val identityModule = IdentityModule.inMemory(
            twitchOAuthGateway = twitchGateway,
            oauthSettings = testOauthSettings,
        )

        application {
            configureApplication(identityModule = identityModule)
        }

        val client = createClient {
            followRedirects = false
            install(HttpCookies)
        }

        signIn(client)
        assertContains(client.get("https://localhost/api/admin/session").bodyAsText(), """"publicAuctionSlug":"cherry_streamer"""")

        twitchGateway.twitchLogin = "renamed_streamer"
        signIn(client)

        val refreshedSessionJson = client.get("https://localhost/api/admin/session").bodyAsText()
        assertContains(refreshedSessionJson, """"publicAuctionSlug":"renamed_streamer"""")
    }
}

private val testOauthSettings = TwitchOAuthSettings(
    clientId = "test-client-id",
    clientSecret = "test-client-secret",
    redirectUri = "http://localhost:8080/api/auth/twitch/callback",
    scopes = listOf("channel:manage:redemptions"),
)

private class FakeTwitchOAuthGateway : TwitchOAuthGateway {
    val revokedCodes = mutableListOf<String>()
    var twitchLogin = "cherry_streamer"

    override fun authorizationUrl(
        settings: TwitchOAuthSettings,
        state: String,
    ): String = settings.authorizationUrl(state)

    override suspend fun exchangeCodeForStreamer(
        settings: TwitchOAuthSettings,
        code: String,
    ): TwitchSignInResult {
        assertEquals("oauth-code", code)
        return TwitchSignInResult(
            twitchUserId = "12345",
            twitchLogin = twitchLogin,
            displayName = "Cherry Streamer",
            accessToken = "access-token",
            refreshToken = "refresh-token",
        )
    }
}

private suspend fun signIn(client: io.ktor.client.HttpClient) {
    val startResponse = client.get("https://localhost/api/auth/twitch/start")
    val state = Url(assertNotNull(startResponse.headers[HttpHeaders.Location])).parameters["state"]
    client.get("https://localhost/api/auth/twitch/callback?code=oauth-code&state=$state")
}
