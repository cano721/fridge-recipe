package com.fridgerecipe.shared.cache

class CacheRepository(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = FridgeRecipeDatabase(databaseDriverFactory.createDriver())
    private val queries = database.fridgeRecipeDatabaseQueries

    // Ingredient cache
    fun getAllIngredients() = queries.selectAllIngredients().executeAsList()

    fun getIngredientsByStorage(storageType: String) =
        queries.selectIngredientsByStorage(storageType).executeAsList()

    fun getExpiringIngredients(beforeDate: String) =
        queries.selectExpiringIngredients(beforeDate).executeAsList()

    fun cacheIngredient(
        id: Long,
        ingredientId: Long,
        name: String,
        category: String?,
        quantity: Double?,
        unit: String?,
        expiryDate: String?,
        storageType: String,
        memo: String?,
        registeredVia: String,
        synced: Long,
        updatedAt: String
    ) = queries.insertIngredient(
        id, ingredientId, name, category, quantity, unit,
        expiryDate, storageType, memo, registeredVia, synced, updatedAt
    )

    fun deleteIngredient(id: Long) = queries.deleteIngredient(id)

    fun clearIngredients() = queries.clearIngredients()

    // Recipe cache
    fun getAllRecipes() = queries.selectAllRecipes().executeAsList()

    fun getRecipesByType(cuisineType: String) =
        queries.selectRecipesByType(cuisineType).executeAsList()

    fun cacheRecipe(
        id: Long,
        title: String,
        description: String?,
        thumbnailUrl: String?,
        cookingTime: Long,
        difficulty: String,
        cuisineType: String?,
        avgRating: Double,
        tags: String?,
        synced: Long,
        updatedAt: String
    ) = queries.insertRecipe(
        id, title, description, thumbnailUrl, cookingTime,
        difficulty, cuisineType, avgRating, tags, synced, updatedAt
    )

    fun clearRecipes() = queries.clearRecipes()

    // Sync queue
    fun getPendingSyncs(limit: Long) =
        queries.selectPendingSyncs(limit).executeAsList()

    fun addSyncItem(
        entityType: String,
        entityId: Long,
        action: String,
        payload: String,
        createdAt: String
    ) = queries.insertSyncItem(entityType, entityId, action, payload, createdAt)

    fun removeSyncItem(id: Long) = queries.deleteSyncItem(id)

    fun incrementSyncRetry(id: Long) = queries.incrementRetry(id)

    // Bookmarks
    fun getBookmarks(userId: Long) =
        queries.selectBookmarks(userId).executeAsList()

    fun addBookmark(userId: Long, recipeId: Long, createdAt: String) =
        queries.insertBookmark(userId, recipeId, createdAt)

    fun removeBookmark(userId: Long, recipeId: Long) =
        queries.deleteBookmark(userId, recipeId)
}
