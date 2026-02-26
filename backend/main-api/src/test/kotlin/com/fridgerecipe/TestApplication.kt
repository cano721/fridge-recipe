package com.fridgerecipe

import com.fridgerecipe.core.config.AppConfig
import com.fridgerecipe.core.security.JwtService
import com.fridgerecipe.plugins.configureSerialization
import com.fridgerecipe.plugins.configureStatusPages
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json

val testJwtConfig = AppConfig.JwtConfig(
    secret = "test-jwt-secret-key-for-testing-only",
    issuer = "fridge-recipe",
    audience = "fridge-recipe-app"
)

val testJwtService = JwtService(testJwtConfig)

fun generateTestToken(userId: Long = 1L, nickname: String = "테스터"): String =
    testJwtService.generateAccessToken(userId, nickname)

fun ApplicationTestBuilder.configureTestApp(block: Application.() -> Unit = {}) {
    application {
        configureSerialization()
        configureStatusPages()
        block()
    }
}

fun ApplicationTestBuilder.configureTestAppWithAuth(block: Application.() -> Unit = {}) {
    application {
        configureSerialization()
        configureStatusPages()
        install(Authentication) {
            jwt("auth-jwt") {
                verifier(testJwtService.verifier)
                validate { credential ->
                    val userId = credential.payload.getClaim("userId").asLong()
                    if (userId != null) JWTPrincipal(credential.payload) else null
                }
                challenge { _, _ ->
                    call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                }
            }
        }
        block()
    }
}

fun ApplicationTestBuilder.createJsonClient() = createClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
}
