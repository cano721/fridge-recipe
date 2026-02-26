package com.fridgerecipe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Long,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val thumbnailUrl: String?,
    val servings: Int,
    val cookingTime: Int,
    val difficulty: String,
    val cuisineType: String?,
    val sourceUrl: String?,
    val sourceType: String,
    val steps: List<CookingStep>?,
    val nutrition: NutritionInfo?,
    val tags: List<String>,
    val viewCount: Long,
    val avgRating: Double,
    val reviewCount: Int
)

@Serializable
data class CookingStep(
    val order: Int,
    val description: String,
    val imageUrl: String? = null,
    val timer: Int? = null
)

@Serializable
data class NutritionInfo(
    val calories: Int? = null,
    val protein: Double? = null,
    val fat: Double? = null,
    val carbs: Double? = null,
    val sodium: Double? = null
)

@Serializable
data class RecipeIngredient(
    val ingredientId: Long,
    val ingredientName: String,
    val quantity: Double?,
    val unit: String?,
    val isEssential: Boolean,
    val substituteIds: List<Long>
)

@Serializable
data class RecipeDetail(
    val recipe: Recipe,
    val ingredients: List<RecipeIngredient>,
    val isBookmarked: Boolean,
    val matchedIngredients: List<Long>? = null,
    val matchRatio: Double? = null
)

@Serializable
data class RecipeRecommendation(
    val recipe: Recipe,
    val matchRatio: Double,
    val matchedCount: Int,
    val totalRequired: Int,
    val missingIngredients: List<String>,
    val matchLabel: String
)
