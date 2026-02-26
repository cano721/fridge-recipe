package com.fridgerecipe.data.database.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object UserIngredients : LongIdTable("user_ingredients") {
    val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val ingredientId = long("ingredient_id").references(IngredientMaster.id)
    val quantity = decimal("quantity", 10, 2).nullable()
    val unit = varchar("unit", 20).nullable()
    val expiryDate = varchar("expiry_date", 20).nullable()
    val storageType = varchar("storage_type", 10).default("fridge")
    val memo = varchar("memo", 200).nullable()
    val registeredVia = varchar("registered_via", 20).default("manual")
    val expiredNotified = bool("expired_notified").default(false)
    val createdAt = text("created_at").default("NOW()")
    val updatedAt = text("updated_at").default("NOW()")

    init {
        index(false, userId)
        index(false, userId, expiryDate)
    }
}
