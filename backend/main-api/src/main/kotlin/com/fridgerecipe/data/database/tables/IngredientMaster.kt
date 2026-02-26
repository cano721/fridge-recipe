package com.fridgerecipe.data.database.tables

import org.jetbrains.exposed.dao.id.LongIdTable

object IngredientMaster : LongIdTable("ingredient_master") {
    val name = varchar("name", 100).uniqueIndex()
    val category = varchar("category", 50)
    val iconUrl = varchar("icon_url", 500).nullable()
    val defaultUnit = varchar("default_unit", 20).nullable()
    val defaultExpiryDays = integer("default_expiry_days").nullable()
    val aliases = textArray("aliases").default(emptyList())
    val createdAt = text("created_at").default("NOW()")
}
