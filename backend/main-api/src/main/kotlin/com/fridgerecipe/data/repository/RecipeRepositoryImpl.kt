package com.fridgerecipe.data.repository

import com.fridgerecipe.data.database.tables.IngredientMaster
import com.fridgerecipe.data.database.tables.RecipeIngredients
import com.fridgerecipe.data.database.tables.Recipes
import com.fridgerecipe.domain.model.CookingStep
import com.fridgerecipe.domain.model.NutritionInfo
import com.fridgerecipe.domain.model.Recipe
import com.fridgerecipe.domain.model.RecipeIngredient
import com.fridgerecipe.domain.repository.RecipeRepository
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

class RecipeRepositoryImpl : RecipeRepository {

    private fun parseSteps(jsonArray: JsonArray): List<CookingStep> = jsonArray.mapIndexed { idx, element ->
        val obj = element.jsonObject
        CookingStep(
            order = obj["order"]?.jsonPrimitive?.intOrNull ?: (idx + 1),
            description = obj["description"]?.jsonPrimitive?.content ?: "",
            imageUrl = obj["imageUrl"]?.jsonPrimitive?.content,
            timer = obj["timer"]?.jsonPrimitive?.intOrNull
        )
    }

    private fun parseNutrition(jsonObject: JsonObject): NutritionInfo = NutritionInfo(
        calories = jsonObject["calories"]?.jsonPrimitive?.intOrNull,
        protein = jsonObject["protein"]?.jsonPrimitive?.doubleOrNull,
        fat = jsonObject["fat"]?.jsonPrimitive?.doubleOrNull,
        carbs = jsonObject["carbs"]?.jsonPrimitive?.doubleOrNull,
        sodium = jsonObject["sodium"]?.jsonPrimitive?.doubleOrNull
    )

    private fun ResultRow.toRecipe() = Recipe(
        id = this[Recipes.id].value,
        title = this[Recipes.title],
        description = this[Recipes.description],
        imageUrl = null,
        thumbnailUrl = this[Recipes.thumbnailUrl],
        servings = this[Recipes.servings],
        cookingTime = this[Recipes.cookingTime] ?: 0,
        difficulty = this[Recipes.difficulty] ?: "medium",
        cuisineType = this[Recipes.cuisineType],
        sourceUrl = this[Recipes.sourceUrl],
        sourceType = this[Recipes.sourceType],
        steps = runCatching { parseSteps(this[Recipes.steps]) }.getOrNull(),
        nutrition = this[Recipes.nutrition]?.let { runCatching { parseNutrition(it) }.getOrNull() },
        tags = this[Recipes.tags],
        viewCount = this[Recipes.viewCount].toLong(),
        avgRating = this[Recipes.avgRating].toDouble(),
        reviewCount = 0
    )

    private fun ResultRow.toRecipeIngredient() = RecipeIngredient(
        ingredientId = this[RecipeIngredients.ingredientId],
        ingredientName = this.getOrNull(IngredientMaster.name) ?: "",
        quantity = this[RecipeIngredients.quantity]?.toDoubleOrNull(),
        unit = null,
        isEssential = this[RecipeIngredients.isEssential],
        substituteIds = this[RecipeIngredients.substituteIds]
    )

    override suspend fun findById(id: Long): Recipe? = newSuspendedTransaction {
        Recipes.selectAll().where { Recipes.id eq id }.singleOrNull()?.toRecipe()
    }

    override suspend fun findAll(page: Int, size: Int): Pair<List<Recipe>, Long> = newSuspendedTransaction {
        val total = Recipes.selectAll().count()
        val items = Recipes.selectAll()
            .orderBy(Recipes.viewCount, org.jetbrains.exposed.sql.SortOrder.DESC)
            .limit(size).offset(((page - 1) * size).toLong())
            .map { it.toRecipe() }
        Pair(items, total)
    }

    override suspend fun search(query: String, page: Int, size: Int): Pair<List<Recipe>, Long> = newSuspendedTransaction {
        val results = mutableListOf<Recipe>()
        exec(
            """
            SELECT id FROM recipes
            WHERE title ILIKE ? OR description ILIKE ?
            ORDER BY view_count DESC
            LIMIT ? OFFSET ?
            """.trimIndent(),
            args = listOf(
                Pair(org.jetbrains.exposed.sql.VarCharColumnType(), "%$query%"),
                Pair(org.jetbrains.exposed.sql.VarCharColumnType(), "%$query%"),
                Pair(org.jetbrains.exposed.sql.IntegerColumnType(), size),
                Pair(org.jetbrains.exposed.sql.IntegerColumnType(), (page - 1) * size)
            )
        ) { rs ->
            while (rs.next()) {
                val id = rs.getLong("id")
                Recipes.selectAll().where { Recipes.id eq id }.singleOrNull()?.toRecipe()?.let { results.add(it) }
            }
        }
        var total = 0L
        exec(
            "SELECT COUNT(*) as cnt FROM recipes WHERE title ILIKE ? OR description ILIKE ?",
            args = listOf(
                Pair(org.jetbrains.exposed.sql.VarCharColumnType(), "%$query%"),
                Pair(org.jetbrains.exposed.sql.VarCharColumnType(), "%$query%")
            )
        ) { rs ->
            if (rs.next()) total = rs.getLong("cnt")
        }
        Pair(results, total)
    }

    override suspend fun findWithIngredients(id: Long): Pair<Recipe, List<RecipeIngredient>>? = newSuspendedTransaction {
        val recipe = Recipes.selectAll().where { Recipes.id eq id }.singleOrNull()?.toRecipe() ?: return@newSuspendedTransaction null
        val ingredients = RecipeIngredients
            .join(IngredientMaster, JoinType.LEFT, additionalConstraint = { RecipeIngredients.ingredientId eq IngredientMaster.id })
            .selectAll()
            .where { RecipeIngredients.recipeId eq id }
            .map { it.toRecipeIngredient() }
        Pair(recipe, ingredients)
    }

    override suspend fun findRecipesWithIngredientIds(ingredientIds: List<Long>): List<Pair<Recipe, List<RecipeIngredient>>> = newSuspendedTransaction {
        if (ingredientIds.isEmpty()) return@newSuspendedTransaction emptyList()

        val recipeIds = RecipeIngredients.selectAll()
            .where { RecipeIngredients.ingredientId inList ingredientIds }
            .map { it[RecipeIngredients.recipeId] }
            .distinct()

        if (recipeIds.isEmpty()) return@newSuspendedTransaction emptyList()

        val recipes = Recipes.selectAll()
            .where { Recipes.id inList recipeIds }
            .map { it.toRecipe() }

        val allIngredients = RecipeIngredients
            .join(IngredientMaster, JoinType.LEFT, additionalConstraint = { RecipeIngredients.ingredientId eq IngredientMaster.id })
            .selectAll()
            .where { RecipeIngredients.recipeId inList recipeIds }
            .map { Pair(it[RecipeIngredients.recipeId], it.toRecipeIngredient()) }

        val ingredientsByRecipe = allIngredients.groupBy({ it.first }, { it.second })

        recipes.map { recipe ->
            Pair(recipe, ingredientsByRecipe[recipe.id] ?: emptyList())
        }
    }

    override suspend fun incrementViewCount(id: Long) = newSuspendedTransaction {
        Recipes.update({ Recipes.id eq id }) {
            with(org.jetbrains.exposed.sql.SqlExpressionBuilder) {
                it.update(Recipes.viewCount, Recipes.viewCount + 1)
            }
        }
        Unit
    }
}
