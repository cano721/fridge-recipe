package com.fridgerecipe.auth

data class OAuthUserInfo(
    val provider: OAuthProvider,
    val oauthId: String,
    val email: String? = null,
    val nickname: String,
    val profileImage: String? = null
)
