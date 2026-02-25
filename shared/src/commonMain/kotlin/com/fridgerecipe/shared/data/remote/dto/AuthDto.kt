package com.fridgerecipe.shared.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val provider: String,
    val accessToken: String,
)

@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
)

@Serializable
data class RefreshRequest(
    val refreshToken: String,
)
