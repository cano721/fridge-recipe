package com.fridgerecipe.auth

import com.fridgerecipe.data.database.UsersTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
)

class AuthService {
    fun loginOrRegister(provider: String, oauthId: String, email: String?, nickname: String): TokenPair {
        val userId = transaction {
            val existing = UsersTable.selectAll()
                .where { (UsersTable.oauthProvider eq provider) and (UsersTable.oauthId eq oauthId) }
                .firstOrNull()

            existing?.get(UsersTable.id)?.value ?: UsersTable.insertAndGetId {
                it[UsersTable.email] = email
                it[UsersTable.nickname] = nickname
                it[oauthProvider] = provider
                it[UsersTable.oauthId] = oauthId
            }.value
        }

        return TokenPair(
            accessToken = JwtConfig.generateAccessToken(userId),
            refreshToken = JwtConfig.generateRefreshToken(userId),
            expiresIn = 1800,
        )
    }
}
