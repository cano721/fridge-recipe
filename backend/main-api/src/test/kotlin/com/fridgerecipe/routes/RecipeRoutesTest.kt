package com.fridgerecipe.routes

import com.fridgerecipe.configureTestAppWithAuth
import com.fridgerecipe.createJsonClient
import com.fridgerecipe.domain.model.Recipe
import com.fridgerecipe.domain.model.RecipeDetail
import com.fridgerecipe.domain.model.RecipeIngredient
import com.fridgerecipe.domain.model.RecipeRecommendation
import com.fridgerecipe.domain.service.RecipeService
import com.fridgerecipe.generateTestToken
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RecipeRoutesTest {

    private val mockRecipeService = mockk<RecipeService>()
    private val testUserId = 1L
    private val testToken by lazy { generateTestToken(userId = testUserId) }

    private val sampleRecipe = Recipe(
        id = 1L,
        title = "김치찌개",
        description = "맛있는 김치찌개",
        imageUrl = null,
        thumbnailUrl = null,
        servings = 2,
        cookingTime = 30,
        difficulty = "easy",
        cuisineType = "Korean",
        sourceUrl = null,
        sourceType = "manual",
        steps = null,
        nutrition = null,
        tags = listOf("한식", "찌개"),
        viewCount = 100L,
        avgRating = 4.5,
        reviewCount = 10
    )

    private val sampleRecommendation = RecipeRecommendation(
        recipe = sampleRecipe,
        matchRatio = 0.8,
        matchedCount = 4,
        totalRequired = 5,
        missingIngredients = listOf("두부"),
        matchLabel = "바로 요리 가능"
    )

    private val sampleIngredient = RecipeIngredient(
        ingredientId = 1L,
        ingredientName = "김치",
        quantity = 200.0,
        unit = "g",
        isEssential = true,
        substituteIds = emptyList()
    )

    private val sampleDetail = RecipeDetail(
        recipe = sampleRecipe,
        ingredients = listOf(sampleIngredient),
        isBookmarked = false,
        matchedIngredients = listOf(1L),
        matchRatio = 0.8
    )

    private fun ApplicationTestBuilder.setupRoutes() {
        configureTestAppWithAuth {
            routing {
                route("/api/v1") {
                    authenticate("auth-jwt") {
                        route("/recipes") {
                            get("/recommend") {
                                val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
                                val recommendations = mockRecipeService.getRecommendations(userId)
                                val dataJson = Json.encodeToString(recommendations)
                                call.respondText(
                                    """{"success":true,"data":$dataJson}""",
                                    ContentType.Application.Json
                                )
                            }

                            get("/search") {
                                val query = call.parameters["q"] ?: ""
                                val page = call.parameters["page"]?.toIntOrNull() ?: 1
                                val size = call.parameters["size"]?.toIntOrNull() ?: 20
                                if (query.isBlank()) {
                                    call.respondText(
                                        """{"success":true,"data":[],"meta":{"total":0}}""",
                                        ContentType.Application.Json
                                    )
                                    return@get
                                }
                                val (recipes, total) = mockRecipeService.search(query, page, size)
                                val dataJson = Json.encodeToString(recipes)
                                call.respondText(
                                    """{"success":true,"data":$dataJson,"meta":{"page":$page,"size":$size,"total":$total}}""",
                                    ContentType.Application.Json
                                )
                            }

                            get("/{id}") {
                                val id = call.parameters["id"]?.toLongOrNull()
                                    ?: throw IllegalArgumentException("VALIDATION_FAILED")
                                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()
                                val detail = mockRecipeService.getDetail(id, userId)
                                val dataJson = Json.encodeToString(detail)
                                call.respondText(
                                    """{"success":true,"data":$dataJson}""",
                                    ContentType.Application.Json
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `GET recipes recommend without token returns 401`() = testApplication {
        setupRoutes()
        val response = client.get("/api/v1/recipes/recommend")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `GET recipes recommend with valid token returns recommendation list`() = testApplication {
        coEvery { mockRecipeService.getRecommendations(testUserId) } returns listOf(sampleRecommendation)

        setupRoutes()
        val client = createJsonClient()
        val response = client.get("/api/v1/recipes/recommend") {
            header(HttpHeaders.Authorization, "Bearer $testToken")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("김치찌개"))
        assertTrue(body.contains("matchRatio"))
        assertTrue(body.contains("바로 요리 가능"))
    }

    @Test
    fun `GET recipes recommend returns empty list when no ingredients`() = testApplication {
        coEvery { mockRecipeService.getRecommendations(testUserId) } returns emptyList()

        setupRoutes()
        val client = createJsonClient()
        val response = client.get("/api/v1/recipes/recommend") {
            header(HttpHeaders.Authorization, "Bearer $testToken")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("\"data\":[]"))
    }

    @Test
    fun `GET recipes search with query returns matching recipes`() = testApplication {
        coEvery { mockRecipeService.search("김치", 1, 20) } returns Pair(listOf(sampleRecipe), 1L)

        setupRoutes()
        val client = createJsonClient()
        val response = client.get("/api/v1/recipes/search?q=김치") {
            header(HttpHeaders.Authorization, "Bearer $testToken")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("김치찌개"))
        assertTrue(body.contains("meta"))
    }

    @Test
    fun `GET recipes search with empty query returns empty list`() = testApplication {
        setupRoutes()
        val client = createJsonClient()
        val response = client.get("/api/v1/recipes/search?q=") {
            header(HttpHeaders.Authorization, "Bearer $testToken")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("\"data\":[]"))
        assertTrue(body.contains("\"total\":0"))
    }

    @Test
    fun `GET recipes search without query param returns empty list`() = testApplication {
        setupRoutes()
        val client = createJsonClient()
        val response = client.get("/api/v1/recipes/search") {
            header(HttpHeaders.Authorization, "Bearer $testToken")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("\"data\":[]"))
    }

    @Test
    fun `GET recipes by id returns recipe detail`() = testApplication {
        coEvery { mockRecipeService.getDetail(1L, testUserId) } returns sampleDetail

        setupRoutes()
        val client = createJsonClient()
        val response = client.get("/api/v1/recipes/1") {
            header(HttpHeaders.Authorization, "Bearer $testToken")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("김치찌개"))
        assertTrue(body.contains("isBookmarked"))
        assertTrue(body.contains("ingredients"))
    }

    @Test
    fun `GET recipes by invalid id returns 400`() = testApplication {
        setupRoutes()
        val response = client.get("/api/v1/recipes/not-a-number") {
            header(HttpHeaders.Authorization, "Bearer $testToken")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `GET recipes by id when not found returns 400`() = testApplication {
        coEvery { mockRecipeService.getDetail(999L, testUserId) } throws IllegalArgumentException("RECIPE_NOT_FOUND")

        setupRoutes()
        val client = createJsonClient()
        val response = client.get("/api/v1/recipes/999") {
            header(HttpHeaders.Authorization, "Bearer $testToken")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertTrue(response.bodyAsText().contains("RECIPE_NOT_FOUND"))
    }
}
