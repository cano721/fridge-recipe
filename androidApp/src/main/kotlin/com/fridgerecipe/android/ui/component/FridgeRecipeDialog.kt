package com.fridgerecipe.android.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * Dialog
 * 너비: 280 ~ 560dp (화면 너비 - 48dp, 최대 560dp)
 * Border Radius: xxl (28dp)
 * Elevation: Level 4
 * 패딩: 24dp
 * 타이틀: headlineSmall
 * 본문: bodyMedium, 상단 16dp
 * 버튼 영역: 상단 24dp, 우측 정렬
 * Scrim: 40% 투명도 (Dialog 기본)
 */
@Composable
fun FridgeRecipeDialog(
    title: String,
    body: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissText: String? = "취소",
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier.widthIn(min = 280.dp, max = 560.dp),
            shape = RoundedCornerShape(28.dp), // xxl
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp, // Level 4
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
            ) {
                // 타이틀
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 본문
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 버튼 영역 (우측 정렬)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (dismissText != null) {
                        FridgeRecipeTextButton(
                            text = dismissText,
                            onClick = onDismiss,
                        )
                    }
                    FridgeRecipeTextButton(
                        text = confirmText,
                        onClick = onConfirm,
                    )
                }
            }
        }
    }
}
