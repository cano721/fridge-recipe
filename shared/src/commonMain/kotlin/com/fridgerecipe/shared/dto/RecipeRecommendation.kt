package com.fridgerecipe.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecipeRecommendation(
    val recipeId: Long,
    val title: String,
    val thumbnailUrl: String? = null,
    val matchRatio: Double,
    val matchedCount: Int,
    val totalRequired: Int,
    val missingIngredients: List<String>,
    val cookingTime: Int? = null,
    val difficulty: String? = null,
    val calories: Int? = null
)
