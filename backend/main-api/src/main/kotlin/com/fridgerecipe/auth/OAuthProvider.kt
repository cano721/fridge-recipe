package com.fridgerecipe.auth

enum class OAuthProvider(val providerId: String) {
    KAKAO("kakao"),
    GOOGLE("google"),
    APPLE("apple");

    companion object {
        fun from(value: String): OAuthProvider =
            entries.find { it.providerId == value }
                ?: throw IllegalArgumentException("지원하지 않는 OAuth 프로바이더: $value")
    }
}
