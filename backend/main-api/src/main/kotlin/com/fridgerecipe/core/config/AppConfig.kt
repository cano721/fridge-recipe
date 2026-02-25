package com.fridgerecipe.core.config

object AppConfig {
    val serverPort: Int get() = env("SERVER_PORT", "8080").toInt()

    // Database
    val dbUrl: String get() = env("DB_URL", "jdbc:postgresql://localhost:5432/fridgerecipe")
    val dbUser: String get() = env("DB_USER", "postgres")
    val dbPassword: String get() = env("DB_PASSWORD", "postgres")
    val dbMaxPoolSize: Int get() = env("DB_MAX_POOL_SIZE", "10").toInt()

    // Redis
    val redisUrl: String get() = env("REDIS_URL", "redis://localhost:6379")

    // JWT
    val jwtSecret: String get() = env("JWT_SECRET", "dev-secret-change-in-production")
    val jwtIssuer: String get() = env("JWT_ISSUER", "fridge-recipe")
    val jwtAudience: String get() = env("JWT_AUDIENCE", "fridge-recipe-app")
    val jwtAccessExpireMinutes: Long get() = env("JWT_ACCESS_EXPIRE_MINUTES", "30").toLong()
    val jwtRefreshExpireDays: Long get() = env("JWT_REFRESH_EXPIRE_DAYS", "14").toLong()

    // AI Service
    val aiServiceUrl: String get() = env("AI_SERVICE_URL", "http://localhost:8000")
    val aiServiceApiKey: String get() = env("AI_SERVICE_API_KEY", "dev-internal-key")

    private fun env(key: String, default: String): String =
        System.getenv(key) ?: default
}
