package com.fridgerecipe.domain.service

import com.fridgerecipe.domain.model.User
import com.fridgerecipe.domain.repository.UserRepository

class UserService(private val userRepository: UserRepository) {
    suspend fun getUser(id: Long): User =
        userRepository.findById(id) ?: throw IllegalArgumentException("USER_NOT_FOUND")

    suspend fun updateProfile(id: Long, nickname: String?, profileImage: String?): User {
        val user = getUser(id)
        val updated = user.copy(
            nickname = nickname ?: user.nickname,
            profileImage = profileImage ?: user.profileImage
        )
        return userRepository.update(updated) ?: throw IllegalArgumentException("USER_NOT_FOUND")
    }

    suspend fun updatePreferences(id: Long, dietaryPrefs: String): User {
        val user = getUser(id)
        val updated = user.copy(dietaryPrefs = dietaryPrefs)
        return userRepository.update(updated) ?: throw IllegalArgumentException("USER_NOT_FOUND")
    }

    suspend fun deleteAccount(id: Long): Boolean = userRepository.delete(id)
}
