package com.fridgerecipe.shared.domain.repository

import com.fridgerecipe.shared.domain.model.Recipe
import com.fridgerecipe.shared.domain.model.RecipeIngredient
import com.fridgerecipe.shared.domain.model.RecipeRecommendation

interface RecipeRepository {
    suspend fun getRecommendations(
        sortBy: String = "match_ratio",
        minMatchRatio: Double = 0.5,
        page: Int = 1,
        size: Int = 20,
    ): Result<List<RecipeRecommendation>>

    suspend fun getRecipeDetail(id: Long): Result<Recipe>
    suspend fun getRecipeIngredients(recipeId: Long): Result<List<RecipeIngredient>>
    suspend fun searchRecipes(query: String, page: Int = 1, size: Int = 20): Result<List<Recipe>>
    suspend fun addBookmark(recipeId: Long): Result<Unit>
    suspend fun removeBookmark(recipeId: Long): Result<Unit>
    suspend fun getBookmarks(page: Int = 1, size: Int = 20): Result<List<Recipe>>
}
