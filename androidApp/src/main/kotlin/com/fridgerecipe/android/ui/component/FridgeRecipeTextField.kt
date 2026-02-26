package com.fridgerecipe.android.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fridgerecipe.android.ui.theme.FridgeRecipeColors

/**
 * 일반 텍스트 필드 (Input)
 * 높이: 56dp, Border: 1.5dp Outline, Border Radius: 12dp
 * 포커스 Border: 2dp Primary-500
 * 에러 Border: 2dp Error-Main
 * Label: bodySmall, 필드 위로 float
 * 에러 메시지: bodySmall, Error-Main, 하단 4dp 간격
 */
@Composable
fun FridgeRecipeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            label = label?.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            },
            placeholder = placeholder?.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    )
                }
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            singleLine = singleLine,
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = RoundedCornerShape(12.dp), // md
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = FridgeRecipeColors.Outline,
                focusedBorderColor = FridgeRecipeColors.Primary500,
                errorBorderColor = FridgeRecipeColors.Error,
                cursorColor = FridgeRecipeColors.Primary500,
                errorCursorColor = FridgeRecipeColors.Error,
            ),
        )

        // 에러 메시지
        if (isError && errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = FridgeRecipeColors.Error,
                modifier = Modifier.padding(start = 16.dp),
            )
        }
    }
}
