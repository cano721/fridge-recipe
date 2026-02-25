package com.fridgerecipe.android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("전체", "냉장", "냉동", "상온")

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("냉장고") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Navigate to add ingredient */ },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Icon(Icons.Default.Add, contentDescription = "재료 추가")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) },
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    // TODO: Ingredient list grouped by category
                    Text(
                        text = "냉장고가 텅 비었어요",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 48.dp),
                    )
                    Text(
                        text = "재료를 추가하면 맞춤 레시피를 추천해요",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
