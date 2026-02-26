package com.fridgerecipe.routes

import com.fridgerecipe.domain.service.AuthService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable
data class LoginRequest(val provider: String, val accessToken: String, val deviceInfo: String? = null)

@Serializable
data class RefreshRequest(val refreshToken: String, val deviceInfo: String? = null)

@Serializable
data class TokenResponse(val accessToken: String, val refreshToken: String, val expiresIn: Long)

@Serializable
data class ApiSuccess<T>(val success: Boolean = true, val data: T)

fun Route.authRoutes() {
    val authService by inject<AuthService>()

    route("/auth") {
        post("/login") {
            val request = call.receive<LoginRequest>()
            val tokenPair = authService.login(request.provider, request.accessToken, request.deviceInfo)
            call.respond(HttpStatusCode.OK, ApiSuccess(data = TokenResponse(
                accessToken = tokenPair.accessToken,
                refreshToken = tokenPair.refreshToken,
                expiresIn = tokenPair.expiresIn
            )))
        }

        post("/refresh") {
            val request = call.receive<RefreshRequest>()
            val tokenPair = authService.refresh(request.refreshToken, request.deviceInfo)
            call.respond(HttpStatusCode.OK, ApiSuccess(data = TokenResponse(
                accessToken = tokenPair.accessToken,
                refreshToken = tokenPair.refreshToken,
                expiresIn = tokenPair.expiresIn
            )))
        }

        authenticate("auth-jwt") {
            post("/logout") {
                val principal = call.principal<JWTPrincipal>()!!
                val userId = principal.payload.getClaim("userId").asLong()
                val token = call.request.headers["Authorization"]?.removePrefix("Bearer ") ?: ""
                authService.logout(token, userId)
                call.respond(HttpStatusCode.OK, ApiSuccess(data = mapOf("message" to "로그아웃 되었습니다.")))
            }
        }
    }
}
