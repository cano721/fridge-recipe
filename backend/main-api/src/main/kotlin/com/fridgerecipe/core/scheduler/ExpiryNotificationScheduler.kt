package com.fridgerecipe.core.scheduler

import com.fridgerecipe.domain.repository.DeviceTokenRepository
import com.fridgerecipe.domain.repository.IngredientRepository
import com.fridgerecipe.domain.repository.NotificationRepository
import io.ktor.server.application.*
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.time.Duration

class ExpiryNotificationScheduler(
    private val ingredientRepository: IngredientRepository,
    private val notificationRepository: NotificationRepository,
    private val deviceTokenRepository: DeviceTokenRepository
) {
    private val logger = LoggerFactory.getLogger(ExpiryNotificationScheduler::class.java)
    private var job: Job? = null

    fun start(scope: CoroutineScope) {
        job = scope.launch {
            while (isActive) {
                try {
                    checkAndNotify()
                } catch (e: Exception) {
                    logger.error("ExpiryNotificationScheduler error: ${e.message}", e)
                }
                delay(Duration.ofHours(1).toMillis())
            }
        }
        logger.info("ExpiryNotificationScheduler started")
    }

    private suspend fun checkAndNotify() {
        val expiryDays = listOf(3, 1, 0)
        val userIds = deviceTokenRepository.findUserIdsWithExpiryNotification(expiryDays)

        for (userId in userIds) {
            val setting = notificationRepository.findByUserId(userId) ?: continue
            if (!setting.expiryEnabled) continue

            val tokens = deviceTokenRepository.findActiveByUserId(userId)
            if (tokens.isEmpty()) continue

            val expiring = setting.expiryDays.flatMap { days ->
                ingredientRepository.findExpiring(userId, days)
                    .filter { !it.expiredNotified }
            }.distinctBy { it.id }

            if (expiring.isEmpty()) continue

            // stub: 실제 FCM/APNs 발송은 추후 구현
            logger.info(
                "Notify userId=$userId, ingredients=${expiring.map { it.ingredientId }}, " +
                "tokens=${tokens.map { it.platform }}"
            )
        }
    }

    fun stop() {
        job?.cancel()
        logger.info("ExpiryNotificationScheduler stopped")
    }
}
