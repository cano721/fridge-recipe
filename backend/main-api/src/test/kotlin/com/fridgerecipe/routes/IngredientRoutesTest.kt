package com.fridgerecipe.routes

import com.fridgerecipe.configureTestAppWithAuth
import com.fridgerecipe.createJsonClient
import com.fridgerecipe.domain.model.UserIngredient
import com.fridgerecipe.domain.service.IngredientService
import com.fridgerecipe.generateTestToken
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IngredientRoutesTest {

    private val mockIngredientService = mockk<IngredientService>()
    private val testUserId = 1L
    private val testToken by lazy { generateTestToken(userId = testUserId) }

    private val sampleIngredient = UserIngredient(
        id = 100L,
        userId = testUserId,
        ingredientId = 1L,
        ingredientName = "당근",
        category = "채소",
        iconUrl = null,
        quantity = java.math.BigDecimal("2.0"),
        unit = "개",
        expiryDate = "2025-03-01",
        storageType = "fridge",
        memo = null,
        registeredVia = "manual",
        createdAt = "2025-02-01T00:00:00Z"
    )

    private fun ingredientToJson(i: UserIngredient) =
        """{"id":${i.id},"ingredientId":${i.ingredientId},"ingredientName":"${i.ingredientName}","category":"${i.category}","iconUrl":null,"quantity":${i.quantity?.toDouble()},"unit":"${i.unit}","expiryDate":"${i.expiryDate}","storageType":"${i.storageType}","memo":null,"registeredVia":"${i.registeredVia}","createdAt":"${i.createdAt}"}"""

    private fun ApplicationTestBuilder.setupRoutes() {
        configureTestAppWithAuth {
            routing {
                route("/api/v1") {
                    authenticate("auth-jwt") {
                        route("/ingredients") {
                            get {
                                val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
                                val page = call.parameters["page"]?.toIntOrNull() ?: 1
                                val size = call.parameters["size"]?.toIntOrNull() ?: 20
                                val ingredients = mockIngredientService.getMyIngredients(userId, page, size)
                                val total = mockIngredientService.countMyIngredients(userId)
                                val dataJson = ingredients.joinToString(",") { ingredientToJson(it) }
                                call.respondText(
                                    """{"success":true,"data":[$dataJson],"meta":{"page":$page,"size":$size,"total":$total}}""",
                                    ContentType.Application.Json
                                )
                            }

                            post {
                                val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
                                val bodyText = call.receive<String>()
                                val json = Json.parseToJsonElement(bodyText).jsonObject
                                val ingredientId = json["ingredientId"]!!.jsonPrimitive.content.toLong()
                                val quantity = json["quantity"]?.jsonPrimitive?.doubleOrNull
                                val unit = json["unit"]?.jsonPrimitive?.content
                                val expiryDate = json["expiryDate"]?.jsonPrimitive?.content
                                val storageType = json["storageType"]?.jsonPrimitive?.content ?: "fridge"
                                val memo = json["memo"]?.jsonPrimitive?.content

                                val ingredient = mockIngredientService.register(
                                    userId = userId,
                                    ingredientId = ingredientId,
                                    quantity = quantity?.let { java.math.BigDecimal.valueOf(it) },
                                    unit = unit,
                                    expiryDate = expiryDate,
                                    storageType = storageType,
                                    memo = memo
                                )
                                call.respondText(
                                    """{"success":true,"data":${ingredientToJson(ingredient)}}""",
                                    ContentType.Application.Json,
                                    HttpStatusCode.Created
                                )
                            }

                            put("/{id}") {
                                val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
                                val id = call.parameters["id"]?.toLongOrNull()
                                    ?: throw IllegalArgumentException("VALIDATION_FAILED")
                                val bodyText = call.receive<String>()
                                val json = Json.parseToJsonElement(bodyText).jsonObject
                                val quantity = json["quantity"]?.jsonPrimitive?.doubleOrNull
                                val unit = json["unit"]?.jsonPrimitive?.content
                                val expiryDate = json["expiryDate"]?.jsonPrimitive?.content
                                val storageType = json["storageType"]?.jsonPrimitive?.content
                                val memo = json["memo"]?.jsonPrimitive?.content

                                val ingredient = mockIngredientService.update(
                                    userId = userId,
                                    id = id,
                                    quantity = quantity?.let { java.math.BigDecimal.valueOf(it) },
                                    unit = unit,
                                    expiryDate = expiryDate,
                                    storageType = storageType,
                                    memo = memo
                                )
                                call.respondText(
                                    """{"success":true,"data":${ingredientToJson(ingredient)}}""",
                                    ContentType.Application.Json
                                )
                            }

                            delete("/{id}") {
                                val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
                                val id = call.parameters["id"]?.toLongOrNull()
                                    ?: throw IllegalArgumentException("VALIDATION_FAILED")
                                mockIngredientService.delete(userId, id)
                                call.respondText(
                                    """{"success":true,"data":{"message":"삭제되었습니다."}}""",
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
    fun `GET ingredients without token returns 401`() = testApplication {
        setupRoutes()
        val response = client.get("/api/v1/ingredients")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `GET ingredients with valid token returns ingredient list`() = testApplication {
        coEvery { mockIngredientService.getMyIngredients(testUserId, 1, 20) } returns listOf(sampleIngredient)
        coEvery { mockIngredientService.countMyIngredients(testUserId) } returns 1L

        setupRoutes()
        val client = createJsonClient()
        val response = client.get("/api/v1/ingredients") {
            header(HttpHeaders.Authorization, "Bearer $testToken")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("당근"))
        assertTrue(body.contains("success"))
    }

    @Test
    fun `GET ingredients returns empty list when no ingredients`() = testApplication {
        coEvery { mockIngredientService.getMyIngredients(testUserId, 1, 20) } returns emptyList()
        coEvery { mockIngredientService.countMyIngredients(testUserId) } returns 0L

        setupRoutes()
        val client = createJsonClient()
        val response = client.get("/api/v1/ingredients") {
            header(HttpHeaders.Authorization, "Bearer $testToken")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("\"data\":[]"))
    }

    @Test
    fun `POST ingredients registers new ingredient and returns 201`() = testApplication {
        coEvery {
            mockIngredientService.register(
                userId = testUserId,
                ingredientId = 1L,
                quantity = java.math.BigDecimal.valueOf(2.0),
                unit = "개",
                expiryDate = "2025-03-01",
                storageType = "fridge",
                memo = null
            )
        } returns sampleIngredient

        setupRoutes()
        val client = createJsonClient()
        val response = client.post("/api/v1/ingredients") {
            header(HttpHeaders.Authorization, "Bearer $testToken")
            contentType(ContentType.Application.Json)
            setBody("""{"ingredientId":1,"quantity":2.0,"unit":"개","expiryDate":"2025-03-01","storageType":"fridge"}""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
        assertTrue(response.bodyAsText().contains("당근"))
    }

    @Test
    fun `POST ingredients without token returns 401`() = testApplication {
        setupRoutes()
        val response = client.post("/api/v1/ingredients") {
            contentType(ContentType.Application.Json)
            setBody("""{"ingredientId":1,"quantity":2.0,"unit":"개","storageType":"fridge"}""")
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `PUT ingredients updates existing ingredient`() = testApplication {
        val updatedIngredient = sampleIngredient.copy(quantity = java.math.BigDecimal("3.0"))
        coEvery {
            mockIngredientService.update(
                userId = testUserId,
                id = 100L,
                quantity = java.math.BigDecimal.valueOf(3.0),
                unit = null,
                expiryDate = null,
                storageType = null,
                memo = null
            )
        } returns updatedIngredient

        setupRoutes()
        val client = createJsonClient()
        val response = client.put("/api/v1/ingredients/100") {
            header(HttpHeaders.Authorization, "Bearer $testToken")
            contentType(ContentType.Application.Json)
            setBody("""{"quantity":3.0}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("success"))
    }

    @Test
    fun `PUT ingredients with invalid id returns 400`() = testApplication {
        setupRoutes()
        val response = client.put("/api/v1/ingredients/not-a-number") {
            header(HttpHeaders.Authorization, "Bearer $testToken")
            contentType(ContentType.Application.Json)
            setBody("""{"quantity":3.0}""")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `DELETE ingredients removes ingredient and returns success`() = testApplication {
        coEvery { mockIngredientService.delete(testUserId, 100L) } returns true

        setupRoutes()
        val client = createJsonClient()
        val response = client.delete("/api/v1/ingredients/100") {
            header(HttpHeaders.Authorization, "Bearer $testToken")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("삭제되었습니다"))
    }

    @Test
    fun `DELETE ingredients without token returns 401`() = testApplication {
        setupRoutes()
        val response = client.delete("/api/v1/ingredients/100")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}
