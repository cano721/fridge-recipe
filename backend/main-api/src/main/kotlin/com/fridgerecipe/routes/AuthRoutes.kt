package com.fridgerecipe.routes

import com.fridgerecipe.auth.AuthService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable
data class LoginRequest(
    val provider: String,
    val accessToken: String,
)

fun Route.authRoutes() {
    val authService by inject<AuthService>()

    route("/auth") {
        post("/login") {
            val request = call.receive<LoginRequest>()
            // TODO: Validate OAuth token with provider (Kakao, Google, Apple)
            // For MVP, we'll create/find user with the provider info
            val tokenPair = authService.loginOrRegister(
                provider = request.provider,
                oauthId = request.accessToken.take(32), // Placeholder - validate with real OAuth
                email = null,
                nickname = "사용자",
            )
            call.respond(HttpStatusCode.OK, mapOf(
                "success" to true,
                "data" to tokenPair,
            ))
        }

        post("/refresh") {
            // TODO: Implement token refresh
            call.respond(HttpStatusCode.OK, mapOf("success" to true))
        }

        post("/logout") {
            // TODO: Invalidate token in Redis
            call.respond(HttpStatusCode.OK, mapOf("success" to true))
        }
    }
}
