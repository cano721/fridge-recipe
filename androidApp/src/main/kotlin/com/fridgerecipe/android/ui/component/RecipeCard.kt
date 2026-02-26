package com.fridgerecipe.android.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fridgerecipe.android.ui.theme.FridgeRecipeColors
import com.fridgerecipe.android.ui.theme.RecipeCardShape

/**
 * 레시피 카드 (세로형)
 * Border Radius: 20dp, Elevation: Level 1
 * 구조: 이미지(3:2) + 레시피명 + 메타정보 + 매칭률 진행바
 */
@Composable
fun RecipeCard(
    title: String,
    cookTimeMinutes: Int,
    rating: Float,
    matchPercent: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    servings: Int? = null,
) {
    val metaText = buildString {
        append("${cookTimeMinutes}분")
        if (rating > 0) append(" · ★${String.format("%.1f", rating)}")
        if (servings != null) append(" · ${servings}인분")
    }

    Card(
        onClick = onClick,
        modifier = modifier
            .semantics(mergeDescendants = true) {
                contentDescription = "$title, ${cookTimeMinutes}분, ${servings ?: ""}인분, 별점 ${rating}점"
            },
        shape = RecipeCardShape, // 20dp
        colors = CardDefaults.cardColors(
            containerColor = FridgeRecipeColors.Surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column {
            // 썸네일 플레이스홀더 (3:2 비율)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 2f)
                    .clip(RecipeCardShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "🍳",
                    style = MaterialTheme.typography.headlineLarge,
                )
            }

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                // 레시피명
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                // 메타 정보
                Text(
                    text = metaText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                // 매칭률 진행바
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    LinearProgressIndicator(
                        progress = { matchPercent / 100f },
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(MaterialTheme.shapes.small),
                        color = FridgeRecipeColors.Primary500,
                        trackColor = FridgeRecipeColors.Primary100,
                    )
                    Text(
                        text = "${matchPercent}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = FridgeRecipeColors.Primary600,
                    )
                }
            }
        }
    }
}
