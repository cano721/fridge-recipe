package com.fridgerecipe.data.repository

import com.fridgerecipe.data.database.tables.NotificationSettings
import com.fridgerecipe.domain.model.NotificationSetting
import com.fridgerecipe.domain.repository.NotificationRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.upsert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class NotificationRepositoryImpl : NotificationRepository {

    override suspend fun findByUserId(userId: Long): NotificationSetting? = newSuspendedTransaction {
        NotificationSettings.selectAll()
            .where { NotificationSettings.userId eq userId }
            .singleOrNull()
            ?.let {
                NotificationSetting(
                    userId = it[NotificationSettings.userId],
                    expiryEnabled = it[NotificationSettings.expiryEnabled],
                    expiryDays = it[NotificationSettings.expiryDays],
                    themePreference = it[NotificationSettings.themePreference]
                )
            }
    }

    override suspend fun upsert(setting: NotificationSetting): Unit = newSuspendedTransaction {
        NotificationSettings.upsert(NotificationSettings.userId) {
            it[userId] = setting.userId
            it[expiryEnabled] = setting.expiryEnabled
            it[expiryDays] = setting.expiryDays
            it[themePreference] = setting.themePreference
        }
    }
}
