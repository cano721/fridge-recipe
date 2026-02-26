package com.fridgerecipe.android.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fridgerecipe.shared.dto.IngredientResponse
import com.fridgerecipe.android.ui.component.IngredientCard

// Mock 데이터 (API 연동 전)
private val mockIngredients = listOf(
    IngredientResponse(id = 1, ingredientId = 1, name = "우유", category = "유제품", quantity = 1.0, unit = "개", daysUntilExpiry = 1, storageType = "fridge"),
    IngredientResponse(id = 2, ingredientId = 2, name = "달걀", category = "단백질", quantity = 6.0, unit = "개", daysUntilExpiry = 0, storageType = "fridge"),
    IngredientResponse(id = 3, ingredientId = 3, name = "두부", category = "단백질", quantity = 1.0, unit = "모", daysUntilExpiry = 2, storageType = "fridge"),
    IngredientResponse(id = 4, ingredientId = 4, name = "냉동만두", category = "냉동식품", quantity = 20.0, unit = "개", daysUntilExpiry = 30, storageType = "freezer"),
    IngredientResponse(id = 5, ingredientId = 5, name = "아이스크림", category = "냉동식품", quantity = 1.0, unit = "통", daysUntilExpiry = 60, storageType = "freezer"),
    IngredientResponse(id = 6, ingredientId = 6, name = "라면", category = "가공식품", quantity = 5.0, unit = "개", daysUntilExpiry = 180, storageType = "room"),
    IngredientResponse(id = 7, ingredientId = 7, name = "고추장", category = "양념", quantity = 1.0, unit = "통", daysUntilExpiry = 90, storageType = "room"),
)

private data class StorageTab(val label: String, val storageKey: String?)

private val storageTabs = listOf(
    StorageTab("전체", null),
    StorageTab("냉장", "fridge"),
    StorageTab("냉동", "freezer"),
    StorageTab("상온", "room"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeScreen(navController: NavController) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val filteredIngredients = remember(selectedTabIndex) {
        val key = storageTabs[selectedTabIndex].storageKey
        if (key == null) mockIngredients
        else mockIngredients.filter { it.storageType == key }
    }

    // 카테고리별 그룹핑
    val grouped = remember(filteredIngredients) {
        filteredIngredients.groupBy { it.category ?: "기타" }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = "냉장고",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            )

            // 스토리지 탭
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color(0xFF3DA63D)
            ) {
                storageTabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = tab.label,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            if (filteredIngredients.isEmpty()) {
                // 빈 상태
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Kitchen,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "냉장고가 텅 비었어요",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "식재료를 추가해보세요",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    grouped.forEach { (category, items) ->
                        item(key = "header_$category") {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                            )
                        }
                        items(items, key = { it.id }) { ingredient ->
                            IngredientCard(ingredient = ingredient)
                        }
                    }
                    // FAB 여백
                    item { Spacer(modifier = Modifier.height(72.dp)) }
                }
            }
        }

        // FAB - 식재료 추가
        FloatingActionButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF3DA63D),
            contentColor = Color.White
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "식재료 추가")
        }
    }
}
