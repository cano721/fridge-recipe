package com.fridgerecipe.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fridgerecipe.core.config.AppConfig
import java.util.*

object JwtConfig {
    private val algorithm = Algorithm.HMAC256(AppConfig.jwtSecret)

    fun generateAccessToken(userId: Long): String {
        return JWT.create()
            .withIssuer(AppConfig.jwtIssuer)
            .withAudience(AppConfig.jwtAudience)
            .withClaim("userId", userId)
            .withClaim("type", "access")
            .withExpiresAt(
                Date(System.currentTimeMillis() + AppConfig.jwtAccessExpireMinutes * 60 * 1000)
            )
            .sign(algorithm)
    }

    fun generateRefreshToken(userId: Long): String {
        return JWT.create()
            .withIssuer(AppConfig.jwtIssuer)
            .withAudience(AppConfig.jwtAudience)
            .withClaim("userId", userId)
            .withClaim("type", "refresh")
            .withExpiresAt(
                Date(System.currentTimeMillis() + AppConfig.jwtRefreshExpireDays * 24 * 60 * 60 * 1000)
            )
            .sign(algorithm)
    }
}
