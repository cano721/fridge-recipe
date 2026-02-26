package com.fridgerecipe.routes

import com.fridgerecipe.configureTestAppWithAuth
import com.fridgerecipe.createJsonClient
import com.fridgerecipe.domain.service.AuthService
import com.fridgerecipe.domain.service.TokenPair
import com.fridgerecipe.generateTestToken
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthRoutesTest {

    private val mockAuthService = mockk<AuthService>()

    private val fakeTokenPair = TokenPair(
        accessToken = "fake-access-token",
        refreshToken = "fake-refresh-token",
        expiresIn = 1800L
    )

    @Test
    fun `POST auth login returns 200 with token pair`() = testApplication {
        coEvery { mockAuthService.login(any(), any(), any()) } returns fakeTokenPair

        configureTestAppWithAuth {
            routing {
                route("/api/v1/auth") {
                    post("/login") {
                        val bodyText = call.receive<String>()
                        val json = Json.parseToJsonElement(bodyText).jsonObject
                        val provider = json["provider"]?.jsonPrimitive?.content ?: ""
                        val accessToken = json["accessToken"]?.jsonPrimitive?.content ?: ""
                        val deviceInfo = json["deviceInfo"]?.jsonPrimitive?.content
                        val tokenPair = mockAuthService.login(provider, accessToken, deviceInfo)
                        call.respondText(
                            """{"success":true,"data":{"accessToken":"${tokenPair.accessToken}","refreshToken":"${tokenPair.refreshToken}","expiresIn":${tokenPair.expiresIn}}}""",
                            ContentType.Application.Json,
                            HttpStatusCode.OK
                        )
                    }
                }
            }
        }

        val client = createJsonClient()
        val response = client.post("/api/v1/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"provider":"kakao","accessToken":"oauth-token-123"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("accessToken"))
        assertTrue(body.contains("refreshToken"))
        assertTrue(body.contains("expiresIn"))
    }

    @Test
    fun `POST auth login with invalid json returns error`() = testApplication {
        configureTestAppWithAuth {
            routing {
                route("/api/v1/auth") {
                    post("/login") {
                        val bodyText = call.receive<String>()
                        try {
                            Json.parseToJsonElement(bodyText).jsonObject
                            call.respondText("{}", ContentType.Application.Json)
                        } catch (e: Exception) {
                            call.respondText(
                                """{"success":false,"error":{"code":"VALIDATION_FAILED","message":"잘못된 요청"}}""",
                                ContentType.Application.Json,
                                HttpStatusCode.BadRequest
                            )
                        }
                    }
                }
            }
        }

        val client = createJsonClient()
        val response = client.post("/api/v1/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("not-valid-json")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `POST auth refresh with valid token returns new token pair`() = testApplication {
        val newTokenPair = fakeTokenPair.copy(
            accessToken = "new-access-token",
            refreshToken = "new-refresh-token"
        )
        coEvery { mockAuthService.refresh(any(), any()) } returns newTokenPair

        configureTestAppWithAuth {
            routing {
                route("/api/v1/auth") {
                    post("/refresh") {
                        val bodyText = call.receive<String>()
                        val json = Json.parseToJsonElement(bodyText).jsonObject
                        val refreshToken = json["refreshToken"]?.jsonPrimitive?.content ?: ""
                        val deviceInfo = json["deviceInfo"]?.jsonPrimitive?.content
                        val tokenPair = mockAuthService.refresh(refreshToken, deviceInfo)
                        call.respondText(
                            """{"success":true,"data":{"accessToken":"${tokenPair.accessToken}","refreshToken":"${tokenPair.refreshToken}","expiresIn":${tokenPair.expiresIn}}}""",
                            ContentType.Application.Json,
                            HttpStatusCode.OK
                        )
                    }
                }
            }
        }

        val client = createJsonClient()
        val response = client.post("/api/v1/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody("""{"refreshToken":"old-refresh-token"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("new-access-token"))
    }

    @Test
    fun `POST auth refresh with revoked token returns 400`() = testApplication {
        coEvery { mockAuthService.refresh(any(), any()) } throws IllegalArgumentException("AUTH_REFRESH_REVOKED")

        configureTestAppWithAuth {
            routing {
                route("/api/v1/auth") {
                    post("/refresh") {
                        val bodyText = call.receive<String>()
                        val json = Json.parseToJsonElement(bodyText).jsonObject
                        val refreshToken = json["refreshToken"]?.jsonPrimitive?.content ?: ""
                        val deviceInfo = json["deviceInfo"]?.jsonPrimitive?.content
                        try {
                            val tokenPair = mockAuthService.refresh(refreshToken, deviceInfo)
                            call.respondText(
                                """{"success":true,"data":{"accessToken":"${tokenPair.accessToken}","refreshToken":"${tokenPair.refreshToken}","expiresIn":${tokenPair.expiresIn}}}""",
                                ContentType.Application.Json
                            )
                        } catch (e: IllegalArgumentException) {
                            call.respondText(
                                """{"success":false,"error":{"code":"${e.message}","message":"토큰이 만료되었습니다."}}""",
                                ContentType.Application.Json,
                                HttpStatusCode.BadRequest
                            )
                        }
                    }
                }
            }
        }

        val client = createJsonClient()
        val response = client.post("/api/v1/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody("""{"refreshToken":"revoked-token"}""")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertTrue(response.bodyAsText().contains("AUTH_REFRESH_REVOKED"))
    }

    @Test
    fun `accessing protected route without token returns 401`() = testApplication {
        configureTestAppWithAuth {
            routing {
                route("/api/v1") {
                    authenticate("auth-jwt") {
                        get("/protected") {
                            call.respondText("ok")
                        }
                    }
                }
            }
        }

        val response = client.get("/api/v1/protected")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `accessing protected route with valid token returns 200`() = testApplication {
        configureTestAppWithAuth {
            routing {
                route("/api/v1") {
                    authenticate("auth-jwt") {
                        get("/protected") {
                            call.respondText("ok")
                        }
                    }
                }
            }
        }

        val token = generateTestToken(userId = 1L, nickname = "테스터")
        val response = client.get("/api/v1/protected") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }
}
