package com.fridgerecipe.routes

import com.fridgerecipe.core.extension.userId
import com.fridgerecipe.data.database.IngredientMasterTable
import com.fridgerecipe.data.database.UserIngredientsTable
import com.fridgerecipe.plugins.BadRequestException
import com.fridgerecipe.plugins.NotFoundException
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class AddIngredientRequest(
    val ingredientId: Long,
    val quantity: Double? = null,
    val unit: String? = null,
    val expiryDate: LocalDate? = null,
    val storageType: String = "fridge",
    val memo: String? = null,
)

@Serializable
data class BatchIngredientRequest(
    val items: List<AddIngredientRequest>,
)

@Serializable
data class BatchDeleteRequest(
    val ids: List<Long>,
)

fun Route.ingredientRoutes() {
    route("/ingredients") {
        authenticate("auth-jwt") {
            get {
                val userId = call.userId()
                val ingredients = transaction {
                    (UserIngredientsTable innerJoin IngredientMasterTable)
                        .selectAll()
                        .where { UserIngredientsTable.userId eq userId }
                        .orderBy(UserIngredientsTable.createdAt, SortOrder.DESC)
                        .map { row ->
                            mapOf(
                                "id" to row[UserIngredientsTable.id].value,
                                "ingredientId" to row[UserIngredientsTable.ingredientId],
                                "name" to row[IngredientMasterTable.name],
                                "category" to row[IngredientMasterTable.category],
                                "iconUrl" to row[IngredientMasterTable.iconUrl],
                                "quantity" to row[UserIngredientsTable.quantity]?.toDouble(),
                                "unit" to row[UserIngredientsTable.unit],
                                "expiryDate" to row[UserIngredientsTable.expiryDate]?.toString(),
                                "storageType" to row[UserIngredientsTable.storageType],
                                "memo" to row[UserIngredientsTable.memo],
                                "registeredVia" to row[UserIngredientsTable.registeredVia],
                            )
                        }
                }
                call.respond(HttpStatusCode.OK, mapOf("success" to true, "data" to ingredients))
            }

            post {
                val userId = call.userId()
                val request = call.receive<AddIngredientRequest>()
                if (request.memo != null && request.memo.length > 200) {
                    throw BadRequestException("메모는 200자 이내여야 합니다.")
                }
                val id = transaction {
                    UserIngredientsTable.insertAndGetId {
                        it[UserIngredientsTable.userId] = userId
                        it[ingredientId] = request.ingredientId
                        it[quantity] = request.quantity?.toBigDecimal()
                        it[unit] = request.unit
                        it[expiryDate] = request.expiryDate
                        it[storageType] = request.storageType
                        it[memo] = request.memo
                    }.value
                }
                call.respond(HttpStatusCode.Created, mapOf("success" to true, "data" to mapOf("id" to id)))
            }

            post("/batch") {
                val userId = call.userId()
                val request = call.receive<BatchIngredientRequest>()
                val ids = transaction {
                    request.items.map { item ->
                        UserIngredientsTable.insertAndGetId {
                            it[UserIngredientsTable.userId] = userId
                            it[ingredientId] = item.ingredientId
                            it[quantity] = item.quantity?.toBigDecimal()
                            it[unit] = item.unit
                            it[expiryDate] = item.expiryDate
                            it[storageType] = item.storageType
                            it[memo] = item.memo
                        }.value
                    }
                }
                call.respond(HttpStatusCode.Created, mapOf("success" to true, "data" to mapOf("ids" to ids)))
            }

            put("/{id}") {
                val userId = call.userId()
                val id = call.parameters["id"]?.toLongOrNull()
                    ?: throw BadRequestException("유효하지 않은 ID입니다.")
                val request = call.receive<AddIngredientRequest>()

                val updated = transaction {
                    UserIngredientsTable.update(
                        { (UserIngredientsTable.id eq id) and (UserIngredientsTable.userId eq userId) }
                    ) {
                        it[ingredientId] = request.ingredientId
                        it[quantity] = request.quantity?.toBigDecimal()
                        it[unit] = request.unit
                        it[expiryDate] = request.expiryDate
                        it[storageType] = request.storageType
                        it[memo] = request.memo
                    }
                }
                if (updated == 0) throw NotFoundException("해당 식재료를 찾을 수 없습니다.")
                call.respond(HttpStatusCode.OK, mapOf("success" to true))
            }

            delete("/{id}") {
                val userId = call.userId()
                val id = call.parameters["id"]?.toLongOrNull()
                    ?: throw BadRequestException("유효하지 않은 ID입니다.")
                val deleted = transaction {
                    UserIngredientsTable.deleteWhere {
                        (UserIngredientsTable.id eq id) and (UserIngredientsTable.userId eq userId)
                    }
                }
                if (deleted == 0) throw NotFoundException("해당 식재료를 찾을 수 없습니다.")
                call.respond(HttpStatusCode.OK, mapOf("success" to true))
            }

            delete("/batch") {
                val userId = call.userId()
                val request = call.receive<BatchDeleteRequest>()
                transaction {
                    UserIngredientsTable.deleteWhere {
                        (UserIngredientsTable.id inList request.ids) and (UserIngredientsTable.userId eq userId)
                    }
                }
                call.respond(HttpStatusCode.OK, mapOf("success" to true))
            }

            get("/search") {
                val query = call.request.queryParameters["q"] ?: ""
                if (query.length < 1) {
                    call.respond(HttpStatusCode.OK, mapOf("success" to true, "data" to emptyList<Any>()))
                    return@get
                }
                val results = transaction {
                    IngredientMasterTable.selectAll()
                        .where { IngredientMasterTable.name like "%$query%" }
                        .limit(20)
                        .map { row ->
                            mapOf(
                                "id" to row[IngredientMasterTable.id].value,
                                "name" to row[IngredientMasterTable.name],
                                "category" to row[IngredientMasterTable.category],
                                "iconUrl" to row[IngredientMasterTable.iconUrl],
                                "defaultUnit" to row[IngredientMasterTable.defaultUnit],
                                "defaultExpiryDays" to row[IngredientMasterTable.defaultExpiryDays],
                            )
                        }
                }
                call.respond(HttpStatusCode.OK, mapOf("success" to true, "data" to results))
            }

            get("/categories") {
                val categories = transaction {
                    IngredientMasterTable
                        .select(IngredientMasterTable.category, IngredientMasterTable.id.count())
                        .groupBy(IngredientMasterTable.category)
                        .map { row ->
                            mapOf(
                                "name" to row[IngredientMasterTable.category],
                                "count" to row[IngredientMasterTable.id.count()],
                            )
                        }
                }
                call.respond(HttpStatusCode.OK, mapOf("success" to true, "data" to categories))
            }

            get("/expiring") {
                val userId = call.userId()
                val ingredients = transaction {
                    (UserIngredientsTable innerJoin IngredientMasterTable)
                        .selectAll()
                        .where {
                            (UserIngredientsTable.userId eq userId) and
                            (UserIngredientsTable.expiryDate.isNotNull())
                        }
                        .orderBy(UserIngredientsTable.expiryDate, SortOrder.ASC)
                        .map { row ->
                            mapOf(
                                "id" to row[UserIngredientsTable.id].value,
                                "name" to row[IngredientMasterTable.name],
                                "expiryDate" to row[UserIngredientsTable.expiryDate]?.toString(),
                                "storageType" to row[UserIngredientsTable.storageType],
                            )
                        }
                }
                call.respond(HttpStatusCode.OK, mapOf("success" to true, "data" to ingredients))
            }
        }
    }
}
