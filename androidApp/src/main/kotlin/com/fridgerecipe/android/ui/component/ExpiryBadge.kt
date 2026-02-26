package com.fridgerecipe.android.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.fridgerecipe.android.ui.theme.FridgeRecipeColors

/**
 * 유통기한 상태
 * - Safe: D-4 이상
 * - Soon: D-3 ~ D-2
 * - Urgent: D-1 ~ 당일
 * - Expired: 만료됨
 */
enum class ExpiryStatus {
    SAFE, SOON, URGENT, EXPIRED;

    companion object {
        fun fromDaysLeft(daysLeft: Int): ExpiryStatus = when {
            daysLeft < 0 -> EXPIRED
            daysLeft <= 1 -> URGENT
            daysLeft <= 3 -> SOON
            else -> SAFE
        }
    }
}

/**
 * 유통기한 배지
 * 높이: 24dp, 좌우 패딩: 8dp, Border Radius: 6dp
 * WCAG 1.4.1 준수: 색상 외에 아이콘 심볼로도 상태 구분
 */
@Composable
fun ExpiryBadge(
    daysLeft: Int,
    modifier: Modifier = Modifier,
) {
    val status = ExpiryStatus.fromDaysLeft(daysLeft)

    val config = when (status) {
        ExpiryStatus.SAFE -> ExpiryBadgeConfig(
            backgroundColor = FridgeRecipeColors.ExpirySafe.copy(alpha = 0.15f),
            textColor = Color(0xFF2E7D32),
            icon = Icons.Outlined.CheckCircle,
            label = "D-$daysLeft",
        )
        ExpiryStatus.SOON -> ExpiryBadgeConfig(
            backgroundColor = FridgeRecipeColors.ExpirySoon.copy(alpha = 0.15f),
            textColor = Color(0xFFE65100),
            icon = Icons.Outlined.HourglassTop,
            label = "D-$daysLeft",
        )
        ExpiryStatus.URGENT -> ExpiryBadgeConfig(
            backgroundColor = FridgeRecipeColors.ExpiryUrgent.copy(alpha = 0.15f),
            textColor = Color(0xFFC62828),
            icon = Icons.Outlined.Warning,
            label = if (daysLeft == 0) "D-Day" else "D-$daysLeft",
            bold = true,
        )
        ExpiryStatus.EXPIRED -> ExpiryBadgeConfig(
            backgroundColor = Color(0xFFF0F0F0),
            textColor = Color(0xFF757575),
            icon = Icons.Outlined.Cancel,
            label = "만료",
            strikethrough = true,
        )
    }

    Row(
        modifier = modifier
            .height(24.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(config.backgroundColor)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = config.icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = config.textColor,
        )
        Text(
            text = config.label,
            style = MaterialTheme.typography.labelSmall,
            color = config.textColor,
            fontWeight = if (config.bold) FontWeight.Bold else null,
            textDecoration = if (config.strikethrough) TextDecoration.LineThrough else null,
        )
    }
}

private data class ExpiryBadgeConfig(
    val backgroundColor: Color,
    val textColor: Color,
    val icon: ImageVector,
    val label: String,
    val bold: Boolean = false,
    val strikethrough: Boolean = false,
)
