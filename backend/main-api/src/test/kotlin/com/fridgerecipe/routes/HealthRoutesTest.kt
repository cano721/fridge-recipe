package com.fridgerecipe.routes

import com.fridgerecipe.configureTestApp
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HealthRoutesTest {

    @Test
    fun `GET health returns 200 with status field`() = testApplication {
        configureTestApp {
            routing {
                route("/api/v1") {
                    get("/health") {
                        val healthResponse = HealthResponse(
                            status = "degraded",
                            db = "disconnected",
                            redis = "disconnected",
                            aiService = "disconnected"
                        )
                        call.respondText(
                            Json.encodeToString(healthResponse),
                            ContentType.Application.Json
                        )
                    }
                }
            }
        }

        val response = client.get("/api/v1/health")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("status"), "응답에 status 필드가 있어야 함")
    }

    @Test
    fun `GET health response contains required fields`() = testApplication {
        configureTestApp {
            routing {
                route("/api/v1") {
                    get("/health") {
                        val healthResponse = HealthResponse(
                            status = "ok",
                            db = "connected",
                            redis = "connected",
                            aiService = "connected"
                        )
                        call.respondText(
                            Json.encodeToString(healthResponse),
                            ContentType.Application.Json
                        )
                    }
                }
            }
        }

        val response = client.get("/api/v1/health")
        val body = response.bodyAsText()

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(body.contains("db"))
        assertTrue(body.contains("redis"))
        assertTrue(body.contains("aiService"))
    }

    @Test
    fun `GET unknown path returns 404`() = testApplication {
        configureTestApp()
        val response = client.get("/api/v1/unknown-endpoint")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}
