package com.fridgerecipe.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val success: Boolean = false,
    val error: ErrorDetail,
)

@Serializable
data class ErrorDetail(
    val code: String,
    val message: String,
    val details: Map<String, String>? = null,
)

class NotFoundException(message: String) : RuntimeException(message)
class BadRequestException(message: String, val details: Map<String, String>? = null) : RuntimeException(message)
class ForbiddenException(message: String) : RuntimeException(message)

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<NotFoundException> { call, cause ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(error = ErrorDetail("NOT_FOUND", cause.message ?: "리소스를 찾을 수 없습니다."))
            )
        }

        exception<BadRequestException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(error = ErrorDetail("BAD_REQUEST", cause.message ?: "잘못된 요청입니다.", cause.details))
            )
        }

        exception<ForbiddenException> { call, cause ->
            call.respond(
                HttpStatusCode.Forbidden,
                ErrorResponse(error = ErrorDetail("FORBIDDEN", cause.message ?: "권한이 없습니다."))
            )
        }

        exception<Throwable> { call, cause ->
            call.application.environment.log.error("Unhandled exception", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(error = ErrorDetail("INTERNAL_ERROR", "서버 내부 오류가 발생했습니다."))
            )
        }
    }
}
