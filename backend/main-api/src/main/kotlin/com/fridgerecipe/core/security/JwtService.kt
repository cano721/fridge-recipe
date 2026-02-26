package com.fridgerecipe.core.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.fridgerecipe.core.config.AppConfig
import java.security.MessageDigest
import java.util.*

class JwtService(private val config: AppConfig.JwtConfig) {
    private val algorithm = Algorithm.HMAC256(config.secret)

    val verifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(config.issuer)
        .withAudience(config.audience)
        .build()

    fun generateAccessToken(userId: Long, nickname: String): String {
        return JWT.create()
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .withClaim("userId", userId)
            .withClaim("nickname", nickname)
            .withExpiresAt(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
            .sign(algorithm)
    }

    fun generateRefreshToken(): String {
        return UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString()
    }

    fun hashRefreshToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(token.toByteArray()).joinToString("") { "%02x".format(it) }
    }

    fun getUserIdFromToken(token: String): Long? {
        return try {
            val decoded = verifier.verify(token)
            decoded.getClaim("userId").asLong()
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        const val ACCESS_TOKEN_EXPIRY = 30L * 60 * 1000 // 30분
        const val REFRESH_TOKEN_EXPIRY = 14L * 24 * 60 * 60 * 1000 // 14일
        const val MAX_REFRESH_TOKENS_PER_USER = 5
    }
}
