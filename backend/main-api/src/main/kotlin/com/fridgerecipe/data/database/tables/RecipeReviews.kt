package com.fridgerecipe.data.database.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object RecipeReviews : LongIdTable("recipe_reviews") {
    val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val recipeId = long("recipe_id").references(Recipes.id, onDelete = ReferenceOption.CASCADE)
    val rating = short("rating")
    val comment = text("comment").nullable()
    val createdAt = text("created_at").default("NOW()")

    init {
        uniqueIndex("uq_user_recipe_review", userId, recipeId)
        index(false, recipeId)
    }
}
