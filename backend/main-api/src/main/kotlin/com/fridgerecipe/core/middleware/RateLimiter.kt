package com.fridgerecipe.core.middleware

import com.fridgerecipe.core.config.AppConfig
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection

class RateLimiter(config: AppConfig.RedisConfig) {
    private val client: RedisClient = RedisClient.create(
        if (config.password.isNotBlank()) {
            "redis://${config.password}@${config.host}:${config.port}"
        } else {
            "redis://${config.host}:${config.port}"
        }
    )
    private val connection: StatefulRedisConnection<String, String> = client.connect()
    private val commands = connection.sync()

    data class RateLimitResult(
        val allowed: Boolean,
        val limit: Long,
        val remaining: Long,
        val resetAt: Long // Unix timestamp (seconds)
    )

    /**
     * Sliding Window Counter 기반 Rate Limiting
     * @param key 사용자 식별 키 (e.g., "rate:user:123:api")
     * @param maxRequests 윈도우 내 최대 요청 수
     * @param windowSeconds 윈도우 크기 (초)
     */
    fun check(key: String, maxRequests: Long, windowSeconds: Long): RateLimitResult {
        val now = System.currentTimeMillis()
        val windowStart = now - (windowSeconds * 1000)
        val resetAt = (now / 1000) + windowSeconds

        // 만료된 엔트리 제거
        commands.zremrangebyscore(key, "-inf", windowStart.toDouble().toString())

        // 현재 윈도우 내 요청 수
        val currentCount = commands.zcard(key)

        if (currentCount >= maxRequests) {
            return RateLimitResult(
                allowed = false,
                limit = maxRequests,
                remaining = 0,
                resetAt = resetAt
            )
        }

        // 요청 추가
        commands.zadd(key, now.toDouble(), "$now")
        commands.expire(key, windowSeconds)

        return RateLimitResult(
            allowed = true,
            limit = maxRequests,
            remaining = maxRequests - currentCount - 1,
            resetAt = resetAt
        )
    }

    fun close() {
        connection.close()
        client.shutdown()
    }
}
