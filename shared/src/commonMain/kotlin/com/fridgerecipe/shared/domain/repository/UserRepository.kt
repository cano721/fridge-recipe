package com.fridgerecipe.shared.domain.repository

import com.fridgerecipe.shared.data.remote.dto.UpdatePreferencesRequest
import com.fridgerecipe.shared.data.remote.dto.UpdateProfileRequest
import com.fridgerecipe.shared.data.remote.dto.UserProfileResponse
import com.fridgerecipe.shared.domain.model.User

interface UserRepository {
    suspend fun getMyProfile(): Result<UserProfileResponse>
    suspend fun updateProfile(request: UpdateProfileRequest): Result<User>
    suspend fun updatePreferences(request: UpdatePreferencesRequest): Result<User>
    suspend fun deleteAccount(): Result<Unit>
}
