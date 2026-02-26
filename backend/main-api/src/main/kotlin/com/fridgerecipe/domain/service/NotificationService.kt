package com.fridgerecipe.domain.service

import com.fridgerecipe.domain.model.DeviceToken
import com.fridgerecipe.domain.model.NotificationSetting
import com.fridgerecipe.domain.repository.DeviceTokenRepository
import com.fridgerecipe.domain.repository.NotificationRepository

class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val deviceTokenRepository: DeviceTokenRepository
) {
    suspend fun getSettings(userId: Long): NotificationSetting =
        notificationRepository.findByUserId(userId) ?: NotificationSetting(userId = userId)

    suspend fun updateSettings(userId: Long, setting: NotificationSetting) {
        notificationRepository.upsert(setting.copy(userId = userId))
    }

    suspend fun registerDeviceToken(userId: Long, token: String, platform: String): Long =
        deviceTokenRepository.register(userId, token, platform)

    suspend fun deactivateDeviceToken(userId: Long, token: String) =
        deviceTokenRepository.deactivate(userId, token)

    suspend fun getActiveDeviceTokens(userId: Long): List<DeviceToken> =
        deviceTokenRepository.findActiveByUserId(userId)
}
