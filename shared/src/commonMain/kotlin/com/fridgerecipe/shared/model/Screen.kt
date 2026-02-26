package com.fridgerecipe.shared.model

/**
 * 앱 화면 정의 (네비게이션용)
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Fridge : Screen("fridge")
    data object Recipe : Screen("recipe")
    data object MyPage : Screen("mypage")
    data object Scan : Screen("scan")
    data object IngredientDetail : Screen("ingredient/{id}") {
        fun createRoute(id: Long) = "ingredient/$id"
    }
    data object RecipeDetail : Screen("recipe/{id}") {
        fun createRoute(id: Long) = "recipe/$id"
    }
}
