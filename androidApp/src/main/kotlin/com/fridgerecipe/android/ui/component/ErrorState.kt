package com.fridgerecipe.android.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fridgerecipe.android.ui.theme.FridgeRecipeColors

/**
 * 인라인 에러
 * Error-Main 색상, error_outline 16dp + 텍스트, bodySmall
 */
@Composable
fun InlineError(
    message: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = FridgeRecipeColors.Error,
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = FridgeRecipeColors.Error,
        )
    }
}

/**
 * 페이지 레벨 에러
 * 빈 상태와 동일 구조 + 재시도 버튼
 */
@Composable
fun PageError(
    title: String,
    description: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EmptyState(
        illustration = "⚠️",
        title = title,
        description = description,
        ctaText = "다시 시도",
        onCtaClick = onRetry,
        modifier = modifier,
    )
}

/**
 * 네트워크 에러 배너
 * 상단 고정, Error-Container 배경, 48dp, wifi_off 아이콘
 */
@Composable
fun NetworkErrorBanner(
    modifier: Modifier = Modifier,
    message: String = "네트워크 연결을 확인해주세요",
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(FridgeRecipeColors.ErrorContainer)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.WifiOff,
            contentDescription = "네트워크 오류",
            modifier = Modifier.size(20.dp),
            tint = FridgeRecipeColors.Error,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = FridgeRecipeColors.Error,
        )
    }
}
