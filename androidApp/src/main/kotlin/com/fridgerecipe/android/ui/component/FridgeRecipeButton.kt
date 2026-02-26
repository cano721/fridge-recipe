package com.fridgerecipe.android.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.fridgerecipe.android.ui.theme.FridgeRecipeColors

/**
 * Primary Button (Filled)
 * 높이: 48dp, 좌우 패딩: 24dp, Border Radius: 12dp
 * 눌림: scale 0.97
 */
@Composable
fun FridgeRecipePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "button_scale",
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .scale(scale),
        enabled = enabled,
        shape = MaterialTheme.shapes.medium, // 12dp
        colors = ButtonDefaults.buttonColors(
            containerColor = FridgeRecipeColors.Primary500,
            contentColor = androidx.compose.ui.graphics.Color.White,
            disabledContainerColor = FridgeRecipeColors.Primary500.copy(alpha = 0.4f),
            disabledContentColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.6f),
        ),
        contentPadding = PaddingValues(horizontal = 24.dp),
        interactionSource = interactionSource,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

/**
 * Secondary Button (Outlined)
 * 높이: 48dp, Border: 1.5dp Primary-500, Border Radius: 12dp
 * 눌림: Primary-50 배경
 */
@Composable
fun FridgeRecipeSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.5.dp, FridgeRecipeColors.Primary500),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = FridgeRecipeColors.Primary600,
        ),
        contentPadding = PaddingValues(horizontal = 24.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

/**
 * Text Button
 * 높이: 40dp, 좌우 패딩: 12dp
 * 텍스트: Primary-600
 */
@Composable
fun FridgeRecipeTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = FridgeRecipeColors.Primary600,
        ),
        contentPadding = PaddingValues(horizontal = 12.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
