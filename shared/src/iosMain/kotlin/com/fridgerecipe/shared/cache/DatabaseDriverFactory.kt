package com.fridgerecipe.shared.cache

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.cash.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(FridgeRecipeDatabase.Schema, "fridge_recipe.db")
    }
}
