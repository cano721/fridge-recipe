package com.fridgerecipe.shared.data.remote.dto

import com.fridgerecipe.shared.domain.model.DietaryPrefs
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponse(
    val id: Long,
    val email: String?,
    val nickname: String,
    val profileImage: String?,
    val dietaryPrefs: DietaryPrefs,
    val ingredientCount: Int,
    val bookmarkCount: Int,
    val cookingCount: Int,
)

@Serializable
data class UpdateProfileRequest(
    val nickname: String? = null,
    val profileImage: String? = null,
)

@Serializable
data class UpdatePreferencesRequest(
    val dietaryPrefs: DietaryPrefs,
)
