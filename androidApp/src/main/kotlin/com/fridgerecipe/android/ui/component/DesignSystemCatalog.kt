package com.fridgerecipe.android.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fridgerecipe.android.ui.theme.FridgeRecipeColors
import com.fridgerecipe.android.ui.theme.FridgeRecipeTheme

// ─── Color Palette ───────────────────────────────────────────

@Composable
private fun ColorSwatch(
    color: Color,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color)
                .border(0.5.dp, Color.Black.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColorPaletteSection() {
    SectionHeader("Color Palette")

    Text("Primary (Herb Green)", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ColorSwatch(FridgeRecipeColors.Primary50, "50")
        ColorSwatch(FridgeRecipeColors.Primary100, "100")
        ColorSwatch(FridgeRecipeColors.Primary200, "200")
        ColorSwatch(FridgeRecipeColors.Primary300, "300")
        ColorSwatch(FridgeRecipeColors.Primary400, "400")
        ColorSwatch(FridgeRecipeColors.Primary500, "500")
        ColorSwatch(FridgeRecipeColors.Primary600, "600")
        ColorSwatch(FridgeRecipeColors.Primary700, "700")
        ColorSwatch(FridgeRecipeColors.Primary800, "800")
        ColorSwatch(FridgeRecipeColors.Primary900, "900")
    }

    Spacer(modifier = Modifier.height(16.dp))
    Text("Secondary (Warm Apricot)", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ColorSwatch(FridgeRecipeColors.Secondary50, "50")
        ColorSwatch(FridgeRecipeColors.Secondary100, "100")
        ColorSwatch(FridgeRecipeColors.Secondary200, "200")
        ColorSwatch(FridgeRecipeColors.Secondary300, "300")
        ColorSwatch(FridgeRecipeColors.Secondary400, "400")
        ColorSwatch(FridgeRecipeColors.Secondary500, "500")
        ColorSwatch(FridgeRecipeColors.Secondary600, "600")
        ColorSwatch(FridgeRecipeColors.Secondary700, "700")
        ColorSwatch(FridgeRecipeColors.Secondary800, "800")
        ColorSwatch(FridgeRecipeColors.Secondary900, "900")
    }

    Spacer(modifier = Modifier.height(16.dp))
    Text("Accent & Semantic", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ColorSwatch(FridgeRecipeColors.Accent500, "Accent")
        ColorSwatch(FridgeRecipeColors.Success, "Success")
        ColorSwatch(FridgeRecipeColors.Warning, "Warning")
        ColorSwatch(FridgeRecipeColors.Error, "Error")
        ColorSwatch(FridgeRecipeColors.Info, "Info")
    }

    Spacer(modifier = Modifier.height(16.dp))
    Text("Expiry Status", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ColorSwatch(FridgeRecipeColors.ExpirySafe, "Safe")
        ColorSwatch(FridgeRecipeColors.ExpirySoon, "Soon")
        ColorSwatch(FridgeRecipeColors.ExpiryUrgent, "Urgent")
        ColorSwatch(FridgeRecipeColors.ExpiryExpired, "Expired")
    }
}

// ─── Typography ──────────────────────────────────────────────

@Composable
private fun TypographySection() {
    SectionHeader("Typography")

    val samples = listOf(
        "Display Large (57sp)" to MaterialTheme.typography.displayLarge,
        "Headline Large (32sp)" to MaterialTheme.typography.headlineLarge,
        "Headline Medium (28sp)" to MaterialTheme.typography.headlineMedium,
        "Headline Small (24sp)" to MaterialTheme.typography.headlineSmall,
        "Title Large (22sp)" to MaterialTheme.typography.titleLarge,
        "Title Medium (16sp)" to MaterialTheme.typography.titleMedium,
        "Title Small (14sp)" to MaterialTheme.typography.titleSmall,
        "Body Large (16sp)" to MaterialTheme.typography.bodyLarge,
        "Body Medium (14sp)" to MaterialTheme.typography.bodyMedium,
        "Body Small (12sp)" to MaterialTheme.typography.bodySmall,
        "Label Large (14sp)" to MaterialTheme.typography.labelLarge,
        "Label Medium (12sp)" to MaterialTheme.typography.labelMedium,
        "Label Small (11sp)" to MaterialTheme.typography.labelSmall,
    )

    samples.forEach { (label, style) ->
        Text(text = label, style = style, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(4.dp))
    }
}

// ─── Buttons ─────────────────────────────────────────────────

@Composable
private fun ButtonSection() {
    SectionHeader("Buttons")

    Text("Primary Button", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(8.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        FridgeRecipePrimaryButton(text = "냉장고에 추가", onClick = {})
        FridgeRecipePrimaryButton(text = "비활성", onClick = {}, enabled = false)
    }

    Spacer(modifier = Modifier.height(16.dp))
    Text("Secondary Button", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(8.dp))
    FridgeRecipeSecondaryButton(text = "레시피 둘러보기", onClick = {})

    Spacer(modifier = Modifier.height(16.dp))
    Text("Text Button", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(8.dp))
    FridgeRecipeTextButton(text = "더보기", onClick = {})
}

// ─── Expiry Badges ───────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpiryBadgeSection() {
    SectionHeader("Expiry Badges")

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ExpiryBadge(daysLeft = 14)
        ExpiryBadge(daysLeft = 3)
        ExpiryBadge(daysLeft = 1)
        ExpiryBadge(daysLeft = 0)
        ExpiryBadge(daysLeft = -1)
    }
}

