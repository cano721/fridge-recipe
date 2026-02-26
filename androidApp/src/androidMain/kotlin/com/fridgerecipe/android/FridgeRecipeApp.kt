package com.fridgerecipe.android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.fridgerecipe.android.navigation.AppNavigation
import com.fridgerecipe.android.navigation.BottomNavBar
import com.fridgerecipe.android.ui.theme.FridgeRecipeTheme

@Composable
fun FridgeRecipeApp() {
    FridgeRecipeTheme {
        val navController = rememberNavController()
        var currentRoute by remember { mutableStateOf("home") }

        Scaffold(
            bottomBar = {
                BottomNavBar(currentRoute) { route ->
                    navController.navigate(route) {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                    currentRoute = route
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                AppNavigation(navController)
            }
        }
    }
}
