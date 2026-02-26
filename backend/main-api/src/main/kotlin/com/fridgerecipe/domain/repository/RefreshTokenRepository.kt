package com.fridgerecipe.domain.repository

import com.fridgerecipe.domain.model.RefreshTokenInfo

interface RefreshTokenRepository {
    suspend fun findByTokenHash(hash: String): RefreshTokenInfo?
    suspend fun findActiveByUserId(userId: Long): List<RefreshTokenInfo>
    suspend fun create(token: RefreshTokenInfo): RefreshTokenInfo
    suspend fun revoke(id: Long): Boolean
    suspend fun revokeAllByUserId(userId: Long): Int
    suspend fun deleteExpiredOrRevoked(): Int
    suspend fun countActiveByUserId(userId: Long): Long
}
