package com.fridgerecipe.shared.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val email: String?,
    val nickname: String,
    val profileImage: String?,
    val oauthProvider: OAuthProvider,
    val dietaryPrefs: DietaryPrefs = DietaryPrefs(),
    val createdAt: Instant? = null,
)

@Serializable
enum class OAuthProvider {
    KAKAO, GOOGLE, APPLE
}

@Serializable
data class DietaryPrefs(
    val vegetarian: Boolean = false,
    val vegan: Boolean = false,
    val halal: Boolean = false,
    val lowCarb: Boolean = false,
    val glutenFree: Boolean = false,
    val lowSodium: Boolean = false,
    val allergies: List<String> = emptyList(),
)
