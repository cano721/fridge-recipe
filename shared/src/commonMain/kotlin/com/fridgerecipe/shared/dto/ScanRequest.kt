package com.fridgerecipe.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class ScanRequest(
    val imageUrl: String
)

@Serializable
data class ScanResponse(
    val scanId: Long,
    val status: String // "processing", "done", "failed"
)

@Serializable
data class ScanResultResponse(
    val scanId: Long,
    val scanType: String,
    val status: String,
    val items: List<ScanItemDto>? = null,
    val errorMessage: String? = null
)

@Serializable
data class ScanItemDto(
    val name: String,
    val quantity: Double? = null,
    val unit: String? = null,
    val confidence: Double = 0.0
)
