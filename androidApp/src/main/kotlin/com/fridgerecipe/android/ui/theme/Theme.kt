package com.fridgerecipe.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = FridgeRecipeColors.Primary500,
    onPrimary = Color.White,
    primaryContainer = FridgeRecipeColors.Primary100,
    onPrimaryContainer = FridgeRecipeColors.Primary900,
    secondary = FridgeRecipeColors.Secondary500,
    onSecondary = Color.White,
    secondaryContainer = FridgeRecipeColors.Secondary100,
    onSecondaryContainer = FridgeRecipeColors.Secondary900,
    background = FridgeRecipeColors.Background,
    onBackground = FridgeRecipeColors.OnBackground,
    surface = FridgeRecipeColors.Surface,
    onSurface = FridgeRecipeColors.OnSurface,
    surfaceVariant = FridgeRecipeColors.SurfaceVariant,
    onSurfaceVariant = FridgeRecipeColors.OnSurfaceVariant,
    outline = FridgeRecipeColors.Outline,
    outlineVariant = FridgeRecipeColors.OutlineVariant,
    error = FridgeRecipeColors.Error,
    onError = Color.White,
    errorContainer = FridgeRecipeColors.ErrorContainer,
    inverseSurface = FridgeRecipeColors.InverseSurface,
    inverseOnSurface = FridgeRecipeColors.InverseOnSurface,
    inversePrimary = FridgeRecipeColors.InversePrimary,
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6EC96E),
    onPrimary = Color(0xFF1A3D1A),
    primaryContainer = Color(0xFF225522),
    onPrimaryContainer = Color(0xFFC8E6C8),
    secondary = FridgeRecipeColors.SecondaryDark300,
    onSecondary = Color(0xFF3D2200),
    secondaryContainer = Color(0xFF5C3300),
    onSecondaryContainer = FridgeRecipeColors.Secondary100,
    background = FridgeRecipeColors.DarkBackground,
    onBackground = FridgeRecipeColors.DarkOnBackground,
    surface = FridgeRecipeColors.DarkSurface,
    onSurface = FridgeRecipeColors.DarkOnSurface,
    surfaceVariant = FridgeRecipeColors.DarkSurfaceVariant,
    onSurfaceVariant = FridgeRecipeColors.DarkOnSurfaceVariant,
    outline = FridgeRecipeColors.DarkOutline,
    error = FridgeRecipeColors.AccentDark,
    onError = Color(0xFF680003),
    inverseSurface = FridgeRecipeColors.InverseOnSurface,
    inverseOnSurface = FridgeRecipeColors.InverseSurface,
    inversePrimary = FridgeRecipeColors.Primary700,
)

@Composable
fun FridgeRecipeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FridgeRecipeTypography,
        shapes = FridgeRecipeShapes,
        content = content,
    )
}
