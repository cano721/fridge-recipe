package com.fridgerecipe.data.repository

import com.fridgerecipe.data.database.tables.Bookmarks
import com.fridgerecipe.domain.repository.BookmarkRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class BookmarkRepositoryImpl : BookmarkRepository {

    override suspend fun add(userId: Long, recipeId: Long) = newSuspendedTransaction {
        val exists = Bookmarks.selectAll()
            .where { (Bookmarks.userId eq userId) and (Bookmarks.recipeId eq recipeId) }
            .count() > 0
        if (!exists) {
            Bookmarks.insert {
                it[Bookmarks.userId] = userId
                it[Bookmarks.recipeId] = recipeId
            }
        }
        Unit
    }

    override suspend fun remove(userId: Long, recipeId: Long) = newSuspendedTransaction {
        Bookmarks.deleteWhere {
            (Bookmarks.userId eq userId) and (Bookmarks.recipeId eq recipeId)
        }
        Unit
    }

    override suspend fun isBookmarked(userId: Long, recipeId: Long): Boolean = newSuspendedTransaction {
        Bookmarks.selectAll()
            .where { (Bookmarks.userId eq userId) and (Bookmarks.recipeId eq recipeId) }
            .count() > 0
    }

    override suspend fun findByUserId(userId: Long, page: Int, size: Int): Pair<List<Long>, Long> = newSuspendedTransaction {
        val total = Bookmarks.selectAll().where { Bookmarks.userId eq userId }.count()
        val ids = Bookmarks.selectAll()
            .where { Bookmarks.userId eq userId }
            .orderBy(Bookmarks.createdAt, org.jetbrains.exposed.sql.SortOrder.DESC)
            .limit(size).offset(((page - 1) * size).toLong())
            .map { it[Bookmarks.recipeId] }
        Pair(ids, total)
    }
}
