package com.fridgerecipe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationSetting(
    val userId: Long,
    val expiryEnabled: Boolean = true,
    val expiryDays: List<Int> = listOf(3, 1, 0),
    val themePreference: String = "system"
)

@Serializable
data class DeviceToken(
    val id: Long,
    val userId: Long,
    val token: String,
    val platform: String,
    val isActive: Boolean = true
)
