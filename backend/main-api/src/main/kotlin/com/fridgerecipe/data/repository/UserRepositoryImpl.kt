package com.fridgerecipe.data.repository

import com.fridgerecipe.data.database.tables.Users
import com.fridgerecipe.domain.model.User
import com.fridgerecipe.domain.repository.UserRepository
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserRepositoryImpl : UserRepository {
    private fun ResultRow.toUser() = User(
        id = this[Users.id].value,
        email = this[Users.email],
        nickname = this[Users.nickname],
        profileImage = this[Users.profileImage],
        oauthProvider = this[Users.oauthProvider],
        oauthId = this[Users.oauthId],
        dietaryPrefs = Json.encodeToString(JsonObject.serializer(), this[Users.dietaryPrefs]),
        createdAt = this[Users.createdAt],
        updatedAt = this[Users.updatedAt]
    )

    override suspend fun findById(id: Long): User? = newSuspendedTransaction {
        Users.selectAll().where { Users.id eq id }.singleOrNull()?.toUser()
    }

    override suspend fun findByOAuth(provider: String, oauthId: String): User? = newSuspendedTransaction {
        Users.selectAll()
            .where { (Users.oauthProvider eq provider) and (Users.oauthId eq oauthId) }
            .singleOrNull()?.toUser()
    }

    override suspend fun create(user: User): User = newSuspendedTransaction {
        val dietaryPrefsObj = Json.decodeFromString(JsonObject.serializer(), user.dietaryPrefs)
        val id = Users.insertAndGetId {
            it[email] = user.email
            it[nickname] = user.nickname
            it[profileImage] = user.profileImage
            it[oauthProvider] = user.oauthProvider
            it[oauthId] = user.oauthId
            it[dietaryPrefs] = dietaryPrefsObj
        }
        user.copy(id = id.value)
    }

    override suspend fun update(user: User): User? = newSuspendedTransaction {
        val dietaryPrefsObj = Json.decodeFromString(JsonObject.serializer(), user.dietaryPrefs)
        val updated = Users.update({ Users.id eq user.id }) {
            it[email] = user.email
            it[nickname] = user.nickname
            it[profileImage] = user.profileImage
            it[dietaryPrefs] = dietaryPrefsObj
        }
        if (updated > 0) findById(user.id) else null
    }

    override suspend fun delete(id: Long): Boolean = newSuspendedTransaction {
        Users.deleteWhere { Users.id eq id } > 0
    }
}
