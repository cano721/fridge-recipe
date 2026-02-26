package com.fridgerecipe.domain.repository

import com.fridgerecipe.domain.model.ScanItem
import com.fridgerecipe.domain.model.ScanResult

interface ScanRepository {
    suspend fun create(userId: Long, scanType: String, imageUrl: String?): Long
    suspend fun findById(id: Long): ScanResult?
    suspend fun findByUserId(userId: Long, limit: Int = 20): List<ScanResult>
    suspend fun updateStatus(id: Long, status: String, items: List<ScanItem>?, errorMessage: String?)
}
