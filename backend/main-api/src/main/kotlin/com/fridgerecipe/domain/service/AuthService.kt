package com.fridgerecipe.domain.service

import com.fridgerecipe.auth.OAuthProvider
import com.fridgerecipe.auth.OAuthService
import com.fridgerecipe.core.security.JwtService
import com.fridgerecipe.core.security.TokenBlacklist
import com.fridgerecipe.domain.model.RefreshTokenInfo
import com.fridgerecipe.domain.model.User
import com.fridgerecipe.domain.repository.RefreshTokenRepository
import com.fridgerecipe.domain.repository.UserRepository
import java.time.OffsetDateTime

data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long = JwtService.ACCESS_TOKEN_EXPIRY / 1000
)

class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtService: JwtService,
    private val tokenBlacklist: TokenBlacklist,
    private val oAuthService: OAuthService
) {
    suspend fun login(provider: String, oauthAccessToken: String, deviceInfo: String?): TokenPair {
        val oauthProvider = OAuthProvider.from(provider)
        val oauthUserInfo = oAuthService.authenticate(oauthProvider, oauthAccessToken)

        val user = userRepository.findByOAuth(oauthUserInfo.provider.providerId, oauthUserInfo.oauthId)
            ?: userRepository.create(User(
                nickname = oauthUserInfo.nickname,
                email = oauthUserInfo.email,
                profileImage = oauthUserInfo.profileImage,
                oauthProvider = oauthUserInfo.provider.providerId,
                oauthId = oauthUserInfo.oauthId
            ))

        return generateTokenPair(user, deviceInfo)
    }

    suspend fun refresh(refreshToken: String, deviceInfo: String?): TokenPair {
        val tokenHash = jwtService.hashRefreshToken(refreshToken)
        val tokenInfo = refreshTokenRepository.findByTokenHash(tokenHash)
            ?: throw IllegalArgumentException("AUTH_REFRESH_REVOKED")

        refreshTokenRepository.revoke(tokenInfo.id)

        val user = userRepository.findById(tokenInfo.userId)
            ?: throw IllegalArgumentException("USER_NOT_FOUND")

        return generateTokenPair(user, deviceInfo)
    }

    suspend fun logout(accessToken: String, userId: Long) {
        val remainingSeconds = try {
            val decoded = jwtService.verifier.verify(accessToken)
            (decoded.expiresAt.time - System.currentTimeMillis()) / 1000
        } catch (e: Exception) { 0L }

        if (remainingSeconds > 0) {
            tokenBlacklist.blacklist(accessToken, remainingSeconds)
        }

        refreshTokenRepository.revokeAllByUserId(userId)
    }

    private suspend fun generateTokenPair(user: User, deviceInfo: String?): TokenPair {
        val accessToken = jwtService.generateAccessToken(user.id, user.nickname)
        val refreshToken = jwtService.generateRefreshToken()
        val tokenHash = jwtService.hashRefreshToken(refreshToken)

        val activeCount = refreshTokenRepository.countActiveByUserId(user.id)
        if (activeCount >= JwtService.MAX_REFRESH_TOKENS_PER_USER) {
            val activeTokens = refreshTokenRepository.findActiveByUserId(user.id)
                .sortedBy { it.createdAt }
            val toRevoke = activeTokens.take((activeCount - JwtService.MAX_REFRESH_TOKENS_PER_USER + 1).toInt())
            toRevoke.forEach { refreshTokenRepository.revoke(it.id) }
        }

        refreshTokenRepository.create(RefreshTokenInfo(
            userId = user.id,
            tokenHash = tokenHash,
            deviceInfo = deviceInfo,
            expiresAt = OffsetDateTime.now().plusDays(14).toString()
        ))

        return TokenPair(accessToken = accessToken, refreshToken = refreshToken)
    }
}
