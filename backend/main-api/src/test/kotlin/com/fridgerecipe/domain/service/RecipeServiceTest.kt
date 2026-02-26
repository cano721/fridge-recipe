package com.fridgerecipe.domain.service

import com.fridgerecipe.domain.model.Recipe
import com.fridgerecipe.domain.model.RecipeIngredient
import com.fridgerecipe.domain.model.UserIngredient
import com.fridgerecipe.domain.repository.BookmarkRepository
import com.fridgerecipe.domain.repository.IngredientMasterRepository
import com.fridgerecipe.domain.repository.IngredientRepository
import com.fridgerecipe.domain.repository.RecipeRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RecipeServiceTest {

    private val mockRecipeRepository = mockk<RecipeRepository>()
    private val mockBookmarkRepository = mockk<BookmarkRepository>()
    private val mockIngredientRepository = mockk<IngredientRepository>()
    private val mockIngredientMasterRepository = mockk<IngredientMasterRepository>()

    private val recipeService = RecipeService(
        recipeRepository = mockRecipeRepository,
        bookmarkRepository = mockBookmarkRepository,
        ingredientRepository = mockIngredientRepository,
        ingredientMasterRepository = mockIngredientMasterRepository
    )

    private val userId = 1L

    private fun makeRecipe(id: Long, viewCount: Long = 0L, avgRating: Double = 0.0) = Recipe(
        id = id,
        title = "레시피 $id",
        description = null,
        imageUrl = null,
        thumbnailUrl = null,
        servings = 2,
        cookingTime = 30,
        difficulty = "easy",
        cuisineType = null,
        sourceUrl = null,
        sourceType = "manual",
        steps = null,
        nutrition = null,
        tags = emptyList(),
        viewCount = viewCount,
        avgRating = avgRating,
        reviewCount = 0
    )

    private fun makeIngredient(ingredientId: Long, isEssential: Boolean = true) = RecipeIngredient(
        ingredientId = ingredientId,
        ingredientName = "재료 $ingredientId",
        quantity = 1.0,
        unit = "개",
        isEssential = isEssential,
        substituteIds = emptyList()
    )

    private fun makeUserIngredient(ingredientId: Long, expiryDate: String? = null) = UserIngredient(
        id = ingredientId,
        userId = userId,
        ingredientId = ingredientId,
        ingredientName = "재료 $ingredientId",
        category = "채소",
        expiryDate = expiryDate,
        storageType = "fridge"
    )

    // ─── 매칭률 계산 검증 ───────────────────────────────────────────

    @Test
    fun `matchRatio is calculated correctly when all essentials matched`() = runBlocking {
        val recipeIngredients = listOf(
            makeIngredient(1L), makeIngredient(2L), makeIngredient(3L)
        )
        val myIngredients = listOf(
            makeUserIngredient(1L), makeUserIngredient(2L), makeUserIngredient(3L)
        )

        coEvery { mockIngredientRepository.findByUserId(userId, 1, 200) } returns myIngredients
        coEvery { mockRecipeRepository.findRecipesWithIngredientIds(listOf(1L, 2L, 3L)) } returns
            listOf(Pair(makeRecipe(1L), recipeIngredients))

        val result = recipeService.getRecommendations(userId)

        assertEquals(1, result.size)
        assertEquals(1.0, result[0].matchRatio, "모든 재료 매칭 시 matchRatio는 1.0이어야 함")
        assertEquals(3, result[0].matchedCount)
        assertEquals(3, result[0].totalRequired)
        assertTrue(result[0].missingIngredients.isEmpty(), "누락 재료가 없어야 함")
    }

    @Test
    fun `matchRatio is calculated correctly when partial essentials matched`() = runBlocking {
        val recipeIngredients = listOf(
            makeIngredient(1L), makeIngredient(2L), makeIngredient(3L), makeIngredient(4L)
        )
        val myIngredients = listOf(
            makeUserIngredient(1L), makeUserIngredient(2L)
        )

        coEvery { mockIngredientRepository.findByUserId(userId, 1, 200) } returns myIngredients
        coEvery { mockRecipeRepository.findRecipesWithIngredientIds(listOf(1L, 2L)) } returns
            listOf(Pair(makeRecipe(1L), recipeIngredients))

        val result = recipeService.getRecommendations(userId)

        assertEquals(1, result.size)
        assertEquals(0.5, result[0].matchRatio, "2/4 매칭 시 matchRatio는 0.5이어야 함")
        assertEquals(2, result[0].matchedCount)
        assertEquals(4, result[0].totalRequired)
        assertEquals(2, result[0].missingIngredients.size, "누락 재료가 2개이어야 함")
    }

    // ─── matchLabel 분류 검증 ────────────────────────────────────────

    @Test
    fun `matchLabel is 바로 요리 가능 when matchRatio is 80 percent or above`() = runBlocking {
        val recipeIngredients = listOf(
            makeIngredient(1L), makeIngredient(2L), makeIngredient(3L),
            makeIngredient(4L), makeIngredient(5L)
        )
        val myIngredients = listOf(
            makeUserIngredient(1L), makeUserIngredient(2L), makeUserIngredient(3L),
            makeUserIngredient(4L), makeUserIngredient(5L)
        )

        coEvery { mockIngredientRepository.findByUserId(userId, 1, 200) } returns myIngredients
        coEvery { mockRecipeRepository.findRecipesWithIngredientIds(any()) } returns
            listOf(Pair(makeRecipe(1L), recipeIngredients))

        val result = recipeService.getRecommendations(userId)

        assertEquals("바로 요리 가능", result[0].matchLabel)
    }

    @Test
    fun `matchLabel is 재료 조금 부족 when matchRatio is between 50 and 80 percent`() = runBlocking {
        // 3/5 = 60%
        val recipeIngredients = listOf(
            makeIngredient(1L), makeIngredient(2L), makeIngredient(3L),
            makeIngredient(4L), makeIngredient(5L)
        )
        val myIngredients = listOf(
            makeUserIngredient(1L), makeUserIngredient(2L), makeUserIngredient(3L)
        )

        coEvery { mockIngredientRepository.findByUserId(userId, 1, 200) } returns myIngredients
        coEvery { mockRecipeRepository.findRecipesWithIngredientIds(listOf(1L, 2L, 3L)) } returns
            listOf(Pair(makeRecipe(1L), recipeIngredients))

        val result = recipeService.getRecommendations(userId)

        assertEquals("재료 조금 부족", result[0].matchLabel)
    }

    @Test
    fun `matchLabel is 도전해보세요 when matchRatio is between 30 and 50 percent`() = runBlocking {
        // 2/5 = 40%
        val recipeIngredients = listOf(
            makeIngredient(1L), makeIngredient(2L), makeIngredient(3L),
            makeIngredient(4L), makeIngredient(5L)
        )
        val myIngredients = listOf(
            makeUserIngredient(1L), makeUserIngredient(2L)
        )

        coEvery { mockIngredientRepository.findByUserId(userId, 1, 200) } returns myIngredients
        coEvery { mockRecipeRepository.findRecipesWithIngredientIds(listOf(1L, 2L)) } returns
            listOf(Pair(makeRecipe(1L), recipeIngredients))

        val result = recipeService.getRecommendations(userId)

        assertEquals("도전해보세요", result[0].matchLabel)
    }

    // ─── 30% 미만 필터링 검증 ────────────────────────────────────────

    @Test
    fun `recipes with matchRatio below 30 percent are excluded`() = runBlocking {
        // 1/5 = 20% → 제외
        val recipeIngredients = listOf(
            makeIngredient(1L), makeIngredient(2L), makeIngredient(3L),
            makeIngredient(4L), makeIngredient(5L)
        )
        val myIngredients = listOf(makeUserIngredient(1L))

        coEvery { mockIngredientRepository.findByUserId(userId, 1, 200) } returns myIngredients
        coEvery { mockRecipeRepository.findRecipesWithIngredientIds(listOf(1L)) } returns
            listOf(Pair(makeRecipe(1L), recipeIngredients))

        val result = recipeService.getRecommendations(userId)

        assertTrue(result.isEmpty(), "20% 매칭률은 필터링되어 결과가 비어야 함")
    }

    @Test
    fun `recipes with matchRatio exactly 30 percent are included`() = runBlocking {
        // 3/10 = 30% → 포함
        val recipeIngredients = (1L..10L).map { makeIngredient(it) }
        val myIngredients = listOf(
            makeUserIngredient(1L), makeUserIngredient(2L), makeUserIngredient(3L)
        )

        coEvery { mockIngredientRepository.findByUserId(userId, 1, 200) } returns myIngredients
        coEvery { mockRecipeRepository.findRecipesWithIngredientIds(listOf(1L, 2L, 3L)) } returns
            listOf(Pair(makeRecipe(1L), recipeIngredients))

        val result = recipeService.getRecommendations(userId)

        assertEquals(1, result.size, "30% 매칭률은 포함되어야 함")
        assertEquals(0.3, result[0].matchRatio, absoluteTolerance = 0.001)
    }

    @Test
    fun `returns empty list when user has no ingredients`() = runBlocking {
        coEvery { mockIngredientRepository.findByUserId(userId, 1, 200) } returns emptyList()

        val result = recipeService.getRecommendations(userId)

        assertTrue(result.isEmpty(), "재료가 없으면 빈 추천 목록이어야 함")
    }

    @Test
    fun `non-essential ingredients do not affect matchRatio`() = runBlocking {
        // essential 2개 + non-essential 5개, 사용자는 essential 2개만 보유
        val recipeIngredients = listOf(
            makeIngredient(1L, isEssential = true),
            makeIngredient(2L, isEssential = true),
            makeIngredient(3L, isEssential = false),
            makeIngredient(4L, isEssential = false),
            makeIngredient(5L, isEssential = false)
        )
        val myIngredients = listOf(makeUserIngredient(1L), makeUserIngredient(2L))

        coEvery { mockIngredientRepository.findByUserId(userId, 1, 200) } returns myIngredients
        coEvery { mockRecipeRepository.findRecipesWithIngredientIds(listOf(1L, 2L)) } returns
            listOf(Pair(makeRecipe(1L), recipeIngredients))

        val result = recipeService.getRecommendations(userId)

        assertEquals(1, result.size)
        assertEquals(1.0, result[0].matchRatio, "필수 재료 2/2 → matchRatio는 1.0이어야 함")
        assertEquals(2, result[0].totalRequired, "totalRequired는 필수 재료 수만 포함해야 함")
    }

    @Test
    fun `results are sorted by score descending`() = runBlocking {
        // 레시피 A: 100% 매칭, 레시피 B: 50% 매칭 → A가 먼저
        val recipeA = makeRecipe(id = 1L, viewCount = 50L, avgRating = 4.0)
        val recipeB = makeRecipe(id = 2L, viewCount = 10L, avgRating = 3.0)

        val ingredientsA = listOf(makeIngredient(1L), makeIngredient(2L))
        val ingredientsB = listOf(makeIngredient(1L), makeIngredient(2L), makeIngredient(3L), makeIngredient(4L))

        val myIngredients = listOf(makeUserIngredient(1L), makeUserIngredient(2L))

        coEvery { mockIngredientRepository.findByUserId(userId, 1, 200) } returns myIngredients
        coEvery { mockRecipeRepository.findRecipesWithIngredientIds(listOf(1L, 2L)) } returns
            listOf(
                Pair(recipeA, ingredientsA),
                Pair(recipeB, ingredientsB)
            )

        val result = recipeService.getRecommendations(userId)

        assertEquals(2, result.size)
        // A(100%) > B(50%) → A가 첫 번째
        assertEquals(1L, result[0].recipe.id, "높은 매칭률 레시피가 먼저 나와야 함")
        assertEquals(2L, result[1].recipe.id)
    }

    @Test
    fun `missing ingredients list is correct`() = runBlocking {
        val recipeIngredients = listOf(
            makeIngredient(1L), makeIngredient(2L), makeIngredient(3L)
        )
        val myIngredients = listOf(makeUserIngredient(1L))

        coEvery { mockIngredientRepository.findByUserId(userId, 1, 200) } returns myIngredients
        coEvery { mockRecipeRepository.findRecipesWithIngredientIds(listOf(1L)) } returns
            listOf(Pair(makeRecipe(1L), recipeIngredients))

        val result = recipeService.getRecommendations(userId)

        assertEquals(1, result.size)
        assertEquals(2, result[0].missingIngredients.size, "누락 재료가 2개이어야 함")
        assertTrue(result[0].missingIngredients.contains("재료 2"), "재료 2가 누락 목록에 있어야 함")
        assertTrue(result[0].missingIngredients.contains("재료 3"), "재료 3이 누락 목록에 있어야 함")
    }
}
