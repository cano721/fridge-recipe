package com.fridgerecipe.routes

import com.fridgerecipe.core.security.userId
import com.fridgerecipe.domain.service.ScanService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
@Serializable
data class ScanReceiptRequest(val imageUrl: String)

@Serializable
data class ScanPhotoRequest(val imageUrl: String)

fun Route.scanRoutes(scanService: ScanService) {

    route("/scan") {
        post("/receipt") {
            val userId = call.userId
            val req = call.receive<ScanReceiptRequest>()
            val scanId = scanService.receiptScan(userId, req.imageUrl)
            call.respond(HttpStatusCode.Created, mapOf("success" to true, "data" to mapOf("scanId" to scanId)))
        }

        get("/receipt/{scanId}") {
            val userId = call.userId
            val scanId = call.parameters["scanId"]?.toLongOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("success" to false, "error" to "유효하지 않은 scanId"))
            val result = scanService.getScanResult(scanId, userId)
                ?: return@get call.respond(HttpStatusCode.NotFound, mapOf("success" to false, "error" to "스캔 결과를 찾을 수 없습니다."))
            call.respond(mapOf("success" to true, "data" to result))
        }

        post("/photo") {
            val userId = call.userId
            val req = call.receive<ScanPhotoRequest>()
            val scanId = scanService.photoScan(userId, req.imageUrl)
            call.respond(HttpStatusCode.Created, mapOf("success" to true, "data" to mapOf("scanId" to scanId)))
        }

        get("/photo/{scanId}") {
            val userId = call.userId
            val scanId = call.parameters["scanId"]?.toLongOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("success" to false, "error" to "유효하지 않은 scanId"))
            val result = scanService.getScanResult(scanId, userId)
                ?: return@get call.respond(HttpStatusCode.NotFound, mapOf("success" to false, "error" to "스캔 결과를 찾을 수 없습니다."))
            call.respond(mapOf("success" to true, "data" to result))
        }
    }
}
