package com.fridgerecipe.android.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fridgerecipe.shared.dto.IngredientResponse
import com.fridgerecipe.shared.model.ExpiryStatus

private fun expiryStatusOf(daysUntilExpiry: Int?): ExpiryStatus {
    if (daysUntilExpiry == null) return ExpiryStatus.SAFE
    return when {
        daysUntilExpiry < 0 -> ExpiryStatus.EXPIRED
        daysUntilExpiry <= 1 -> ExpiryStatus.URGENT
        daysUntilExpiry <= 3 -> ExpiryStatus.SOON
        else -> ExpiryStatus.SAFE
    }
}

@Composable
fun IngredientCard(
    ingredient: IngredientResponse,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val expiryStatus = expiryStatusOf(ingredient.daysUntilExpiry)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = onClick ?: {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 재료 아이콘 48dp
            Icon(
                imageVector = Icons.Default.LocalDining,
                contentDescription = ingredient.name,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))

            // 재료명 + 수량
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
                if (ingredient.quantity != null) {
                    val quantityText = buildString {
                        append(
                            if (ingredient.quantity % 1.0 == 0.0)
                                ingredient.quantity.toInt().toString()
                            else
                                ingredient.quantity.toString()
                        )
                        if (ingredient.unit != null) append(" ${ingredient.unit}")
                    }
                    Text(
                        text = quantityText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            }

            // 소비기한 배지
            ExpiryBadge(
                status = expiryStatus,
                daysUntilExpiry = ingredient.daysUntilExpiry
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 화살표
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
