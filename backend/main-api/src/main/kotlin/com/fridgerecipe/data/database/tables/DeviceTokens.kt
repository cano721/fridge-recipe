package com.fridgerecipe.data.database.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object DeviceTokens : LongIdTable("device_tokens") {
    val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val token = varchar("token", 500).uniqueIndex()
    val deviceType = varchar("device_type", 10)
    val isActive = bool("is_active").default(true)
    val createdAt = text("created_at").default("NOW()")
    val updatedAt = text("updated_at").default("NOW()")

    init {
        index(false, userId)
    }
}
