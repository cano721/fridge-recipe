package com.fridgerecipe.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fridgerecipe.android.ui.screen.FridgeScreen
import com.fridgerecipe.android.ui.screen.HomeScreen
import com.fridgerecipe.android.ui.screen.MyPageScreen
import com.fridgerecipe.android.ui.screen.ProfileEditScreen
import com.fridgerecipe.android.ui.screen.RecipeDetailScreen
import com.fridgerecipe.android.ui.screen.RecipeScreen
import com.fridgerecipe.android.ui.screen.ScanScreen
import com.fridgerecipe.android.ui.screen.SettingsScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("fridge") { FridgeScreen(navController) }
        composable("recipe") { RecipeScreen(navController) }
        composable("mypage") { MyPageScreen(navController) }
        composable("recipe_detail/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toLongOrNull() ?: 0L
            RecipeDetailScreen(recipeId = recipeId, onBackClick = { navController.popBackStack() })
        }
        composable("settings") { SettingsScreen(onBackClick = { navController.popBackStack() }) }
        composable("profile_edit") { ProfileEditScreen(onBackClick = { navController.popBackStack() }) }
        composable("scan") { ScanScreen(onBackClick = { navController.popBackStack() }) }
    }
}
