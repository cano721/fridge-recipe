package com.fridgerecipe.routes

import com.fridgerecipe.core.security.userId
import com.fridgerecipe.core.security.userIdOrNull
import com.fridgerecipe.domain.service.RecipeService
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
fun Route.recipeRoutes(recipeService: RecipeService) {

    route("/recipes") {
        get("/recommend") {
            val userId = call.userId
            val recommendations = recipeService.getRecommendations(userId)
            call.respond(mapOf("success" to true, "data" to recommendations))
        }

        get("/search") {
            val query = call.parameters["q"] ?: ""
            val page = call.parameters["page"]?.toIntOrNull() ?: 1
            val size = call.parameters["size"]?.toIntOrNull() ?: 20
            if (query.isBlank()) {
                call.respond(mapOf("success" to true, "data" to emptyList<Any>(), "meta" to mapOf("total" to 0)))
                return@get
            }
            val (recipes, total) = recipeService.search(query, page, size)
            call.respond(mapOf(
                "success" to true,
                "data" to recipes,
                "meta" to mapOf("page" to page, "size" to size, "total" to total)
            ))
        }

        get("/bookmarks") {
            val userId = call.userId
            val page = call.parameters["page"]?.toIntOrNull() ?: 1
            val size = call.parameters["size"]?.toIntOrNull() ?: 20
            val (recipes, total) = recipeService.getBookmarks(userId, page, size)
            call.respond(mapOf(
                "success" to true,
                "data" to recipes,
                "meta" to mapOf("page" to page, "size" to size, "total" to total)
            ))
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("VALIDATION_FAILED")
            val userId = call.userIdOrNull
            val detail = recipeService.getDetail(id, userId)
            call.respond(mapOf("success" to true, "data" to detail))
        }

        post("/{id}/bookmark") {
            val userId = call.userId
            val recipeId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("VALIDATION_FAILED")
            recipeService.addBookmark(userId, recipeId)
            call.respond(HttpStatusCode.Created, mapOf("success" to true, "data" to mapOf("message" to "북마크가 추가되었습니다.")))
        }

        delete("/{id}/bookmark") {
            val userId = call.userId
            val recipeId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("VALIDATION_FAILED")
            recipeService.removeBookmark(userId, recipeId)
            call.respond(mapOf("success" to true, "data" to mapOf("message" to "북마크가 삭제되었습니다.")))
        }
    }
}
