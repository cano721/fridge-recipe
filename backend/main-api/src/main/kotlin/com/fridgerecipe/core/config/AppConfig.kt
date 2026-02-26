package com.fridgerecipe.core.config

import io.ktor.server.application.*

data class AppConfig(
    val db: DbConfig,
    val redis: RedisConfig,
    val aiService: AiServiceConfig,
    val jwt: JwtConfig
) {
    data class DbConfig(
        val host: String,
        val port: Int,
        val name: String,
        val user: String,
        val password: String,
        val maxPoolSize: Int = 10
    )

    data class RedisConfig(
        val host: String,
        val port: Int,
        val password: String
    )

    data class AiServiceConfig(
        val url: String,
        val apiKey: String
    )

    data class JwtConfig(
        val secret: String,
        val issuer: String,
        val audience: String
    )

    companion object {
        fun load(environment: ApplicationEnvironment): AppConfig {
            fun env(name: String, default: String = ""): String =
                System.getenv(name) ?: default

            return AppConfig(
                db = DbConfig(
                    host = env("DB_HOST", "localhost"),
                    port = env("DB_PORT", "5432").toInt(),
                    name = env("DB_NAME", "fridge_recipe"),
                    user = env("DB_USER", "fridge"),
                    password = env("DB_PASSWORD", "fridge1234"),
                    maxPoolSize = env("DB_MAX_POOL_SIZE", "10").toInt()
                ),
                redis = RedisConfig(
                    host = env("REDIS_HOST", "localhost"),
                    port = env("REDIS_PORT", "6379").toInt(),
                    password = env("REDIS_PASSWORD", "redis1234")
                ),
                aiService = AiServiceConfig(
                    url = env("AI_SERVICE_URL", "http://localhost:8000"),
                    apiKey = env("AI_SERVICE_API_KEY", "dev-internal-key")
                ),
                jwt = JwtConfig(
                    secret = env("JWT_SECRET", "dev-jwt-secret"),
                    issuer = env("JWT_ISSUER", "fridge-recipe"),
                    audience = env("JWT_AUDIENCE", "fridge-recipe-app")
                )
            )
        }
    }
}
