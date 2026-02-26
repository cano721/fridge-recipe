package com.fridgerecipe.domain.repository

import com.fridgerecipe.domain.model.Recipe
import com.fridgerecipe.domain.model.RecipeIngredient

interface RecipeRepository {
    suspend fun findById(id: Long): Recipe?
    suspend fun findAll(page: Int, size: Int): Pair<List<Recipe>, Long>
    suspend fun search(query: String, page: Int, size: Int): Pair<List<Recipe>, Long>
    suspend fun findWithIngredients(id: Long): Pair<Recipe, List<RecipeIngredient>>?
    suspend fun findRecipesWithIngredientIds(ingredientIds: List<Long>): List<Pair<Recipe, List<RecipeIngredient>>>
    suspend fun incrementViewCount(id: Long)
}
