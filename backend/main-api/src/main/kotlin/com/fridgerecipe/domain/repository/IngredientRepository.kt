package com.fridgerecipe.domain.repository

import com.fridgerecipe.domain.model.UserIngredient

interface IngredientRepository {
    suspend fun findByUserId(userId: Long, page: Int, size: Int): List<UserIngredient>
    suspend fun countByUserId(userId: Long): Long
    suspend fun findById(id: Long): UserIngredient?
    suspend fun findByUserAndIngredient(userId: Long, ingredientId: Long, storageType: String, expiryDate: String?): UserIngredient?
    suspend fun create(ingredient: UserIngredient): UserIngredient
    suspend fun update(ingredient: UserIngredient): UserIngredient?
    suspend fun delete(id: Long): Boolean
    suspend fun deleteBatch(ids: List<Long>, userId: Long): Int
    suspend fun findExpiring(userId: Long, daysAhead: Int = 3): List<UserIngredient>
}
