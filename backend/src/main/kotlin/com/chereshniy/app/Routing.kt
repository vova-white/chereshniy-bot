package com.chereshniy.app

import com.chereshniy.identity.application.IdentityModule
import com.chereshniy.identity.web.identityRoutes
import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting(identityModule: IdentityModule) {
    routing {
        get("/") {
            call.respondText("Chereshniy Bot")
        }
        identityRoutes(identityModule)
    }
}
