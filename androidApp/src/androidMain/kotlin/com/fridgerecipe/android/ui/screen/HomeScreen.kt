package com.fridgerecipe.android.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fridgerecipe.shared.dto.IngredientResponse
import com.fridgerecipe.android.ui.component.IngredientCard

// Mock 데이터 (API 연동 전)
private val mockExpiringItems = listOf(
    IngredientResponse(id = 1, ingredientId = 1, name = "우유", quantity = 1.0, unit = "개", daysUntilExpiry = 1, storageType = "fridge"),
    IngredientResponse(id = 2, ingredientId = 2, name = "달걀", quantity = 6.0, unit = "개", daysUntilExpiry = 0, storageType = "fridge"),
    IngredientResponse(id = 3, ingredientId = 3, name = "두부", quantity = 1.0, unit = "모", daysUntilExpiry = 2, storageType = "fridge"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = "냉장고 레시피",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 빠른 액션 버튼 3개
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionButton(
                        icon = Icons.Default.Receipt,
                        label = "영수증\n스캔",
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate("scan") }
                    )
                    QuickActionButton(
                        icon = Icons.Default.CameraAlt,
                        label = "사진\n스캔",
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate("scan") }
                    )
                    QuickActionButton(
                        icon = Icons.Default.Add,
                        label = "수동\n추가",
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate("fridge") }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 소비기한 임박 섹션 헤더
            item {
                Text(
                    text = "소비기한 임박 \uD83D\uDD25",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (mockExpiringItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "임박한 식재료가 없어요",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(mockExpiringItems) { ingredient ->
                    IngredientCard(ingredient = ingredient)
                }
            }

            // 추천 레시피 섹션
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "오늘의 추천 레시피",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "레시피 추천 기능이 곧 출시됩니다",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3DA63D).copy(alpha = 0.1f),
            contentColor = Color(0xFF3DA63D)
        ),
        contentPadding = PaddingValues(4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, fontSize = 11.sp, lineHeight = 14.sp)
        }
    }
}
