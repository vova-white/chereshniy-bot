package com.chereshniy.app

import com.chereshniy.identity.application.IdentityModule
import io.ktor.server.application.Application

fun Application.configureApplication(
    identityModule: IdentityModule = IdentityModule.fromEnvironment(environment.config),
) {
    configureHttp()
    configureMonitoring()
    configureSerialization()
    configureSecurity()
    configureAutoHeadResponse()
    configureRequestValidation()
    configureSse()
    configureStatusPages()
    configurePersistence()
    configureRateLimiting()
    configureJobs()
    configureRouting(identityModule)
}

fun Application.configureJobs() {
}
