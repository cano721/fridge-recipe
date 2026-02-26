package com.fridgerecipe.plugins

import com.fridgerecipe.core.security.JwtService
import com.fridgerecipe.core.security.TokenBlacklist
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val jwtService by inject<JwtService>()
    val tokenBlacklist by inject<TokenBlacklist>()

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(jwtService.verifier)
            validate { credential ->
                val token = request.headers["Authorization"]?.removePrefix("Bearer ")
                if (token != null && tokenBlacklist.isBlacklisted(token)) {
                    return@validate null
                }
                val userId = credential.payload.getClaim("userId").asLong()
                if (userId != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse(error = ApiError("AUTH_INVALID_TOKEN", "유효하지 않은 토큰입니다."))
                )
            }
        }
    }
}
