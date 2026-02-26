package com.fridgerecipe.data.database.tables

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.json.jsonb

object ScanHistory : LongIdTable("scan_history") {
    val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val scanType = varchar("scan_type", 20)
    val imageUrl = varchar("image_url", 500).nullable()
    val status = varchar("status", 20).default("processing")
    val result = jsonb(
        "result",
        { obj: JsonObject -> Json.encodeToString(JsonObject.serializer(), obj) },
        { str: String -> Json.decodeFromString(JsonObject.serializer(), str) }
    ).nullable()
    val createdAt = text("created_at").default("NOW()")
}
