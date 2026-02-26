package com.fridgerecipe.routes

import com.fridgerecipe.domain.model.NotificationSetting
import com.fridgerecipe.domain.service.NotificationService
import io.ktor.http.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
@Serializable
data class NotificationSettingRequest(
    val expiryEnabled: Boolean? = null,
    val expiryDays: List<Int>? = null,
    val themePreference: String? = null
)

@Serializable
data class DeviceTokenRequest(
    val token: String,
    val platform: String
)

fun Route.notificationRoutes(notificationService: NotificationService) {

    route("/notifications") {
        get("/settings") {
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
            val settings = notificationService.getSettings(userId)
            call.respond(mapOf("success" to true, "data" to settings))
        }

        put("/settings") {
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
            val req = call.receive<NotificationSettingRequest>()
            val current = notificationService.getSettings(userId)
            val updated = current.copy(
                expiryEnabled = req.expiryEnabled ?: current.expiryEnabled,
                expiryDays = req.expiryDays ?: current.expiryDays,
                themePreference = req.themePreference ?: current.themePreference
            )
            notificationService.updateSettings(userId, updated)
            call.respond(mapOf("success" to true, "data" to updated))
        }

        post("/device-token") {
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
            val req = call.receive<DeviceTokenRequest>()
            val id = notificationService.registerDeviceToken(userId, req.token, req.platform)
            call.respond(HttpStatusCode.Created, mapOf("success" to true, "data" to mapOf("id" to id)))
        }
    }
}
