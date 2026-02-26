package com.fridgerecipe.data.database.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object RefreshTokens : LongIdTable("refresh_tokens") {
    val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val tokenHash = varchar("token_hash", 64).uniqueIndex()
    val deviceInfo = varchar("device_info", 200).nullable()
    val expiresAt = text("expires_at")
    val createdAt = text("created_at").default("NOW()")
    val revokedAt = text("revoked_at").nullable()

    init {
        index(false, userId)
    }
}
