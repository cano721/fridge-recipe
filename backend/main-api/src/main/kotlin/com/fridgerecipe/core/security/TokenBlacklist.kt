package com.fridgerecipe.core.security

import com.fridgerecipe.core.config.AppConfig
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection

class TokenBlacklist(config: AppConfig.RedisConfig) {
    private val client: RedisClient = RedisClient.create(
        if (config.password.isNotBlank()) {
            "redis://${config.password}@${config.host}:${config.port}"
        } else {
            "redis://${config.host}:${config.port}"
        }
    )
    private val connection: StatefulRedisConnection<String, String> = client.connect()
    private val commands = connection.sync()

    fun blacklist(accessToken: String, remainingTtlSeconds: Long) {
        if (remainingTtlSeconds > 0) {
            commands.setex("blacklist:$accessToken", remainingTtlSeconds, "1")
        }
    }

    fun isBlacklisted(accessToken: String): Boolean {
        return commands.get("blacklist:$accessToken") != null
    }

    fun close() {
        connection.close()
        client.shutdown()
    }
}
