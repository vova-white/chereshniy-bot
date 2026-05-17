package com.chereshniy.app

import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.RequestValidation

fun Application.configureRequestValidation() {
    install(RequestValidation)
}
