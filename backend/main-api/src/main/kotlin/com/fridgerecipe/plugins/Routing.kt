package com.fridgerecipe.plugins

import com.fridgerecipe.core.middleware.RateLimiter
import com.fridgerecipe.core.middleware.RateLimitPlugin
import com.fridgerecipe.routes.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val rateLimiter by inject<RateLimiter>()

    routing {
        route("/api/v1") {
            healthRoutes()
            authRoutes()
            authenticate("auth-jwt") {
                install(RateLimitPlugin) {
                    this.rateLimiter = rateLimiter
                    defaultLimit = 100
                    defaultWindowSeconds = 60
                    scanLimit = 10
                    scanWindowSeconds = 86400
                }
                userRoutes()
                ingredientRoutes()
                recipeRoutes()
                scanRoutes()
                notificationRoutes()
            }
        }
    }
}
