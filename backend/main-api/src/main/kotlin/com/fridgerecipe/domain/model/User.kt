package com.fridgerecipe.domain.model

data class User(
    val id: Long = 0,
    val email: String? = null,
    val nickname: String,
    val profileImage: String? = null,
    val oauthProvider: String,
    val oauthId: String,
    val dietaryPrefs: String = "{}",
    val createdAt: String? = null,
    val updatedAt: String? = null
)
