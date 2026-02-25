package com.fridgerecipe.shared.domain.repository

import com.fridgerecipe.shared.data.remote.dto.CategoryResponse
import com.fridgerecipe.shared.data.remote.dto.IngredientRequest
import com.fridgerecipe.shared.data.remote.dto.IngredientSearchResult
import com.fridgerecipe.shared.domain.model.UserIngredient

interface IngredientRepository {
    suspend fun getMyIngredients(): Result<List<UserIngredient>>
    suspend fun addIngredient(request: IngredientRequest): Result<UserIngredient>
    suspend fun addIngredientsBatch(requests: List<IngredientRequest>): Result<List<UserIngredient>>
    suspend fun updateIngredient(id: Long, request: IngredientRequest): Result<UserIngredient>
    suspend fun deleteIngredient(id: Long): Result<Unit>
    suspend fun deleteIngredientsBatch(ids: List<Long>): Result<Unit>
    suspend fun searchIngredients(query: String): Result<List<IngredientSearchResult>>
    suspend fun getCategories(): Result<List<CategoryResponse>>
    suspend fun getExpiringIngredients(): Result<List<UserIngredient>>
}
