package com.fridgerecipe.shared.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ScanHistory(
    val id: Long,
    val userId: Long,
    val scanType: ScanType,
    val imageUrl: String? = null,
    val status: ScanStatus = ScanStatus.PROCESSING,
    val result: ScanResult? = null,
    val createdAt: Instant? = null,
)

@Serializable
enum class ScanType {
    RECEIPT, PHOTO
}

@Serializable
enum class ScanStatus {
    PROCESSING, DONE, FAILED
}

@Serializable
data class ScanResult(
    val items: List<ScannedItem> = emptyList(),
)

@Serializable
data class ScannedItem(
    val rawText: String,
    val matchedIngredientId: Long? = null,
    val matchedIngredientName: String? = null,
    val quantity: String? = null,
    val confidence: ConfidenceLevel = ConfidenceLevel.LOW,
    val isFood: Boolean = true,
)

@Serializable
enum class ConfidenceLevel {
    HIGH, MEDIUM, LOW
}
