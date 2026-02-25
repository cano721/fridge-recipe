package com.fridgerecipe.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fridgerecipe.core.config.AppConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "fridge-recipe"
            verifier(
                JWT.require(Algorithm.HMAC256(AppConfig.jwtSecret))
                    .withIssuer(AppConfig.jwtIssuer)
                    .withAudience(AppConfig.jwtAudience)
                    .build()
            )
            validate { credential ->
                val userId = credential.payload.getClaim("userId")?.asLong()
                if (userId != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf(
                    "success" to false,
                    "error" to mapOf(
                        "code" to "AUTH_REQUIRED",
                        "message" to "인증이 필요합니다."
                    )
                ))
            }
        }
    }
}
