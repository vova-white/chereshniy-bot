package com.chereshniy.app

import io.ktor.http.HttpHeaders
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.CallId

fun Application.configureMonitoring() {
    install(CallId) {
        header(HttpHeaders.XRequestId)
        verify { callId -> callId.isNotBlank() }
    }
}
