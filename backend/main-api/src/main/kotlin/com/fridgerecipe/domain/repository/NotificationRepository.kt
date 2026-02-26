package com.fridgerecipe.domain.repository

import com.fridgerecipe.domain.model.NotificationSetting

interface NotificationRepository {
    suspend fun findByUserId(userId: Long): NotificationSetting?
    suspend fun upsert(setting: NotificationSetting)
}
