package com.fridgerecipe.domain.repository

import com.fridgerecipe.domain.model.IngredientMasterItem

interface IngredientMasterRepository {
    suspend fun findById(id: Long): IngredientMasterItem?
    suspend fun searchByName(query: String, limit: Int = 10): List<IngredientMasterItem>
    suspend fun findCategories(): List<String>
}
