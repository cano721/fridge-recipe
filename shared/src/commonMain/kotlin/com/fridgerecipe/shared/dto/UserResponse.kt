package com.fridgerecipe.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Long,
    val email: String?,
    val nickname: String,
    val profileImage: String?,
    val oauthProvider: String
)
