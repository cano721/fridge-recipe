package com.fridgerecipe.routes

import com.fridgerecipe.domain.service.IngredientService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable
data class IngredientRegisterRequest(
    val ingredientId: Long,
    val quantity: Double? = null,
    val unit: String? = null,
    val expiryDate: String? = null,
    val storageType: String = "fridge",
    val memo: String? = null
)

@Serializable
data class BatchRegisterRequest(
    val ingredients: List<IngredientRegisterRequest>,
    val conflictStrategy: String = "MERGE"
)

@Serializable
data class IngredientUpdateRequest(
    val quantity: Double? = null,
    val unit: String? = null,
    val expiryDate: String? = null,
    val storageType: String? = null,
    val memo: String? = null
)

@Serializable
data class DeleteBatchRequest(val ids: List<Long>)

fun Route.ingredientRoutes() {
    val ingredientService by inject<IngredientService>()

    route("/ingredients") {
        // 목록 조회
        get {
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
            val page = call.parameters["page"]?.toIntOrNull() ?: 1
            val size = call.parameters["size"]?.toIntOrNull() ?: 20
            val ingredients = ingredientService.getMyIngredients(userId, page, size)
            val total = ingredientService.countMyIngredients(userId)
            call.respond(mapOf("success" to true, "data" to ingredients.map { it.toResponse() }, "meta" to mapOf("page" to page, "size" to size, "total" to total)))
        }

        // 단건 등록
        post {
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
            val req = call.receive<IngredientRegisterRequest>()
            val ingredient = ingredientService.register(
                userId = userId,
                ingredientId = req.ingredientId,
                quantity = req.quantity?.let { java.math.BigDecimal.valueOf(it) },
                unit = req.unit,
                expiryDate = req.expiryDate,
                storageType = req.storageType,
                memo = req.memo
            )
            call.respond(HttpStatusCode.Created, mapOf("success" to true, "data" to ingredient.toResponse()))
        }

        // 일괄 등록
        post("/batch") {
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
            val req = call.receive<BatchRegisterRequest>()
            val items = req.ingredients.map {
                IngredientService.BatchItem(
                    ingredientId = it.ingredientId,
                    quantity = it.quantity?.let { q -> java.math.BigDecimal.valueOf(q) },
                    unit = it.unit,
                    expiryDate = it.expiryDate,
                    storageType = it.storageType,
                    memo = it.memo
                )
            }
            val result = ingredientService.registerBatch(userId, items, req.conflictStrategy)
            call.respond(HttpStatusCode.Created, mapOf(
                "success" to true,
                "data" to mapOf(
                    "registered" to result.registered.map { it.toResponse() },
                    "errors" to result.errors
                )
            ))
        }

        // 수정
        put("/{id}") {
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
            val id = call.parameters["id"]?.toLongOrNull() ?: throw IllegalArgumentException("VALIDATION_FAILED")
            val req = call.receive<IngredientUpdateRequest>()
            val ingredient = ingredientService.update(
                userId = userId,
                id = id,
                quantity = req.quantity?.let { java.math.BigDecimal.valueOf(it) },
                unit = req.unit,
                expiryDate = req.expiryDate,
                storageType = req.storageType,
                memo = req.memo
            )
            call.respond(mapOf("success" to true, "data" to ingredient.toResponse()))
        }

        // 삭제
        delete("/{id}") {
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
            val id = call.parameters["id"]?.toLongOrNull() ?: throw IllegalArgumentException("VALIDATION_FAILED")
            ingredientService.delete(userId, id)
            call.respond(mapOf("success" to true, "data" to mapOf("message" to "삭제되었습니다.")))
        }

        // 일괄 삭제
        post("/delete-batch") {
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
            val req = call.receive<DeleteBatchRequest>()
            val count = ingredientService.deleteBatch(userId, req.ids)
            call.respond(mapOf("success" to true, "data" to mapOf("deleted" to count)))
        }

        // 자동완성 검색
        get("/search") {
            val query = call.parameters["q"] ?: ""
            if (query.isEmpty()) {
                call.respond(mapOf("success" to true, "data" to emptyList<Any>()))
                return@get
            }
            val results = ingredientService.search(query)
            call.respond(mapOf("success" to true, "data" to results.map {
                mapOf(
                    "id" to it.id,
                    "name" to it.name,
                    "category" to it.category,
                    "iconUrl" to it.iconUrl,
                    "defaultUnit" to it.defaultUnit
                )
            }))
        }

        // 카테고리 목록
        get("/categories") {
            val categories = ingredientService.getCategories()
            call.respond(mapOf("success" to true, "data" to categories))
        }

        // 소비기한 임박 목록
        get("/expiring") {
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
            val days = call.parameters["days"]?.toIntOrNull() ?: 3
            val expiring = ingredientService.getExpiring(userId, days)
            call.respond(mapOf("success" to true, "data" to expiring.map { it.toResponse() }))
        }
    }
}

private fun com.fridgerecipe.domain.model.UserIngredient.toResponse() = mapOf(
    "id" to id,
    "ingredientId" to ingredientId,
    "ingredientName" to ingredientName,
    "category" to category,
    "iconUrl" to iconUrl,
    "quantity" to quantity?.toDouble(),
    "unit" to unit,
    "expiryDate" to expiryDate,
    "storageType" to storageType,
    "memo" to memo,
    "registeredVia" to registeredVia,
    "createdAt" to createdAt
)
