package com.fridgerecipe.data.database.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object RecipeIngredients : LongIdTable("recipe_ingredients") {
    val recipeId = long("recipe_id").references(Recipes.id, onDelete = ReferenceOption.CASCADE)
    val ingredientId = long("ingredient_id").references(IngredientMaster.id)
    val quantity = varchar("quantity", 50).nullable()
    val isEssential = bool("is_essential").default(true)
    val substituteIds = longArray("substitute_ids").default(emptyList())

    init {
        uniqueIndex("uq_recipe_ingredient", recipeId, ingredientId)
        index(false, recipeId)
        index(false, ingredientId)
    }
}
