package com.fridgerecipe.data.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ReferenceOption

object Bookmarks : Table("bookmarks") {
    val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val recipeId = long("recipe_id").references(Recipes.id, onDelete = ReferenceOption.CASCADE)
    val createdAt = text("created_at").default("NOW()")

    override val primaryKey = PrimaryKey(userId, recipeId)
}
