package com.fridgerecipe.domain.service

import com.fridgerecipe.domain.model.IngredientMasterItem
import com.fridgerecipe.domain.model.UserIngredient
import com.fridgerecipe.domain.repository.IngredientMasterRepository
import com.fridgerecipe.domain.repository.IngredientRepository

class IngredientService(
    private val ingredientRepository: IngredientRepository,
    private val masterRepository: IngredientMasterRepository
) {
    companion object {
        const val MAX_INGREDIENTS_PER_USER = 200
    }

    suspend fun register(
        userId: Long,
        ingredientId: Long,
        quantity: java.math.BigDecimal?,
        unit: String?,
        expiryDate: String?,
        storageType: String,
        memo: String?,
        registeredVia: String = "manual"
    ): UserIngredient {
        masterRepository.findById(ingredientId) ?: throw IllegalArgumentException("INGREDIENT_MASTER_NOT_FOUND")

        val count = ingredientRepository.countByUserId(userId)
        if (count >= MAX_INGREDIENTS_PER_USER) throw IllegalArgumentException("INGREDIENT_LIMIT_EXCEEDED")

        val existing = ingredientRepository.findByUserAndIngredient(userId, ingredientId, storageType, expiryDate)
        if (existing != null) {
            if (quantity != null && existing.quantity != null) {
                val merged = existing.copy(quantity = existing.quantity.add(quantity))
                return ingredientRepository.update(merged) ?: throw IllegalStateException()
            }
            throw IllegalArgumentException("INGREDIENT_DUPLICATE")
        }

        return ingredientRepository.create(
            UserIngredient(
                userId = userId,
                ingredientId = ingredientId,
                quantity = quantity,
                unit = unit,
                expiryDate = expiryDate,
                storageType = storageType,
                memo = memo,
                registeredVia = registeredVia
            )
        )
    }

    suspend fun registerBatch(
        userId: Long,
        items: List<BatchItem>,
        conflictStrategy: String = "MERGE"
    ): BatchResult {
        val results = mutableListOf<UserIngredient>()
        val errors = mutableListOf<String>()

        items.forEachIndexed { index, item ->
            try {
                when (conflictStrategy) {
                    "MERGE" -> {
                        val result = register(userId, item.ingredientId, item.quantity, item.unit, item.expiryDate, item.storageType, item.memo, item.registeredVia)
                        results.add(result)
                    }
                    "SEPARATE" -> {
                        val ingredient = ingredientRepository.create(
                            UserIngredient(
                                userId = userId,
                                ingredientId = item.ingredientId,
                                quantity = item.quantity,
                                unit = item.unit,
                                expiryDate = item.expiryDate,
                                storageType = item.storageType,
                                memo = item.memo,
                                registeredVia = item.registeredVia
                            )
                        )
                        results.add(ingredient)
                    }
                    "SKIP" -> {
                        val existing = ingredientRepository.findByUserAndIngredient(userId, item.ingredientId, item.storageType, item.expiryDate)
                        if (existing == null) {
                            val ingredient = ingredientRepository.create(
                                UserIngredient(
                                    userId = userId,
                                    ingredientId = item.ingredientId,
                                    quantity = item.quantity,
                                    unit = item.unit,
                                    expiryDate = item.expiryDate,
                                    storageType = item.storageType,
                                    memo = item.memo,
                                    registeredVia = item.registeredVia
                                )
                            )
                            results.add(ingredient)
                        }
                    }
                }
            } catch (e: Exception) {
                errors.add("[$index] ${e.message}")
            }
        }

        return BatchResult(registered = results, errors = errors)
    }

    data class BatchItem(
        val ingredientId: Long,
        val quantity: java.math.BigDecimal? = null,
        val unit: String? = null,
        val expiryDate: String? = null,
        val storageType: String = "fridge",
        val memo: String? = null,
        val registeredVia: String = "manual"
    )

    data class BatchResult(
        val registered: List<UserIngredient>,
        val errors: List<String>
    )

    suspend fun getMyIngredients(userId: Long, page: Int = 1, size: Int = 20) =
        ingredientRepository.findByUserId(userId, page, size)

    suspend fun countMyIngredients(userId: Long) = ingredientRepository.countByUserId(userId)

    suspend fun update(
        userId: Long,
        id: Long,
        quantity: java.math.BigDecimal?,
        unit: String?,
        expiryDate: String?,
        storageType: String?,
        memo: String?
    ): UserIngredient {
        val existing = ingredientRepository.findById(id) ?: throw IllegalArgumentException("INGREDIENT_NOT_FOUND")
        if (existing.userId != userId) throw IllegalArgumentException("INGREDIENT_NOT_FOUND")

        val updated = existing.copy(
            quantity = quantity ?: existing.quantity,
            unit = unit ?: existing.unit,
            expiryDate = expiryDate ?: existing.expiryDate,
            storageType = storageType ?: existing.storageType,
            memo = memo ?: existing.memo
        )
        return ingredientRepository.update(updated) ?: throw IllegalStateException()
    }

    suspend fun delete(userId: Long, id: Long): Boolean {
        val existing = ingredientRepository.findById(id) ?: throw IllegalArgumentException("INGREDIENT_NOT_FOUND")
        if (existing.userId != userId) throw IllegalArgumentException("INGREDIENT_NOT_FOUND")
        return ingredientRepository.delete(id)
    }

    suspend fun deleteBatch(userId: Long, ids: List<Long>) = ingredientRepository.deleteBatch(ids, userId)

    suspend fun search(query: String, limit: Int = 10) = masterRepository.searchByName(query, limit)

    suspend fun getCategories() = masterRepository.findCategories()

    suspend fun getExpiring(userId: Long, daysAhead: Int = 3) = ingredientRepository.findExpiring(userId, daysAhead)
}
