package com.fridgerecipe.plugins

import com.fridgerecipe.core.config.AppConfig
import com.fridgerecipe.core.middleware.RateLimiter
import com.fridgerecipe.core.middleware.RateLimitPlugin
import com.fridgerecipe.domain.service.*
import com.fridgerecipe.routes.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val rateLimiter by inject<RateLimiter>()
    val appConfig by inject<AppConfig>()
    val authService by inject<AuthService>()
    val userService by inject<UserService>()
    val ingredientService by inject<IngredientService>()
    val recipeService by inject<RecipeService>()
    val scanService by inject<ScanService>()
    val notificationService by inject<NotificationService>()

    routing {
        route("/api/v1") {
            healthRoutes(appConfig)
            authRoutes(authService)
            authenticate("auth-jwt") {
                install(RateLimitPlugin) {
                    this.rateLimiter = rateLimiter
                    defaultLimit = 100
                    defaultWindowSeconds = 60
                    scanLimit = 10
                    scanWindowSeconds = 86400
                }
                userRoutes(userService)
                ingredientRoutes(ingredientService)
                recipeRoutes(recipeService)
                scanRoutes(scanService)
                notificationRoutes(notificationService)
            }
        }
    }
}
