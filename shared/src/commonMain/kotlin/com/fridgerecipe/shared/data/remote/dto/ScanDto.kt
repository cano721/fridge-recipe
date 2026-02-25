package com.fridgerecipe.shared.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ScanRequest(
    val imageBase64: String,
)

@Serializable
data class ScanStatusResponse(
    val scanId: Long,
    val status: String,
    val result: ScanResultDto? = null,
)

@Serializable
data class ScanResultDto(
    val items: List<ScanItemDto>,
)

@Serializable
data class ScanItemDto(
    val rawText: String,
    val matchedIngredientId: Long? = null,
    val matchedIngredientName: String? = null,
    val quantity: String? = null,
    val confidence: String,
    val isFood: Boolean = true,
)
