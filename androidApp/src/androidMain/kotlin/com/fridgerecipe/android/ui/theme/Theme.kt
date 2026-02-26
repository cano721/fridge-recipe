package com.fridgerecipe.android.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = FridgeRecipeColors.Primary,
    onPrimary = FridgeRecipeColors.OnPrimary,
    primaryContainer = FridgeRecipeColors.PrimaryLight,
    secondary = FridgeRecipeColors.Secondary,
    onSecondary = FridgeRecipeColors.OnSecondary,
    secondaryContainer = FridgeRecipeColors.SecondaryLight,
    background = FridgeRecipeColors.BackgroundLight,
    onBackground = FridgeRecipeColors.OnBackgroundLight,
    surface = FridgeRecipeColors.SurfaceLight,
    onSurface = FridgeRecipeColors.OnSurfaceLight,
    error = FridgeRecipeColors.ErrorLight,
    onError = FridgeRecipeColors.OnError
)

private val DarkColorScheme = darkColorScheme(
    primary = FridgeRecipeColors.PrimaryLight,
    onPrimary = FridgeRecipeColors.PrimaryDark,
    primaryContainer = FridgeRecipeColors.PrimaryDark,
    secondary = FridgeRecipeColors.SecondaryLight,
    onSecondary = FridgeRecipeColors.SecondaryDark,
    secondaryContainer = FridgeRecipeColors.SecondaryDark,
    background = FridgeRecipeColors.BackgroundDark,
    onBackground = FridgeRecipeColors.OnBackgroundDark,
    surface = FridgeRecipeColors.SurfaceDark,
    onSurface = FridgeRecipeColors.OnSurfaceDark,
    error = FridgeRecipeColors.ErrorDark,
    onError = FridgeRecipeColors.OnError
)

@Composable
fun FridgeRecipeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FridgeRecipeTypography,
        shapes = FridgeRecipeShapes,
        content = content
    )
}
