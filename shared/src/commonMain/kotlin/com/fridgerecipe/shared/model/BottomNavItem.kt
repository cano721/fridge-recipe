package com.fridgerecipe.shared.model

enum class BottomNavItem(
    val screen: Screen,
    val label: String,
    val iconName: String
) {
    HOME(Screen.Home, "홈", "home"),
    FRIDGE(Screen.Fridge, "냉장고", "kitchen"),
    RECIPE(Screen.Recipe, "레시피", "restaurant_menu"),
    MY_PAGE(Screen.MyPage, "마이페이지", "person")
}
