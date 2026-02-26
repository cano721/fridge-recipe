package com.fridgerecipe.data.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ReferenceOption

object NotificationSettings : Table("notification_settings") {
    val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val expiryEnabled = bool("expiry_enabled").default(true)
    val expiryDays = intArray("expiry_days").default(listOf(3, 1, 0))
    val themePreference = varchar("theme_preference", 10).default("system")
    val updatedAt = text("updated_at").default("NOW()")

    override val primaryKey = PrimaryKey(userId)
}
