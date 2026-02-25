package com.fridgerecipe.routes

import com.fridgerecipe.core.extension.userId
import com.fridgerecipe.data.database.*
import com.fridgerecipe.plugins.BadRequestException
import com.fridgerecipe.plugins.NotFoundException
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.recipeRoutes() {
    route("/recipes") {
        authenticate("auth-jwt") {
            get("/recommend") {
                val userId = call.userId()
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20
                val minMatch = call.request.queryParameters["minMatchRatio"]?.toDoubleOrNull() ?: 0.5

                val recommendations = transaction {
                    // Get user's ingredient IDs
                    val userIngredientIds = UserIngredientsTable
                        .selectAll()
                        .where { UserIngredientsTable.userId eq userId }
                        .map { it[UserIngredientsTable.ingredientId] }
                        .toSet()

                    if (userIngredientIds.isEmpty()) {
                        return@transaction emptyList()
                    }

                    // Get all recipes with their required ingredients
                    val recipeIngredients = RecipeIngredientsTable
                        .selectAll()
                        .groupBy { it[RecipeIngredientsTable.recipeId] }

                    // Calculate match ratio for each recipe
                    recipeIngredients.mapNotNull { (recipeId, ingredients) ->
                        val essentialIds = ingredients
                            .filter { it[RecipeIngredientsTable.isEssential] }
                            .map { it[RecipeIngredientsTable.ingredientId] }
                        val totalRequired = essentialIds.size
                        if (totalRequired == 0) return@mapNotNull null

                        val matchedCount = essentialIds.count { it in userIngredientIds }
                        val matchRatio = matchedCount.toDouble() / totalRequired

                        if (matchRatio < minMatch) return@mapNotNull null

                        val recipe = RecipesTable.selectAll()
                            .where { RecipesTable.id eq recipeId }
                            .firstOrNull() ?: return@mapNotNull null

                        val missingNames = ingredients
                            .filter { it[RecipeIngredientsTable.ingredientId] !in userIngredientIds }
                            .mapNotNull { ing ->
                                IngredientMasterTable.selectAll()
                                    .where { IngredientMasterTable.id eq ing[RecipeIngredientsTable.ingredientId] }
                                    .firstOrNull()?.get(IngredientMasterTable.name)
                            }

                        mapOf(
                            "recipeId" to recipe[RecipesTable.id].value,
                            "title" to recipe[RecipesTable.title],
                            "thumbnailUrl" to recipe[RecipesTable.thumbnailUrl],
                            "matchRatio" to matchRatio,
                            "matchedCount" to matchedCount,
                            "totalRequired" to totalRequired,
                            "missingIngredients" to missingNames,
                            "cookingTime" to recipe[RecipesTable.cookingTime],
                            "difficulty" to recipe[RecipesTable.difficulty],
                            "calories" to recipe[RecipesTable.calories],
                        )
                    }
                    .sortedByDescending { it["matchRatio"] as Double }
                    .drop((page - 1) * size)
                    .take(size)
                }

                call.respond(HttpStatusCode.OK, mapOf("success" to true, "data" to recommendations))
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                    ?: throw BadRequestException("유효하지 않은 ID입니다.")

                val recipe = transaction {
                    RecipesTable.selectAll()
                        .where { RecipesTable.id eq id }
                        .firstOrNull()
                } ?: throw NotFoundException("레시피를 찾을 수 없습니다.")

                val ingredients = transaction {
                    (RecipeIngredientsTable innerJoin IngredientMasterTable)
                        .selectAll()
                        .where { RecipeIngredientsTable.recipeId eq id }
                        .map { row ->
                            mapOf(
                                "ingredientId" to row[RecipeIngredientsTable.ingredientId],
                                "name" to row[IngredientMasterTable.name],
                                "quantity" to row[RecipeIngredientsTable.quantity],
                                "isEssential" to row[RecipeIngredientsTable.isEssential],
                            )
                        }
                }

                // Increment view count
                transaction {
                    RecipesTable.update({ RecipesTable.id eq id }) {
                        with(SqlExpressionBuilder) {
                            it[viewCount] = viewCount + 1
                        }
                    }
                }

                call.respond(HttpStatusCode.OK, mapOf(
                    "success" to true,
                    "data" to mapOf(
                        "id" to recipe[RecipesTable.id].value,
                        "title" to recipe[RecipesTable.title],
                        "description" to recipe[RecipesTable.description],
                        "cuisineType" to recipe[RecipesTable.cuisineType],
                        "difficulty" to recipe[RecipesTable.difficulty],
                        "cookingTime" to recipe[RecipesTable.cookingTime],
                        "servings" to recipe[RecipesTable.servings],
                        "calories" to recipe[RecipesTable.calories],
                        "thumbnailUrl" to recipe[RecipesTable.thumbnailUrl],
                        "steps" to recipe[RecipesTable.steps],
                        "nutrition" to recipe[RecipesTable.nutrition],
                        "tags" to recipe[RecipesTable.tags],
                        "avgRating" to recipe[RecipesTable.avgRating],
                        "viewCount" to recipe[RecipesTable.viewCount],
                        "ingredients" to ingredients,
                    )
                ))
            }

            get("/search") {
                val query = call.request.queryParameters["q"] ?: ""
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20

                val recipes = transaction {
                    RecipesTable.selectAll()
                        .where { RecipesTable.title like "%$query%" }
                        .orderBy(RecipesTable.avgRating, SortOrder.DESC)
                        .limit(size).offset(((page - 1) * size).toLong())
                        .map { row ->
                            mapOf(
                                "id" to row[RecipesTable.id].value,
                                "title" to row[RecipesTable.title],
                                "cuisineType" to row[RecipesTable.cuisineType],
                                "difficulty" to row[RecipesTable.difficulty],
                                "cookingTime" to row[RecipesTable.cookingTime],
                                "thumbnailUrl" to row[RecipesTable.thumbnailUrl],
                                "avgRating" to row[RecipesTable.avgRating],
                            )
                        }
                }
                call.respond(HttpStatusCode.OK, mapOf("success" to true, "data" to recipes))
            }

            post("/{id}/bookmark") {
                val userId = call.userId()
                val recipeId = call.parameters["id"]?.toLongOrNull()
                    ?: throw BadRequestException("유효하지 않은 ID입니다.")
                transaction {
                    BookmarksTable.insertIgnore {
                        it[BookmarksTable.userId] = userId
                        it[BookmarksTable.recipeId] = recipeId
                    }
                }
                call.respond(HttpStatusCode.Created, mapOf("success" to true))
            }

            delete("/{id}/bookmark") {
                val userId = call.userId()
                val recipeId = call.parameters["id"]?.toLongOrNull()
                    ?: throw BadRequestException("유효하지 않은 ID입니다.")
                transaction {
                    BookmarksTable.deleteWhere {
                        (BookmarksTable.userId eq userId) and (BookmarksTable.recipeId eq recipeId)
                    }
                }
                call.respond(HttpStatusCode.OK, mapOf("success" to true))
            }

            get("/bookmarks") {
                val userId = call.userId()
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20

                val bookmarks = transaction {
                    (BookmarksTable innerJoin RecipesTable)
                        .selectAll()
                        .where { BookmarksTable.userId eq userId }
                        .orderBy(BookmarksTable.createdAt, SortOrder.DESC)
                        .limit(size).offset(((page - 1) * size).toLong())
                        .map { row ->
                            mapOf(
                                "id" to row[RecipesTable.id].value,
                                "title" to row[RecipesTable.title],
                                "cuisineType" to row[RecipesTable.cuisineType],
                                "cookingTime" to row[RecipesTable.cookingTime],
                                "thumbnailUrl" to row[RecipesTable.thumbnailUrl],
                                "avgRating" to row[RecipesTable.avgRating],
                            )
                        }
                }
                call.respond(HttpStatusCode.OK, mapOf("success" to true, "data" to bookmarks))
            }
        }
    }
}
