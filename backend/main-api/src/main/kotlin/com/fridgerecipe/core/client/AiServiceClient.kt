package com.fridgerecipe.core.client

import com.fridgerecipe.core.config.AppConfig
import com.fridgerecipe.domain.model.ScanItem
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class AiOcrReceiptResponse(
    val taskId: String
)

@Serializable
data class AiOcrReceiptResultResponse(
    val status: String,
    val items: List<AiScanItemDto>? = null,
    val errorMessage: String? = null
)

@Serializable
data class AiVisionIngredientsResponse(
    val taskId: String
)

@Serializable
data class AiVisionIngredientsResultResponse(
    val status: String,
    val items: List<AiScanItemDto>? = null,
    val errorMessage: String? = null
)

@Serializable
data class AiScanItemDto(
    val name: String,
    val quantity: Double? = null,
    val unit: String? = null,
    val confidence: Double = 0.0
)

fun AiScanItemDto.toDomain() = ScanItem(
    name = name,
    quantity = quantity,
    unit = unit,
    confidence = confidence
)

class AiServiceClient(private val config: AppConfig.AiServiceConfig) {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 10_000
        }
    }

    private fun HttpRequestBuilder.authHeader() {
        header("X-Internal-Api-Key", config.apiKey)
    }

    suspend fun requestReceiptOcr(imageUrl: String): String {
        val response: AiOcrReceiptResponse = client.post("${config.url}/ai/ocr/receipt") {
            authHeader()
            contentType(ContentType.Application.Json)
            setBody(mapOf("imageUrl" to imageUrl))
        }.body()
        return response.taskId
    }

    suspend fun getReceiptOcrResult(taskId: String): AiOcrReceiptResultResponse =
        client.get("${config.url}/ai/ocr/receipt/$taskId") {
            authHeader()
        }.body()

    suspend fun requestVisionIngredients(imageUrl: String): String {
        val response: AiVisionIngredientsResponse = client.post("${config.url}/ai/vision/ingredients") {
            authHeader()
            contentType(ContentType.Application.Json)
            setBody(mapOf("imageUrl" to imageUrl))
        }.body()
        return response.taskId
    }

    suspend fun getVisionIngredientsResult(taskId: String): AiVisionIngredientsResultResponse =
        client.get("${config.url}/ai/vision/ingredients/$taskId") {
            authHeader()
        }.body()
}
