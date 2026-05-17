package com.chereshniy.app

import io.ktor.server.application.Application

fun Application.configureApplication() {
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
    configureRouting()
}

fun Application.configureJobs() {
}
