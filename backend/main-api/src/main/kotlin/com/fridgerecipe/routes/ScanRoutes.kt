package com.fridgerecipe.routes

import com.fridgerecipe.core.config.AppConfig
import com.fridgerecipe.core.extension.userId
import com.fridgerecipe.data.database.ScanHistoryTable
import com.fridgerecipe.plugins.BadRequestException
import com.fridgerecipe.plugins.NotFoundException
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.koin.ktor.ext.inject

@Serializable
data class ScanImageRequest(
    val imageBase64: String,
)

fun Route.scanRoutes() {
    val httpClient by inject<HttpClient>()

    route("/scan") {
        authenticate("auth-jwt") {
            post("/receipt") {
                val userId = call.userId()
                val request = call.receive<ScanImageRequest>()

                if (request.imageBase64.length > 15_000_000) {
                    throw BadRequestException("이미지 크기가 10MB를 초과합니다.")
                }

                val scanId = transaction {
                    ScanHistoryTable.insertAndGetId {
                        it[ScanHistoryTable.userId] = userId
                        it[scanType] = "receipt"
                        it[status] = "processing"
                    }.value
                }

                // Forward to AI service asynchronously
                try {
                    val response = httpClient.post("${AppConfig.aiServiceUrl}/ai/ocr/receipt") {
                        contentType(ContentType.Application.Json)
                        header("X-Internal-Api-Key", AppConfig.aiServiceApiKey)
                        setBody(mapOf(
                            "image_base64" to request.imageBase64,
                            "scan_id" to scanId,
                            "user_id" to userId,
                        ))
                    }
                } catch (_: Exception) {
                    // AI service call is async; scan result will be polled
                }

                call.respond(HttpStatusCode.Accepted, mapOf(
                    "success" to true,
                    "data" to mapOf("scanId" to scanId, "status" to "processing")
                ))
            }

            get("/receipt/{scanId}") {
                val userId = call.userId()
                val scanId = call.parameters["scanId"]?.toLongOrNull()
                    ?: throw BadRequestException("유효하지 않은 스캔 ID입니다.")

                val scan = transaction {
                    ScanHistoryTable.selectAll()
                        .where {
                            (ScanHistoryTable.id eq scanId) and (ScanHistoryTable.userId eq userId)
                        }
                        .firstOrNull()
                } ?: throw NotFoundException("스캔 결과를 찾을 수 없습니다.")

                call.respond(HttpStatusCode.OK, mapOf(
                    "success" to true,
                    "data" to mapOf(
                        "scanId" to scan[ScanHistoryTable.id].value,
                        "status" to scan[ScanHistoryTable.status],
                        "result" to scan[ScanHistoryTable.result],
                    )
                ))
            }

            post("/photo") {
                val userId = call.userId()
                val request = call.receive<ScanImageRequest>()

                val scanId = transaction {
                    ScanHistoryTable.insertAndGetId {
                        it[ScanHistoryTable.userId] = userId
                        it[scanType] = "photo"
                        it[status] = "processing"
                    }.value
                }

                try {
                    httpClient.post("${AppConfig.aiServiceUrl}/ai/vision/ingredients") {
                        contentType(ContentType.Application.Json)
                        header("X-Internal-Api-Key", AppConfig.aiServiceApiKey)
                        setBody(mapOf(
                            "image_base64" to request.imageBase64,
                            "scan_id" to scanId,
                            "user_id" to userId,
                        ))
                    }
                } catch (_: Exception) {
                    // Async processing
                }

                call.respond(HttpStatusCode.Accepted, mapOf(
                    "success" to true,
                    "data" to mapOf("scanId" to scanId, "status" to "processing")
                ))
            }

            get("/photo/{scanId}") {
                val userId = call.userId()
                val scanId = call.parameters["scanId"]?.toLongOrNull()
                    ?: throw BadRequestException("유효하지 않은 스캔 ID입니다.")

                val scan = transaction {
                    ScanHistoryTable.selectAll()
                        .where {
                            (ScanHistoryTable.id eq scanId) and (ScanHistoryTable.userId eq userId)
                        }
                        .firstOrNull()
                } ?: throw NotFoundException("스캔 결과를 찾을 수 없습니다.")

                call.respond(HttpStatusCode.OK, mapOf(
                    "success" to true,
                    "data" to mapOf(
                        "scanId" to scan[ScanHistoryTable.id].value,
                        "status" to scan[ScanHistoryTable.status],
                        "result" to scan[ScanHistoryTable.result],
                    )
                ))
            }
        }
    }
}
