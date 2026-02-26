package com.fridgerecipe.auth

class OAuthService {
    private val clients = mapOf(
        OAuthProvider.KAKAO to KakaoOAuthClient(),
        OAuthProvider.GOOGLE to GoogleOAuthClient(),
        OAuthProvider.APPLE to AppleOAuthClient()
    )

    suspend fun authenticate(provider: OAuthProvider, accessToken: String): OAuthUserInfo {
        val client = clients[provider] ?: throw IllegalArgumentException("지원하지 않는 프로바이더: $provider")
        return client.getUserInfo(accessToken)
    }
}
