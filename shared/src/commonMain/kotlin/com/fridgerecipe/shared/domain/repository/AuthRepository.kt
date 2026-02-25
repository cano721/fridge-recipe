package com.fridgerecipe.shared.domain.repository

import com.fridgerecipe.shared.data.remote.dto.TokenResponse
import com.fridgerecipe.shared.domain.model.OAuthProvider

interface AuthRepository {
    suspend fun login(provider: OAuthProvider, accessToken: String): Result<TokenResponse>
    suspend fun refreshToken(refreshToken: String): Result<TokenResponse>
    suspend fun logout(): Result<Unit>
    suspend fun getStoredToken(): String?
    suspend fun saveToken(tokenResponse: TokenResponse)
    suspend fun clearToken()
}
