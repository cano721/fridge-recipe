package com.fridgerecipe.domain.repository

interface BookmarkRepository {
    suspend fun add(userId: Long, recipeId: Long)
    suspend fun remove(userId: Long, recipeId: Long)
    suspend fun isBookmarked(userId: Long, recipeId: Long): Boolean
    suspend fun findByUserId(userId: Long, page: Int, size: Int): Pair<List<Long>, Long>
}
