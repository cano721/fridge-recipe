package com.fridgerecipe.shared.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecipeSearchParams(
    val query: String? = null,
    val cuisineType: String? = null,
    val difficulty: String? = null,
    val maxCookingTime: Int? = null,
    val page: Int = 1,
    val size: Int = 20,
)

@Serializable
data class RecommendParams(
    val sortBy: String = "match_ratio",
    val minMatchRatio: Double = 0.5,
    val page: Int = 1,
    val size: Int = 20,
)
