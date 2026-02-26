package com.fridgerecipe.android.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fridgerecipe.android.ui.component.RecipeCard

private data class MockRecipe(
    val id: Long,
    val title: String,
    val cookingTime: Int,
    val rating: Double,
    val matchRatio: Double,
    val matchLabel: String,
    val isBookmarked: Boolean = false
)

private val mockRecipes = listOf(
    MockRecipe(1L, "김치찌개", 30, 4.8, 0.9, "재료 9/10개 보유"),
    MockRecipe(2L, "된장찌개", 25, 4.6, 0.83, "재료 5/6개 보유"),
    MockRecipe(3L, "제육볶음", 20, 4.7, 0.75, "재료 6/8개 보유"),
    MockRecipe(4L, "계란볶음밥", 15, 4.5, 1.0, "재료 전부 보유"),
    MockRecipe(5L, "부대찌개", 35, 4.9, 0.6, "재료 6/10개 보유"),
    MockRecipe(6L, "파스타 아라비아타", 25, 4.3, 0.4, "재료 4/10개 보유")
)

private val filterCategories = listOf("전체", "한식", "양식", "중식", "일식", "간단요리")

@Composable
fun RecipeScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("전체") }
    val bookmarkStates = remember { mutableStateOf(mockRecipes.map { it.isBookmarked }.toMutableList()) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 타이틀
        Text(
            text = "레시피",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        // 검색 바
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("레시피 검색...") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        // 필터 칩 (가로 스크롤)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filterCategories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category) }
                )
            }
        }

        // 추천 섹션 헤더
        Text(
            text = "내 재료로 만들 수 있는 레시피",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        // 레시피 그리드
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(mockRecipes) { recipe ->
                val index = mockRecipes.indexOf(recipe)
                RecipeCard(
                    title = recipe.title,
                    imageUrl = null,
                    cookingTime = recipe.cookingTime,
                    rating = recipe.rating,
                    matchRatio = recipe.matchRatio,
                    matchLabel = recipe.matchLabel,
                    isBookmarked = bookmarkStates.value[index],
                    onBookmarkClick = {
                        val updated = bookmarkStates.value.toMutableList()
                        updated[index] = !updated[index]
                        bookmarkStates.value = updated
                    },
                    onClick = { navController.navigate("recipe_detail/${recipe.id}") }
                )
            }
        }
    }
}
