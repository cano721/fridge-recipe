package com.fridgerecipe.android.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fridgerecipe.shared.model.ExpiryStatus

private val SafeColor = Color(0xFF4CAF50)
private val SoonColor = Color(0xFFFF9800)
private val UrgentColor = Color(0xFFF44336)
private val ExpiredColor = Color(0xFF757575)

private data class BadgeStyle(
    val background: Color,
    val textColor: Color,
    val icon: String,
    val bold: Boolean = false,
    val strikethrough: Boolean = false
)

private fun badgeStyleFor(status: ExpiryStatus): BadgeStyle = when (status) {
    ExpiryStatus.SAFE -> BadgeStyle(
        background = SafeColor.copy(alpha = 0.15f),
        textColor = Color(0xFF1B5E20),
        icon = "✓"
    )
    ExpiryStatus.SOON -> BadgeStyle(
        background = SoonColor.copy(alpha = 0.15f),
        textColor = Color(0xFFE65100),
        icon = "⏳"
    )
    ExpiryStatus.URGENT -> BadgeStyle(
        background = UrgentColor.copy(alpha = 0.15f),
        textColor = Color(0xFFB71C1C),
        icon = "⚠",
        bold = true
    )
    ExpiryStatus.EXPIRED -> BadgeStyle(
        background = Color(0xFFF0F0F0),
        textColor = ExpiredColor,
        icon = "✕",
        strikethrough = true
    )
}

private fun labelFor(status: ExpiryStatus, daysUntilExpiry: Int?): String = when (status) {
    ExpiryStatus.SAFE -> if (daysUntilExpiry != null) "D-$daysUntilExpiry" else "안전"
    ExpiryStatus.SOON -> if (daysUntilExpiry != null) "D-$daysUntilExpiry" else "임박"
    ExpiryStatus.URGENT -> if (daysUntilExpiry != null && daysUntilExpiry == 0) "오늘 만료" else "D-${daysUntilExpiry ?: 1}"
    ExpiryStatus.EXPIRED -> "만료됨"
}

@Composable
fun ExpiryBadge(
    status: ExpiryStatus,
    daysUntilExpiry: Int? = null,
    modifier: Modifier = Modifier
) {
    val style = badgeStyleFor(status)
    val label = labelFor(status, daysUntilExpiry)
    val shape = RoundedCornerShape(6.dp)

    Row(
        modifier = modifier
            .height(24.dp)
            .background(color = style.background, shape = shape)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = style.icon,
            fontSize = 10.sp,
            color = style.textColor
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = label,
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = if (style.bold) FontWeight.Bold else FontWeight.Normal,
                color = style.textColor,
                textDecoration = if (style.strikethrough) TextDecoration.LineThrough else TextDecoration.None
            )
        )
    }
}
