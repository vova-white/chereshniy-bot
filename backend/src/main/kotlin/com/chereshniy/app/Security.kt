package com.chereshniy.app

import com.chereshniy.identity.application.ApplicationSession
import com.chereshniy.identity.application.TwitchOAuthState
import io.ktor.server.application.*
import io.ktor.server.plugins.csrf.CSRF
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie

fun Application.configureSecurity() {
    val secureSessionCookies = environment.config.propertyOrNull("application.session.secureCookies")
        ?.getString()
        ?.toBooleanStrictOrNull()
        ?: (environment.config.propertyOrNull("http.forceHttps")?.getString()?.toBooleanStrictOrNull() == true)

    install(CSRF) {
        allowOrigin("http://localhost:8080")
        allowOrigin("http://localhost:5173")
        allowOrigin("http://127.0.0.1:5173")
        originMatchesHost()
        checkHeader("X-CSRF-Token")
    }
    install(Sessions) {
        cookie<ApplicationSession>("APPLICATION_SESSION") {
            cookie.httpOnly = true
            cookie.secure = secureSessionCookies
            cookie.extensions["SameSite"] = "lax"
        }
        cookie<TwitchOAuthState>("TWITCH_OAUTH_STATE") {
            cookie.httpOnly = true
            cookie.secure = secureSessionCookies
            cookie.extensions["SameSite"] = "lax"
        }
    }
}
