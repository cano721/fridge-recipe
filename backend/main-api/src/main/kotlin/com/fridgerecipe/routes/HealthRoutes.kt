package com.fridgerecipe.routes

import com.fridgerecipe.core.config.AppConfig
import com.fridgerecipe.data.database.DatabaseFactory
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.lettuce.core.RedisClient
import kotlinx.serialization.Serializable
@Serializable
data class HealthResponse(
    val status: String,
    val db: String,
    val redis: String,
    val aiService: String
)

fun Route.healthRoutes(config: AppConfig) {

    get("/health") {
        val dbStatus = try {
            DatabaseFactory.ping()
            "connected"
        } catch (e: Exception) {
            "disconnected"
        }

        val redisStatus = try {
            val client = RedisClient.create("redis://${config.redis.password}@${config.redis.host}:${config.redis.port}")
            val connection = client.connect()
            val result = connection.sync().ping()
            connection.close()
            client.shutdown()
            if (result == "PONG") "connected" else "disconnected"
        } catch (e: Exception) {
            "disconnected"
        }

        val aiServiceStatus = try {
            val httpClient = HttpClient(CIO)
            val response = httpClient.get("${config.aiService.url}/ai/health") {
                header("X-Internal-Api-Key", config.aiService.apiKey)
            }
            httpClient.close()
            if (response.status == HttpStatusCode.OK) "connected" else "disconnected"
        } catch (e: Exception) {
            "disconnected"
        }

        val overallStatus = if (dbStatus == "connected" && redisStatus == "connected") "ok" else "degraded"

        call.respond(HealthResponse(
            status = overallStatus,
            db = dbStatus,
            redis = redisStatus,
            aiService = aiServiceStatus
        ))
    }
}
