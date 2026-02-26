package com.fridgerecipe.android.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.fridgerecipe.android.ui.theme.FridgeRecipeColors

/**
 * Bottom Navigation Item 데이터
 */
data class BottomNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
)

/**
 * 기본 Bottom Navigation 탭 목록
 */
val DefaultBottomNavItems = listOf(
    BottomNavItem("홈", Icons.Filled.Home, Icons.Outlined.Home, "home"),
    BottomNavItem("냉장고", Icons.Filled.Search, Icons.Outlined.Search, "fridge"),
    BottomNavItem("레시피", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder, "recipe"),
    BottomNavItem("마이페이지", Icons.Filled.Person, Icons.Outlined.Person, "mypage"),
)

/**
 * Bottom Navigation Bar
 * 높이: 80dp (Safe Area 포함)
 * Elevation: Level 2
 * 아이콘: 24dp, Outlined(비선택) / Filled(선택)
 * 라벨: labelMedium (12sp)
 * 선택 색상: Primary-500
 * 비선택 색상: On-Surface-Variant
 * 선택 인디케이터: Primary-100
 */
@Composable
fun FridgeRecipeBottomNavBar(
    items: List<BottomNavItem> = DefaultBottomNavItems,
    selectedRoute: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp, // Level 2
    ) {
        items.forEach { item ->
            val selected = item.route == selectedRoute
            NavigationBarItem(
                selected = selected,
                onClick = { onItemSelected(item.route) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = FridgeRecipeColors.Primary500,
                    selectedTextColor = FridgeRecipeColors.Primary500,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = FridgeRecipeColors.Primary100,
                ),
            )
        }
    }
}
