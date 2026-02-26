package com.fridgerecipe.domain.model

data class RefreshTokenInfo(
    val id: Long = 0,
    val userId: Long,
    val tokenHash: String,
    val deviceInfo: String? = null,
    val expiresAt: String,
    val createdAt: String? = null,
    val revokedAt: String? = null
)
