package com.fridgerecipe.android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text(
                text = "안녕하세요!",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = "냉장고에 재료를 추가하고 레시피를 추천받아보세요",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        item {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("레시피나 재료를 검색해보세요") },
                singleLine = true,
            )
        }

        item {
            Text(
                text = "소비기한 임박 재료",
                style = MaterialTheme.typography.titleMedium,
            )
            // TODO: Expiring ingredients horizontal scroll
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "오늘 추천 레시피",
                    style = MaterialTheme.typography.titleMedium,
                )
                TextButton(onClick = { }) {
                    Text("더보기")
                }
            }
            // TODO: Recipe recommendation cards
        }
    }
}
