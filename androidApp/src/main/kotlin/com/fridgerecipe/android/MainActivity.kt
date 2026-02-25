package com.fridgerecipe.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fridgerecipe.android.ui.theme.FridgeRecipeTheme
import com.fridgerecipe.android.navigation.FridgeRecipeApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FridgeRecipeTheme {
                FridgeRecipeApp()
            }
        }
    }
}
