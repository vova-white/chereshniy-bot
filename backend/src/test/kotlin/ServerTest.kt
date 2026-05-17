package com.chereshniy.app

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.*

class ServerTest {

    @Test
    fun `application composition root exposes service status`() = testApplication {
        application {
            configureApplication()
        }

        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Chereshniy Bot", response.bodyAsText())
    }

    @Test
    fun `generated demo endpoints are not registered`() = testApplication {
        application {
            configureApplication()
        }

        assertEquals(HttpStatusCode.NotFound, client.get("/json/kotlinx-serialization").status)
        assertEquals(HttpStatusCode.NotFound, client.get("/users/1").status)
        assertEquals(HttpStatusCode.NotFound, client.get("/session/increment").status)
    }
}
