package com.fridgerecipe.android.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Snackbar
 * 위치: 화면 하단, Bottom Nav 위 8dp
 * 너비: 화면 너비 - 32dp, 최대 480dp
 * 높이: 48dp (단일 행), 68dp (액션 포함)
 * Border Radius: 12dp
 * 배경: Inverse-Surface (#2E2E2A)
 * 텍스트: bodyMedium, Inverse-On-Surface
 * 자동 닫힘: 3초 (SnackbarHostState에서 제어)
 */
@Composable
fun FridgeRecipeSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    ) { data: SnackbarData ->
        Snackbar(
            modifier = Modifier.widthIn(max = 480.dp),
            shape = RoundedCornerShape(12.dp),
            containerColor = MaterialTheme.colorScheme.inverseSurface,
            contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            action = data.visuals.actionLabel?.let { actionLabel ->
                {
                    TextButton(onClick = { data.performAction() }) {
                        Text(
                            text = actionLabel,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.inversePrimary,
                        )
                    }
                }
            },
        ) {
            Text(
                text = data.visuals.message,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
