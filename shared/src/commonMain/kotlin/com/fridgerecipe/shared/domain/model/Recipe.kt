package com.fridgerecipe.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Long,
    val title: String,
    val description: String? = null,
    val cuisineType: String? = null,
    val difficulty: Difficulty? = null,
    val cookingTime: Int? = null,
    val servings: Int = 2,
    val calories: Int? = null,
    val thumbnailUrl: String? = null,
    val steps: List<RecipeStep> = emptyList(),
    val nutrition: Nutrition? = null,
    val tags: List<String> = emptyList(),
    val avgRating: Double = 0.0,
    val viewCount: Int = 0,
    val sourceUrl: String? = null,
    val sourceType: SourceType = SourceType.MANUAL,
)

@Serializable
data class RecipeStep(
    val step: Int,
    val text: String,
    val imageUrl: String? = null,
)

@Serializable
data class Nutrition(
    val carbs: Int? = null,
    val protein: Int? = null,
    val fat: Int? = null,
)

@Serializable
enum class Difficulty {
    EASY, MEDIUM, HARD
}

@Serializable
enum class SourceType {
    CRAWLED, MANUAL, API
}

@Serializable
data class RecipeIngredient(
    val id: Long,
    val recipeId: Long,
    val ingredientId: Long,
    val ingredientName: String,
    val quantity: String? = null,
    val isEssential: Boolean = true,
    val substituteIds: List<Long> = emptyList(),
)

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
    val calories: Int? = null,
)
