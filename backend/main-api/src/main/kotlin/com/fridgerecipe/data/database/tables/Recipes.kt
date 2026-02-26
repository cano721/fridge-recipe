package com.fridgerecipe.data.database.tables

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.json.jsonb

object Recipes : LongIdTable("recipes") {
    val title = varchar("title", 200)
    val description = text("description").nullable()
    val cuisineType = varchar("cuisine_type", 50).nullable()
    val difficulty = varchar("difficulty", 10).nullable()
    val cookingTime = integer("cooking_time").nullable()
    val servings = integer("servings").default(2)
    val calories = integer("calories").nullable()
    val thumbnailUrl = varchar("thumbnail_url", 500).nullable()
    val steps = jsonb(
        "steps",
        { arr: JsonArray -> Json.encodeToString(JsonArray.serializer(), arr) },
        { str: String -> Json.decodeFromString(JsonArray.serializer(), str) }
    )
    val nutrition = jsonb(
        "nutrition",
        { obj: JsonObject -> Json.encodeToString(JsonObject.serializer(), obj) },
        { str: String -> Json.decodeFromString(JsonObject.serializer(), str) }
    ).nullable()
    val tags = textArray("tags").default(emptyList())
    val viewCount = integer("view_count").default(0)
    val avgRating = decimal("avg_rating", 2, 1).default(java.math.BigDecimal.ZERO)
    val sourceUrl = varchar("source_url", 500).nullable()
    val sourceType = varchar("source_type", 20).default("manual")
    val createdAt = text("created_at").default("NOW()")

    init {
        index(false, cuisineType)
    }
}
