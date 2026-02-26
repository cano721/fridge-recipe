package com.fridgerecipe.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val code: String,
    val message: String,
    val details: Map<String, String>? = null
)

@Serializable
data class ErrorResponse(
    val success: Boolean = false,
    val error: ApiError
)

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(error = ApiError("VALIDATION_FAILED", cause.message ?: "잘못된 요청입니다."))
            )
        }
        exception<Throwable> { call, cause ->
            call.application.environment.log.error("Unhandled exception", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(error = ApiError("INTERNAL_ERROR", "서버 내부 오류가 발생했습니다."))
            )
        }
        status(HttpStatusCode.NotFound) { call, _ ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(error = ApiError("NOT_FOUND", "요청한 리소스를 찾을 수 없습니다."))
            )
        }
    }
}
