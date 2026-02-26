package com.fridgerecipe.shared.cache

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(FridgeRecipeDatabase.Schema, context, "fridge_recipe.db")
    }
}
