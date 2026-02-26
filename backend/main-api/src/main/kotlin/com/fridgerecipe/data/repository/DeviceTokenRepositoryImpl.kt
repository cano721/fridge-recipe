package com.fridgerecipe.data.repository

import com.fridgerecipe.data.database.tables.DeviceTokens
import com.fridgerecipe.data.database.tables.NotificationSettings
import com.fridgerecipe.data.database.tables.UserIngredients
import com.fridgerecipe.domain.model.DeviceToken
import com.fridgerecipe.domain.repository.DeviceTokenRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDate

class DeviceTokenRepositoryImpl : DeviceTokenRepository {

    override suspend fun register(userId: Long, token: String, platform: String): Long = newSuspendedTransaction {
        val existing = DeviceTokens.selectAll()
            .where { DeviceTokens.token eq token }
            .singleOrNull()

        if (existing != null) {
            DeviceTokens.update({ DeviceTokens.token eq token }) {
                it[DeviceTokens.userId] = userId
                it[deviceType] = platform
                it[isActive] = true
            }
            existing[DeviceTokens.id].value
        } else {
            DeviceTokens.insertAndGetId {
                it[DeviceTokens.userId] = userId
                it[DeviceTokens.token] = token
                it[deviceType] = platform
                it[isActive] = true
            }.value
        }
    }

    override suspend fun deactivate(userId: Long, token: String): Unit = newSuspendedTransaction {
        DeviceTokens.update({
            (DeviceTokens.userId eq userId) and (DeviceTokens.token eq token)
        }) {
            it[isActive] = false
        }
    }

    override suspend fun findActiveByUserId(userId: Long): List<DeviceToken> = newSuspendedTransaction {
        DeviceTokens.selectAll()
            .where { (DeviceTokens.userId eq userId) and (DeviceTokens.isActive eq true) }
            .map {
                DeviceToken(
                    id = it[DeviceTokens.id].value,
                    userId = it[DeviceTokens.userId],
                    token = it[DeviceTokens.token],
                    platform = it[DeviceTokens.deviceType],
                    isActive = it[DeviceTokens.isActive]
                )
            }
    }

    override suspend fun findUserIdsWithExpiryNotification(expiryDays: List<Int>): List<Long> = newSuspendedTransaction {
        val today = LocalDate.now()
        val targetDates = expiryDays.map { days -> today.plusDays(days.toLong()).toString() }

        if (targetDates.isEmpty()) return@newSuspendedTransaction emptyList()

        UserIngredients
            .join(NotificationSettings, JoinType.INNER,
                additionalConstraint = {
                    UserIngredients.userId eq NotificationSettings.userId
                }
            )
            .join(DeviceTokens, JoinType.INNER,
                additionalConstraint = {
                    UserIngredients.userId eq DeviceTokens.userId
                }
            )
            .select(UserIngredients.userId)
            .where {
                (UserIngredients.expiryDate inList targetDates) and
                (UserIngredients.expiredNotified eq false) and
                (NotificationSettings.expiryEnabled eq true) and
                (DeviceTokens.isActive eq true)
            }
            .withDistinct()
            .map { it[UserIngredients.userId] }
    }
}
