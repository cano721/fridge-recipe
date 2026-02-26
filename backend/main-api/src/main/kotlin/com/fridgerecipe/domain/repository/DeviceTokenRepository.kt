package com.fridgerecipe.domain.repository

import com.fridgerecipe.domain.model.DeviceToken

interface DeviceTokenRepository {
    suspend fun register(userId: Long, token: String, platform: String): Long
    suspend fun deactivate(userId: Long, token: String)
    suspend fun findActiveByUserId(userId: Long): List<DeviceToken>
    suspend fun findUserIdsWithExpiryNotification(expiryDays: List<Int>): List<Long>
}