// ─── Chips ───────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipSection() {
    SectionHeader("Chips")

    Text("Filter Chip", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        var selected1 by remember { mutableStateOf(true) }
        var selected2 by remember { mutableStateOf(false) }
        var selected3 by remember { mutableStateOf(false) }
        FridgeRecipeFilterChip(label = "채식", selected = selected1, onSelectedChange = { selected1 = it })
        FridgeRecipeFilterChip(label = "비건", selected = selected2, onSelectedChange = { selected2 = it })
        FridgeRecipeFilterChip(label = "글루텐프리", selected = selected3, onSelectedChange = { selected3 = it })
    }

    Spacer(modifier = Modifier.height(16.dp))
    Text("Suggestion Chip", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FridgeRecipeSuggestionChip(label = "오늘+3일", onClick = {})
        FridgeRecipeSuggestionChip(label = "오늘+7일", onClick = {})
        FridgeRecipeSuggestionChip(label = "직접입력", onClick = {})
    }

    Spacer(modifier = Modifier.height(16.dp))
    Text("Input Chip", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FridgeRecipeInputChip(label = "당근", onRemove = {})
        FridgeRecipeInputChip(label = "양파", onRemove = {})
        FridgeRecipeInputChip(label = "달걀", onRemove = {})
    }
}

// ─── Search Field ────────────────────────────────────────────

@Composable
private fun SearchFieldSection() {
    SectionHeader("Search Field")

    var text by remember { mutableStateOf("") }
    FridgeRecipeSearchField(value = text, onValueChange = { text = it })
}

// ─── Cards ───────────────────────────────────────────────────

@Composable
private fun CardSection() {
    SectionHeader("Ingredient Card")

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        IngredientCard(
            icon = "\uD83E\uDD55",
            name = "당근",
            quantity = "500g",
            daysLeft = 2,
            onClick = {},
        )
        IngredientCard(
            icon = "\uD83E\uDD5B",
            name = "우유",
            quantity = "1L",
            daysLeft = 3,
            onClick = {},
        )
        IngredientCard(
            icon = "\uD83E\uDD66",
            name = "브로콜리",
            quantity = "1개",
            daysLeft = 10,
            onClick = {},
        )
        IngredientCard(
            icon = "\uD83C\uDF45",
            name = "토마토",
            quantity = "5개",
            daysLeft = -1,
            onClick = {},
        )
    }
}

@Composable
private fun RecipeCardSection() {
    SectionHeader("Recipe Card")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        RecipeCard(
            title = "김치볶음밥",
            cookTimeMinutes = 20,
            rating = 4.8f,
            matchPercent = 100,
            servings = 2,
            onClick = {},
            modifier = Modifier.weight(1f),
        )
        RecipeCard(
            title = "된장찌개",
            cookTimeMinutes = 30,
            rating = 4.9f,
            matchPercent = 80,
            servings = 3,
            onClick = {},
            modifier = Modifier.weight(1f),
        )
    }
}

// ─── TextField ───────────────────────────────────────────────

@Composable
private fun TextFieldSection() {
    SectionHeader("Text Field")

    var text1 by remember { mutableStateOf("") }
    FridgeRecipeTextField(
        value = text1,
        onValueChange = { text1 = it },
        label = "재료 이름",
        placeholder = "예: 당근, 양파",
    )

    Spacer(modifier = Modifier.height(12.dp))

    FridgeRecipeTextField(
        value = "잘못된 입력",
        onValueChange = {},
        label = "수량",
        isError = true,
        errorMessage = "숫자만 입력할 수 있어요",
    )
}

