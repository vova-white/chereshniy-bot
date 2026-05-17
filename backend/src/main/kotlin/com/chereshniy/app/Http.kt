package com.chereshniy.app

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.forwardedheaders.ForwardedHeaders
import io.ktor.server.plugins.forwardedheaders.XForwardedHeaders
import io.ktor.server.plugins.httpsredirect.HttpsRedirect

fun Application.configureHttp() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("X-CSRF-Token")
        anyHost()
    }
    install(Compression)
    install(DefaultHeaders) {
        header("X-Engine", "Ktor")
    }
    install(ForwardedHeaders)
    install(XForwardedHeaders)
    install(HttpsRedirect) {
        sslPort = 443
        permanentRedirect = true
    }
}

fun Application.configureAutoHeadResponse() {
    install(AutoHeadResponse)
}
