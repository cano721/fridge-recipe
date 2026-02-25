package com.fridgerecipe.shared.core.util

import com.fridgerecipe.shared.domain.model.ExpiryStatus
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn

object ExpiryHelper {
    fun getStatus(expiryDate: LocalDate?): ExpiryStatus {
        if (expiryDate == null) return ExpiryStatus.SAFE
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val daysLeft = today.daysUntil(expiryDate)
        return when {
            daysLeft < 0 -> ExpiryStatus.EXPIRED
            daysLeft <= 1 -> ExpiryStatus.URGENT
            daysLeft <= 3 -> ExpiryStatus.SOON
            else -> ExpiryStatus.SAFE
        }
    }

    fun getDaysLeft(expiryDate: LocalDate?): Int? {
        if (expiryDate == null) return null
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        return today.daysUntil(expiryDate)
    }

    fun formatDaysLeft(expiryDate: LocalDate?): String {
        val days = getDaysLeft(expiryDate) ?: return "기한 없음"
        return when {
            days < 0 -> "만료 ${-days}일 경과"
            days == 0 -> "오늘 만료"
            else -> "D-$days"
        }
    }
}
