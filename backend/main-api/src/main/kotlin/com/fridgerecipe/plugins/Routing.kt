package com.fridgerecipe.plugins

import com.fridgerecipe.routes.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/health") {
            call.respond(mapOf("status" to "ok"))
        }

        route("/api/v1") {
            authRoutes()
            userRoutes()
            ingredientRoutes()
            recipeRoutes()
            scanRoutes()
        }
    }
}
