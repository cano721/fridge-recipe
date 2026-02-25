package com.fridgerecipe.data.database

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object UsersTable : LongIdTable("users") {
    val email = varchar("email", 255).uniqueIndex().nullable()
    val nickname = varchar("nickname", 50)
    val profileImage = varchar("profile_image", 500).nullable()
    val oauthProvider = varchar("oauth_provider", 20)
    val oauthId = varchar("oauth_id", 255)
    val dietaryPrefs = text("dietary_prefs").default("{}")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)

    init {
        uniqueIndex("uq_oauth", oauthProvider, oauthId)
    }
}

object IngredientMasterTable : LongIdTable("ingredient_master") {
    val name = varchar("name", 100).uniqueIndex()
    val category = varchar("category", 50)
    val iconUrl = varchar("icon_url", 500).nullable()
    val defaultUnit = varchar("default_unit", 20).nullable()
    val defaultExpiryDays = integer("default_expiry_days").nullable()
    val aliases = text("aliases").default("[]")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
}

object UserIngredientsTable : LongIdTable("user_ingredients") {
    val userId = long("user_id").references(UsersTable.id)
    val ingredientId = long("ingredient_id").references(IngredientMasterTable.id)
    val quantity = decimal("quantity", 10, 2).nullable()
    val unit = varchar("unit", 20).nullable()
    val expiryDate = date("expiry_date").nullable()
    val storageType = varchar("storage_type", 10).default("fridge")
    val memo = varchar("memo", 200).nullable()
    val registeredVia = varchar("registered_via", 20).default("manual")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)

    init {
        index(false, userId)
        index(false, userId, expiryDate)
    }
}

object RecipesTable : LongIdTable("recipes") {
    val title = varchar("title", 200)
    val description = text("description").nullable()
    val cuisineType = varchar("cuisine_type", 50).nullable()
    val difficulty = varchar("difficulty", 10).nullable()
    val cookingTime = integer("cooking_time").nullable()
    val servings = integer("servings").default(2)
    val calories = integer("calories").nullable()
    val thumbnailUrl = varchar("thumbnail_url", 500).nullable()
    val steps = text("steps").default("[]")
    val nutrition = text("nutrition").nullable()
    val tags = text("tags").default("[]")
    val viewCount = integer("view_count").default(0)
    val avgRating = decimal("avg_rating", 2, 1).default(java.math.BigDecimal.ZERO)
    val sourceUrl = varchar("source_url", 500).nullable()
    val sourceType = varchar("source_type", 20).default("manual")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)

    init {
        index(false, cuisineType)
    }
}

object RecipeIngredientsTable : LongIdTable("recipe_ingredients") {
    val recipeId = long("recipe_id").references(RecipesTable.id)
    val ingredientId = long("ingredient_id").references(IngredientMasterTable.id)
    val quantity = varchar("quantity", 50).nullable()
    val isEssential = bool("is_essential").default(true)
    val substituteIds = text("substitute_ids").default("[]")

    init {
        index(false, recipeId)
        index(false, ingredientId)
    }
}

object BookmarksTable : Table("bookmarks") {
    val userId = long("user_id").references(UsersTable.id)
    val recipeId = long("recipe_id").references(RecipesTable.id)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(userId, recipeId)
}

object ScanHistoryTable : LongIdTable("scan_history") {
    val userId = long("user_id").references(UsersTable.id)
    val scanType = varchar("scan_type", 20)
    val imageUrl = varchar("image_url", 500).nullable()
    val status = varchar("status", 20).default("processing")
    val result = text("result").nullable()
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)

    init {
        index(false, userId)
    }
}

object NotificationSettingsTable : Table("notification_settings") {
    val userId = long("user_id").references(UsersTable.id)
    val expiryEnabled = bool("expiry_enabled").default(true)
    val expiryDays = text("expiry_days").default("[3, 1]")
    val themePreference = varchar("theme_preference", 10).default("system")
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(userId)
}

object RecipeReviewsTable : LongIdTable("recipe_reviews") {
    val userId = long("user_id").references(UsersTable.id)
    val recipeId = long("recipe_id").references(RecipesTable.id)
    val rating = short("rating")
    val comment = text("comment").nullable()
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)

    init {
        uniqueIndex("uq_user_recipe_review", userId, recipeId)
        index(false, recipeId)
    }
}

object UserCookingHistoryTable : LongIdTable("user_cooking_history") {
    val userId = long("user_id").references(UsersTable.id)
    val recipeId = long("recipe_id").references(RecipesTable.id).nullable()
    val cookedAt = timestamp("cooked_at").defaultExpression(CurrentTimestamp)
    val usedIngredients = text("used_ingredients").default("[]")

    init {
        index(false, userId)
    }
}

object DeviceTokensTable : LongIdTable("device_tokens") {
    val userId = long("user_id").references(UsersTable.id)
    val token = varchar("token", 500).uniqueIndex()
    val deviceType = varchar("device_type", 10)
    val isActive = bool("is_active").default(true)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)

    init {
        index(false, userId)
    }
}
