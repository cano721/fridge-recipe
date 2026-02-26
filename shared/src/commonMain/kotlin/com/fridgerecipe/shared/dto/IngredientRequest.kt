package com.fridgerecipe.shared.dto

import com.fridgerecipe.shared.model.StorageType
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class IngredientRequest(
    val ingredientId: Long,
    val quantity: Double? = null,
    val unit: String? = null,
    val expiryDate: LocalDate? = null,
    val storageType: StorageType = StorageType.FRIDGE,
    val memo: String? = null
) {
    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (quantity != null && quantity <= 0) errors.add("수량은 0보다 커야 합니다")
        if (memo != null && memo.length > 200) errors.add("메모는 200자 이내여야 합니다")
        return errors
    }
}
