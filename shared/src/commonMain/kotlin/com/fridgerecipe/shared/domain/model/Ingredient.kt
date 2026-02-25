package com.fridgerecipe.shared.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class IngredientMaster(
    val id: Long,
    val name: String,
    val category: IngredientCategory,
    val iconUrl: String? = null,
    val defaultUnit: String? = null,
    val defaultExpiryDays: Int? = null,
    val aliases: List<String> = emptyList(),
)

@Serializable
data class UserIngredient(
    val id: Long,
    val userId: Long,
    val ingredientId: Long,
    val ingredientName: String,
    val category: IngredientCategory,
    val iconUrl: String? = null,
    val quantity: Double? = null,
    val unit: String? = null,
    val expiryDate: LocalDate? = null,
    val storageType: StorageType = StorageType.FRIDGE,
    val memo: String? = null,
    val registeredVia: RegisteredVia = RegisteredVia.MANUAL,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)

@Serializable
enum class StorageType {
    FRIDGE, FREEZER, ROOM
}

@Serializable
enum class RegisteredVia {
    MANUAL, RECEIPT, PHOTO
}

@Serializable
enum class IngredientCategory {
    VEGETABLE, FRUIT, MEAT, SEAFOOD, DAIRY, EGG,
    FROZEN, SEASONING, GRAIN, NOODLE, CANNED,
    BEVERAGE, SNACK, OTHER
}

@Serializable
enum class ExpiryStatus {
    SAFE, SOON, URGENT, EXPIRED
}
