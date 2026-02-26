package com.fridgerecipe.domain.repository

import com.fridgerecipe.domain.model.User

interface UserRepository {
    suspend fun findById(id: Long): User?
    suspend fun findByOAuth(provider: String, oauthId: String): User?
    suspend fun create(user: User): User
    suspend fun update(user: User): User?
    suspend fun delete(id: Long): Boolean
}
