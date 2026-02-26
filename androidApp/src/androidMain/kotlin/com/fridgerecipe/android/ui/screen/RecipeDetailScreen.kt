package com.fridgerecipe.android.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fridgerecipe.android.ui.theme.FridgeRecipeColors

private data class RecipeIngredient(
    val name: String,
    val amount: String,
    val isOwned: Boolean
)

private data class RecipeStep(
    val order: Int,
    val description: String
)

private data class RecipeNutrition(
    val calories: String,
    val protein: String,
    val fat: String,
    val carbs: String
)

private data class RecipeDetail(
    val id: Long,
    val title: String,
    val cookingTime: Int,
    val servings: Int,
    val difficulty: String,
    val ownedCount: Int,
    val totalCount: Int,
    val ingredients: List<RecipeIngredient>,
    val steps: List<RecipeStep>,
    val nutrition: RecipeNutrition
)

private val mockDetails = mapOf(
    1L to RecipeDetail(
        id = 1L,
        title = "김치찌개",
        cookingTime = 30,
        servings = 2,
        difficulty = "쉬움",
        ownedCount = 9,
        totalCount = 10,
        ingredients = listOf(
            RecipeIngredient("김치", "200g", true),
            RecipeIngredient("돼지고기", "150g", true),
            RecipeIngredient("두부", "1/2모", true),
            RecipeIngredient("대파", "1대", true),
            RecipeIngredient("고춧가루", "1큰술", true),
            RecipeIngredient("된장", "1작은술", true),
            RecipeIngredient("참기름", "1작은술", true),
            RecipeIngredient("다진마늘", "1큰술", true),
            RecipeIngredient("물", "400ml", true),
            RecipeIngredient("새우젓", "1작은술", false)
        ),
        steps = listOf(
            RecipeStep(1, "김치를 적당한 크기로 썰어 냄비에 볶아줍니다."),
            RecipeStep(2, "돼지고기를 넣고 함께 볶아 육즙을 살립니다."),
            RecipeStep(3, "물을 붓고 고춧가루, 된장을 넣어 끓입니다."),
            RecipeStep(4, "두부와 대파를 넣고 5분간 더 끓입니다."),
            RecipeStep(5, "참기름을 두르고 불을 끕니다.")
        ),
        nutrition = RecipeNutrition("320kcal", "18g", "12g", "35g")
    ),
    2L to RecipeDetail(
        id = 2L,
        title = "된장찌개",
        cookingTime = 25,
        servings = 2,
        difficulty = "쉬움",
        ownedCount = 5,
        totalCount = 6,
        ingredients = listOf(
            RecipeIngredient("된장", "2큰술", true),
            RecipeIngredient("두부", "1/2모", true),
            RecipeIngredient("애호박", "1/4개", true),
            RecipeIngredient("감자", "1개", true),
            RecipeIngredient("대파", "1대", true),
            RecipeIngredient("멸치육수", "500ml", false)
        ),
        steps = listOf(
            RecipeStep(1, "감자와 애호박을 깍둑썰기 합니다."),
            RecipeStep(2, "멸치육수를 끓인 후 된장을 풀어줍니다."),
            RecipeStep(3, "감자를 먼저 넣고 5분간 끓입니다."),
            RecipeStep(4, "두부, 애호박, 대파를 넣고 5분간 더 끓입니다.")
        ),
        nutrition = RecipeNutrition("180kcal", "12g", "5g", "22g")
    )
)

private fun getMockDetail(recipeId: Long): RecipeDetail {
    return mockDetails[recipeId] ?: RecipeDetail(
        id = recipeId,
        title = "레시피 $recipeId",
        cookingTime = 20,
        servings = 2,
        difficulty = "보통",
        ownedCount = 4,
        totalCount = 6,
        ingredients = listOf(
            RecipeIngredient("재료 1", "100g", true),
            RecipeIngredient("재료 2", "50g", true),
            RecipeIngredient("재료 3", "1개", false)
        ),
        steps = listOf(
            RecipeStep(1, "재료를 손질합니다."),
            RecipeStep(2, "팬에 볶아줍니다."),
            RecipeStep(3, "완성합니다.")
        ),
        nutrition = RecipeNutrition("250kcal", "10g", "8g", "30g")
    )
}

@Composable
fun RecipeDetailScreen(
    recipeId: Long,
    onBackClick: () -> Unit
) {
    val detail = remember(recipeId) { getMockDetail(recipeId) }
    var isBookmarked by remember { mutableStateOf(false) }
    val matchRatio = detail.ownedCount.toFloat() / detail.totalCount.toFloat()
    val matchColor = when {
        matchRatio >= 0.8f -> FridgeRecipeColors.Primary
        matchRatio >= 0.5f -> FridgeRecipeColors.Secondary
        else -> FridgeRecipeColors.Urgent
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 헤더 이미지 (placeholder)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier.matchParentSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🍽", fontSize = 64.sp)
            }

            // 뒤로가기 + 북마크 오버레이
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "뒤로가기",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = { isBookmarked = !isBookmarked },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isBookmarked) "북마크 해제" else "북마크",
                        tint = if (isBookmarked) Color(0xFFE53935) else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // 제목
            Text(
                text = detail.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 정보 Row (3칸 균등)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoItem(label = "조리시간", value = "${detail.cookingTime}분")
                InfoDivider()
                InfoItem(label = "인분", value = "${detail.servings}인분")
                InfoDivider()
                InfoItem(label = "난이도", value = detail.difficulty)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // 매칭률
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "보유 재료",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${detail.ownedCount}/${detail.totalCount}개",
                    style = MaterialTheme.typography.bodyMedium,
                    color = matchColor,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { matchRatio },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = matchColor,
                trackColor = matchColor.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // 재료 섹션
            Text(
                text = "재료",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            detail.ingredients.forEach { ingredient ->
                IngredientRow(ingredient = ingredient)
                Spacer(modifier = Modifier.height(6.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // 조리법 섹션
            Text(
                text = "조리법",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            detail.steps.forEach { step ->
                StepRow(step = step)
                Spacer(modifier = Modifier.height(12.dp))
            }

            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // 영양정보 섹션
            Text(
                text = "영양정보",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutritionItem(label = "칼로리", value = detail.nutrition.calories)
                NutritionItem(label = "단백질", value = detail.nutrition.protein)
                NutritionItem(label = "지방", value = detail.nutrition.fat)
                NutritionItem(label = "탄수화물", value = detail.nutrition.carbs)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun InfoDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(36.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}

@Composable
private fun IngredientRow(ingredient: RecipeIngredient) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (ingredient.isOwned) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = if (ingredient.isOwned) FridgeRecipeColors.Primary else FridgeRecipeColors.Urgent
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            color = if (ingredient.isOwned)
                MaterialTheme.colorScheme.onSurface
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = ingredient.amount,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StepRow(step: RecipeStep) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${step.order}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = step.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .padding(top = 3.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun NutritionItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.shapes.small
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}
