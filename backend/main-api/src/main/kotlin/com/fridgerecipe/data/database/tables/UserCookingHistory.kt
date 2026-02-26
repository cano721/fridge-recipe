package com.fridgerecipe.data.database.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object UserCookingHistory : LongIdTable("user_cooking_history") {
    val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val recipeId = long("recipe_id").references(Recipes.id, onDelete = ReferenceOption.SET_NULL).nullable()
    val cookedAt = text("cooked_at").default("NOW()")
    val usedIngredients = longArray("used_ingredients").default(emptyList())

    init {
        index(false, userId)
    }
}
