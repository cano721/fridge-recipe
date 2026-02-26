package com.fridgerecipe.shared.dto

import kotlinx.serialization.Serializable

@Serializable
enum class ConflictStrategy {
    MERGE,
    SEPARATE,
    SKIP
}

@Serializable
data class BatchIngredientRequest(
    val ingredients: List<IngredientRequest>,
    val conflictStrategy: ConflictStrategy = ConflictStrategy.MERGE
) {
    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (ingredients.isEmpty()) errors.add("재료 목록이 비어있습니다")
        if (ingredients.size > 50) errors.add("한 번에 최대 50개까지 등록할 수 있습니다")
        ingredients.forEachIndexed { index, req ->
            req.validate().forEach { error -> errors.add("[$index] $error") }
        }
        return errors
    }
}
