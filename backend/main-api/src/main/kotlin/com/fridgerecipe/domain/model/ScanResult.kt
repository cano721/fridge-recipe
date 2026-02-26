package com.fridgerecipe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ScanResult(
    val id: Long,
    val userId: Long,
    val scanType: String,
    val imageUrl: String?,
    val status: String,
    val items: List<ScanItem>?,
    val errorMessage: String?,
    val createdAt: String
)

@Serializable
data class ScanItem(
    val name: String,
    val quantity: Double?,
    val unit: String?,
    val confidence: Double
)
