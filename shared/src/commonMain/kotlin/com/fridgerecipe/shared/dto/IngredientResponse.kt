package com.fridgerecipe.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class IngredientResponse(
    val id: Long,
    val ingredientId: Long,
    val name: String,
    val category: String? = null,
    val quantity: Double? = null,
    val unit: String? = null,
    val expiryDate: String? = null,
    val storageType: String = "fridge",
    val memo: String? = null,
    val registeredVia: String = "manual",
    val daysUntilExpiry: Int? = null,
    val iconUrl: String? = null
)

@Serializable
data class IngredientListResponse(
    val items: List<IngredientResponse>,
    val total: Int
)

@Serializable
data class CategoryResponse(
    val name: String,
    val count: Int
)

@Serializable
data class ExpiringItemResponse(
    val id: Long,
    val name: String,
    val expiryDate: String,
    val daysUntilExpiry: Int,
    val storageType: String,
    val quantity: Double? = null,
    val unit: String? = null
)
