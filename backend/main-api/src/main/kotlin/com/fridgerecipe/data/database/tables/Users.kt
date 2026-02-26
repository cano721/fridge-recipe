package com.fridgerecipe.data.database.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.json.jsonb
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

object Users : LongIdTable("users") {
    val email = varchar("email", 255).nullable()
    val nickname = varchar("nickname", 50)
    val profileImage = varchar("profile_image", 500).nullable()
    val oauthProvider = varchar("oauth_provider", 20)
    val oauthId = varchar("oauth_id", 255)
    val dietaryPrefs = jsonb(
        "dietary_prefs",
        { obj: JsonObject -> Json.encodeToString(JsonObject.serializer(), obj) },
        { str: String -> Json.decodeFromString(JsonObject.serializer(), str) }
    ).default(JsonObject(emptyMap()))
    val createdAt = text("created_at").default("NOW()")
    val updatedAt = text("updated_at").default("NOW()")

    init {
        uniqueIndex("uq_users_oauth", oauthProvider, oauthId)
    }
}