// ─── FAB ─────────────────────────────────────────────────────

@Composable
private fun FabSection() {
    SectionHeader("FAB (Floating Action Button)")

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FridgeRecipeFab(
                icon = Icons.Default.Add,
                contentDescription = "재료 추가",
                onClick = {},
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Standard", style = MaterialTheme.typography.labelSmall)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FridgeRecipeSmallFab(
                icon = Icons.Default.CameraAlt,
                contentDescription = "카메라",
                onClick = {},
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Small", style = MaterialTheme.typography.labelSmall)
        }
    }
}

// ─── Skeleton ────────────────────────────────────────────────

@Composable
private fun SkeletonSection() {
    SectionHeader("Skeleton / Shimmer")

    Text("Ingredient Card Skeleton", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(8.dp))
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        IngredientCardSkeleton()
        IngredientCardSkeleton()
    }

    Spacer(modifier = Modifier.height(16.dp))
    Text("Recipe Card Skeleton", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        RecipeCardSkeleton(modifier = Modifier.weight(1f))
        RecipeCardSkeleton(modifier = Modifier.weight(1f))
    }
}

// ─── Error State ─────────────────────────────────────────────

@Composable
private fun ErrorStateSection() {
    SectionHeader("Error State")

    Text("Inline Error", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(8.dp))
    InlineError(message = "수량을 입력해주세요")

    Spacer(modifier = Modifier.height(16.dp))
    Text("Network Error Banner", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(8.dp))
    NetworkErrorBanner()
}

// ─── Bottom Navigation ──────────────────────────────────────

@Composable
private fun BottomNavSection() {
    SectionHeader("Bottom Navigation Bar")

    FridgeRecipeBottomNavBar(
        selectedRoute = "home",
        onItemSelected = {},
    )
}

// ─── Empty State ─────────────────────────────────────────────

@Composable
private fun EmptyStateSection() {
    SectionHeader("Empty State")

    EmptyState(
        illustration = "\uD83E\uDDCA",
        title = "냉장고가 텅 비었어요",
        description = "재료를 추가하면 맞춤 레시피를 추천해요",
        ctaText = "재료 추가하기",
        onCtaClick = {},
    )
}

// ─── Helpers ─────────────────────────────────────────────────

@Composable
private fun SectionHeader(title: String) {
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary,
    )
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.outlineVariant,
    )
}

// ─── Full Catalog ────────────────────────────────────────────

@Composable
fun DesignSystemCatalog(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(
            text = "Design System Catalog",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "냉장고 레시피 디자인 시스템",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        ColorPaletteSection()
        TypographySection()
        ButtonSection()
        TextFieldSection()
        ExpiryBadgeSection()
        ChipSection()
        SearchFieldSection()
        FabSection()
        CardSection()
        RecipeCardSection()
        SkeletonSection()
        ErrorStateSection()
        EmptyStateSection()
        BottomNavSection()

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ─── Previews ────────────────────────────────────────────────

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Design System - Light",
)
@Composable
private fun DesignSystemCatalogLightPreview() {
    FridgeRecipeTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            DesignSystemCatalog()
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Design System - Dark",
)
@Composable
private fun DesignSystemCatalogDarkPreview() {
    FridgeRecipeTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            DesignSystemCatalog()
        }
    }
}

// ─── Individual Component Previews ───────────────────────────

@Preview(showBackground = true, name = "Buttons")
@Composable
private fun ButtonPreview() {
    FridgeRecipeTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            ButtonSection()
        }
    }
}

@Preview(showBackground = true, name = "Expiry Badges")
@Composable
private fun ExpiryBadgePreview() {
    FridgeRecipeTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            ExpiryBadgeSection()
        }
    }
}

@Preview(showBackground = true, name = "Ingredient Cards")
@Composable
private fun IngredientCardPreview() {
    FridgeRecipeTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            CardSection()
        }
    }
}

@Preview(showBackground = true, name = "Recipe Cards")
@Composable
private fun RecipeCardPreview() {
    FridgeRecipeTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            RecipeCardSection()
        }
    }
}

@Preview(showBackground = true, name = "Chips")
@Composable
private fun ChipPreview() {
    FridgeRecipeTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            ChipSection()
        }
    }
}
