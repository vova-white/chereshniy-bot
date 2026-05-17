package com.chereshniy.app

import io.ktor.server.application.*
import io.ktor.server.sse.SSE

fun Application.configureSse() {
    install(SSE)
}
