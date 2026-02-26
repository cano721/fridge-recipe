package com.fridgerecipe.core.middleware

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

class RateLimitConfig {
    var rateLimiter: RateLimiter? = null
    var defaultLimit: Long = 100
    var defaultWindowSeconds: Long = 60
    var scanLimit: Long = 10
    var scanWindowSeconds: Long = 86400 // 24시간
}

val RateLimitPlugin = createRouteScopedPlugin("RateLimit", ::RateLimitConfig) {
    val rateLimiter = pluginConfig.rateLimiter ?: return@createRouteScopedPlugin
    val defaultLimit = pluginConfig.defaultLimit
    val defaultWindowSeconds = pluginConfig.defaultWindowSeconds
    val scanLimit = pluginConfig.scanLimit
    val scanWindowSeconds = pluginConfig.scanWindowSeconds

    onCall { call ->
        val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()
            ?: return@onCall // 미인증 요청은 Rate Limit 스킵

        val path = call.request.local.uri
        val isScanApi = path.contains("/scan/")

        val (limit, window, key) = if (isScanApi) {
            Triple(scanLimit, scanWindowSeconds, "rate:scan:$userId")
        } else {
            Triple(defaultLimit, defaultWindowSeconds, "rate:api:$userId")
        }

        val result = rateLimiter.check(key, limit, window)

        // 응답 헤더 추가
        call.response.headers.append("X-RateLimit-Limit", result.limit.toString())
        call.response.headers.append("X-RateLimit-Remaining", result.remaining.toString())
        call.response.headers.append("X-RateLimit-Reset", result.resetAt.toString())

        if (!result.allowed) {
            call.response.headers.append("Retry-After", window.toString())
            call.respond(
                HttpStatusCode.TooManyRequests,
                mapOf(
                    "success" to false,
                    "error" to mapOf(
                        "code" to "RATE_LIMIT_EXCEEDED",
                        "message" to "요청 제한을 초과했습니다. ${window}초 후에 다시 시도해주세요."
                    )
                )
            )
        }
    }
}
