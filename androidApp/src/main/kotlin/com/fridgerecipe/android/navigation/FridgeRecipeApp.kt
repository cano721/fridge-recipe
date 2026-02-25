package com.fridgerecipe.android.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.fridgerecipe.android.ui.screen.FridgeScreen
import com.fridgerecipe.android.ui.screen.HomeScreen
import com.fridgerecipe.android.ui.screen.ProfileScreen
import com.fridgerecipe.android.ui.screen.RecipeScreen

enum class Screen(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    HOME("홈", Icons.Filled.Home, Icons.Outlined.Home),
    FRIDGE("냉장고", Icons.Filled.Kitchen, Icons.Outlined.Kitchen),
    RECIPE("레시피", Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
    PROFILE("마이페이지", Icons.Filled.Person, Icons.Outlined.Person),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeRecipeApp() {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                Screen.entries.forEach { screen ->
                    val selected = currentScreen == screen
                    NavigationBarItem(
                        selected = selected,
                        onClick = { currentScreen = screen },
                        icon = {
                            Icon(
                                imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                                contentDescription = screen.label,
                            )
                        },
                        label = { Text(screen.label) },
                    )
                }
            }
        }
    ) { innerPadding ->
        when (currentScreen) {
            Screen.HOME -> HomeScreen(modifier = Modifier.padding(innerPadding))
            Screen.FRIDGE -> FridgeScreen(modifier = Modifier.padding(innerPadding))
            Screen.RECIPE -> RecipeScreen(modifier = Modifier.padding(innerPadding))
            Screen.PROFILE -> ProfileScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
