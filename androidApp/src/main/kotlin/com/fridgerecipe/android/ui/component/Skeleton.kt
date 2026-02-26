package com.fridgerecipe.android.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Shimmer 효과 Brush
 * 색상 (Light): #E8E8E4 → #F4F4F0
 * 색상 (Dark):  #2A2A28 → #343432
 * 방향: 135도 대각선, 1.5초 반복
 */
@Composable
fun shimmerBrush(): Brush {
    val isDark = isSystemInDarkTheme()
    val baseColor = if (isDark) Color(0xFF2A2A28) else Color(0xFFE8E8E4)
    val highlightColor = if (isDark) Color(0xFF343432) else Color(0xFFF4F4F0)

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer_translate",
    )

    return Brush.linearGradient(
        colors = listOf(baseColor, highlightColor, baseColor),
        start = Offset(translateAnim - 200f, translateAnim - 200f), // 135도 방향
        end = Offset(translateAnim, translateAnim),
    )
}

/**
 * 스켈레톤 Box
 * Border Radius: 실제 컴포넌트와 동일하게 지정
 */
@Composable
fun SkeletonBox(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp,
) {
    Box(
        modifier = modifier
            .size(width, height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(shimmerBrush()),
    )
}

/**
 * 식재료 카드 스켈레톤
 * 높이: 72dp, Border Radius: 16dp
 */
@Composable
fun IngredientCardSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        SkeletonBox(width = 48.dp, height = 48.dp, cornerRadius = 12.dp)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            SkeletonBox(width = 80.dp, height = 14.dp, cornerRadius = 4.dp)
            Spacer(modifier = Modifier.height(6.dp))
            SkeletonBox(width = 48.dp, height = 12.dp, cornerRadius = 4.dp)
        }
        Spacer(modifier = Modifier.weight(1f))
        Column {
            Spacer(modifier = Modifier.height(24.dp))
            SkeletonBox(width = 48.dp, height = 24.dp, cornerRadius = 6.dp)
        }
        Spacer(modifier = Modifier.width(12.dp))
    }
}

/**
 * 레시피 카드 스켈레톤
 * Border Radius: 20dp
 */
@Composable
fun RecipeCardSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        // 썸네일 플레이스홀더
        SkeletonBox(
            width = 200.dp,
            height = 133.dp, // 3:2 비율 근사
            cornerRadius = 20.dp,
            modifier = Modifier.fillMaxWidth(),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            SkeletonBox(
                width = 120.dp,
                height = 14.dp,
                cornerRadius = 4.dp,
                modifier = Modifier.fillMaxWidth(0.8f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            SkeletonBox(
                width = 80.dp,
                height = 12.dp,
                cornerRadius = 4.dp,
                modifier = Modifier.fillMaxWidth(0.5f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            SkeletonBox(
                width = 150.dp,
                height = 6.dp,
                cornerRadius = 3.dp,
                modifier = Modifier.fillMaxWidth(0.9f),
            )
        }
    }
}
