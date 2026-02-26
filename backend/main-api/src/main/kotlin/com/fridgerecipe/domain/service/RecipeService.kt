package com.fridgerecipe.domain.service

import com.fridgerecipe.domain.model.Recipe
import com.fridgerecipe.domain.model.RecipeDetail
import com.fridgerecipe.domain.model.RecipeIngredient
import com.fridgerecipe.domain.model.RecipeRecommendation
import com.fridgerecipe.domain.repository.BookmarkRepository
import com.fridgerecipe.domain.repository.IngredientMasterRepository
import com.fridgerecipe.domain.repository.IngredientRepository
import com.fridgerecipe.domain.repository.RecipeRepository
import kotlin.math.min

class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val ingredientRepository: IngredientRepository,
    private val ingredientMasterRepository: IngredientMasterRepository
) {

    suspend fun getRecommendations(userId: Long): List<RecipeRecommendation> {
        val myIngredients = ingredientRepository.findByUserId(userId, 1, 200)
        val myIngredientIds = myIngredients.map { it.ingredientId }

        if (myIngredientIds.isEmpty()) return emptyList()

        val today = java.time.LocalDate.now().toString()
        val soonDate = java.time.LocalDate.now().plusDays(3).toString()
        val expiringIds = myIngredients
            .filter { it.expiryDate != null && it.expiryDate >= today && it.expiryDate <= soonDate }
            .map { it.ingredientId }
            .toSet()

        val recipesWithIngredients = recipeRepository.findRecipesWithIngredientIds(myIngredientIds)

        val maxViewCount = recipesWithIngredients.maxOfOrNull { it.first.viewCount } ?: 1L
        val maxRating = recipesWithIngredients.maxOfOrNull { it.first.avgRating } ?: 1.0

        data class ScoredRecipe(
            val recipe: Recipe,
            val ingredients: List<RecipeIngredient>,
            val score: Double,
            val matchRatio: Double,
            val matchedCount: Int,
            val totalRequired: Int,
            val missingIngredients: List<String>
        )

        val scored = recipesWithIngredients.mapNotNull { (recipe, ingredients) ->
            val essentialIngredients = ingredients.filter { it.isEssential }
            val totalRequired = essentialIngredients.size
            if (totalRequired == 0) return@mapNotNull null

            val matchedEssential = essentialIngredients.filter { it.ingredientId in myIngredientIds }
            val matchedCount = matchedEssential.size
            val matchRatio = matchedCount.toDouble() / totalRequired

            if (matchRatio < 0.30) return@mapNotNull null

            val missingIngredients = essentialIngredients
                .filter { it.ingredientId !in myIngredientIds }
                .map { it.ingredientName }

            val expiringBonus = if (ingredients.any { it.ingredientId in expiringIds }) 0.25 else 0.0

            val normalizedView = if (maxViewCount > 0) min(recipe.viewCount.toDouble() / maxViewCount, 1.0) else 0.0
            val normalizedRating = if (maxRating > 0) min(recipe.avgRating / maxRating, 1.0) else 0.0
            val popularityScore = (normalizedView * 0.5 + normalizedRating * 0.5)

            val missingPenalty = (totalRequired - matchedCount).toDouble() / totalRequired

            val score = (matchRatio * 0.40) +
                (expiringBonus * 0.25) +
                (popularityScore * 0.10) -
                (missingPenalty * 0.10)

            ScoredRecipe(recipe, ingredients, score, matchRatio, matchedCount, totalRequired, missingIngredients)
        }.sortedByDescending { it.score }

        return scored.map { s ->
            val matchLabel = when {
                s.matchRatio >= 0.80 -> "바로 요리 가능"
                s.matchRatio >= 0.50 -> "재료 조금 부족"
                else -> "도전해보세요"
            }
            RecipeRecommendation(
                recipe = s.recipe,
                matchRatio = s.matchRatio,
                matchedCount = s.matchedCount,
                totalRequired = s.totalRequired,
                missingIngredients = s.missingIngredients,
                matchLabel = matchLabel
            )
        }
    }

    suspend fun getDetail(id: Long, userId: Long?): RecipeDetail {
        val (recipe, ingredients) = recipeRepository.findWithIngredients(id)
            ?: throw IllegalArgumentException("RECIPE_NOT_FOUND")

        recipeRepository.incrementViewCount(id)

        val isBookmarked = if (userId != null) bookmarkRepository.isBookmarked(userId, id) else false

        val matchedIngredients: List<Long>?
        val matchRatio: Double?

        if (userId != null) {
            val myIngredients = ingredientRepository.findByUserId(userId, 1, 200)
            val myIngredientIds = myIngredients.map { it.ingredientId }.toSet()
            matchedIngredients = ingredients.filter { it.ingredientId in myIngredientIds }.map { it.ingredientId }
            val essentialTotal = ingredients.count { it.isEssential }
            matchRatio = if (essentialTotal > 0) {
                ingredients.count { it.isEssential && it.ingredientId in myIngredientIds }.toDouble() / essentialTotal
            } else null
        } else {
            matchedIngredients = null
            matchRatio = null
        }

        return RecipeDetail(
            recipe = recipe,
            ingredients = ingredients,
            isBookmarked = isBookmarked,
            matchedIngredients = matchedIngredients,
            matchRatio = matchRatio
        )
    }

    suspend fun search(query: String, page: Int, size: Int): Pair<List<Recipe>, Long> {
        return recipeRepository.search(query, page, size)
    }

    suspend fun addBookmark(userId: Long, recipeId: Long) {
        recipeRepository.findById(recipeId) ?: throw IllegalArgumentException("RECIPE_NOT_FOUND")
        bookmarkRepository.add(userId, recipeId)
    }

    suspend fun removeBookmark(userId: Long, recipeId: Long) {
        bookmarkRepository.remove(userId, recipeId)
    }

    suspend fun getBookmarks(userId: Long, page: Int, size: Int): Pair<List<Recipe>, Long> {
        val (recipeIds, total) = bookmarkRepository.findByUserId(userId, page, size)
        if (recipeIds.isEmpty()) return Pair(emptyList(), total)
        val recipes = recipeIds.mapNotNull { recipeRepository.findById(it) }
        return Pair(recipes, total)
    }
}
