package com.fridgerecipe.data.repository

import com.fridgerecipe.data.database.tables.RefreshTokens
import com.fridgerecipe.domain.model.RefreshTokenInfo
import com.fridgerecipe.domain.repository.RefreshTokenRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.OffsetDateTime

class RefreshTokenRepositoryImpl : RefreshTokenRepository {
    private fun ResultRow.toInfo() = RefreshTokenInfo(
        id = this[RefreshTokens.id].value,
        userId = this[RefreshTokens.userId],
        tokenHash = this[RefreshTokens.tokenHash],
        deviceInfo = this[RefreshTokens.deviceInfo],
        expiresAt = this[RefreshTokens.expiresAt],
        createdAt = this[RefreshTokens.createdAt],
        revokedAt = this[RefreshTokens.revokedAt]
    )

    override suspend fun findByTokenHash(hash: String): RefreshTokenInfo? = newSuspendedTransaction {
        RefreshTokens.selectAll()
            .where { (RefreshTokens.tokenHash eq hash) and RefreshTokens.revokedAt.isNull() }
            .singleOrNull()?.toInfo()
    }

    override suspend fun findActiveByUserId(userId: Long): List<RefreshTokenInfo> = newSuspendedTransaction {
        RefreshTokens.selectAll()
            .where { (RefreshTokens.userId eq userId) and RefreshTokens.revokedAt.isNull() }
            .map { it.toInfo() }
    }

    override suspend fun create(token: RefreshTokenInfo): RefreshTokenInfo = newSuspendedTransaction {
        val id = RefreshTokens.insertAndGetId {
            it[userId] = token.userId
            it[tokenHash] = token.tokenHash
            it[deviceInfo] = token.deviceInfo
            it[expiresAt] = token.expiresAt
        }
        token.copy(id = id.value)
    }

    override suspend fun revoke(id: Long): Boolean = newSuspendedTransaction {
        RefreshTokens.update({ RefreshTokens.id eq id }) {
            it[revokedAt] = OffsetDateTime.now().toString()
        } > 0
    }

    override suspend fun revokeAllByUserId(userId: Long): Int = newSuspendedTransaction {
        RefreshTokens.update({ (RefreshTokens.userId eq userId) and RefreshTokens.revokedAt.isNull() }) {
            it[revokedAt] = OffsetDateTime.now().toString()
        }
    }

    override suspend fun deleteExpiredOrRevoked(): Int = newSuspendedTransaction {
        val now = OffsetDateTime.now().toString()
        RefreshTokens.deleteWhere {
            revokedAt.isNotNull() or (expiresAt less now)
        }
    }

    override suspend fun countActiveByUserId(userId: Long): Long = newSuspendedTransaction {
        RefreshTokens.selectAll()
            .where { (RefreshTokens.userId eq userId) and RefreshTokens.revokedAt.isNull() }
            .count()
    }
}
