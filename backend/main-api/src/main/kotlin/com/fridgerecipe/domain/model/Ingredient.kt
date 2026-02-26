package com.fridgerecipe.domain.model

data class UserIngredient(
    val id: Long = 0,
    val userId: Long,
    val ingredientId: Long,
    val ingredientName: String? = null,
    val category: String? = null,
    val iconUrl: String? = null,
    val quantity: java.math.BigDecimal? = null,
    val unit: String? = null,
    val expiryDate: String? = null,
    val storageType: String = "fridge",
    val memo: String? = null,
    val registeredVia: String = "manual",
    val expiredNotified: Boolean = false,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class IngredientMasterItem(
    val id: Long = 0,
    val name: String,
    val category: String,
    val iconUrl: String? = null,
    val defaultUnit: String? = null,
    val defaultExpiryDays: Int? = null,
    val aliases: List<String> = emptyList()
)
