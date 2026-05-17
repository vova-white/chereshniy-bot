package com.chereshniy.app

import com.chereshniy.identity.application.ApplicationSession
import io.ktor.server.application.*
import io.ktor.server.plugins.csrf.CSRF
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie

fun Application.configureSecurity() {
    install(CSRF) {
        allowOrigin("http://localhost:8080")
        originMatchesHost()
        checkHeader("X-CSRF-Token")
    }
    install(Sessions) {
        cookie<ApplicationSession>("APPLICATION_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
}
