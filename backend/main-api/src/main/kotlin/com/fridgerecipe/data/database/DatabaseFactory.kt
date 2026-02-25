package com.fridgerecipe.data.database

import com.fridgerecipe.core.config.AppConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val dataSource = hikari()
        Database.connect(dataSource)

        transaction {
            SchemaUtils.create(
                UsersTable,
                IngredientMasterTable,
                UserIngredientsTable,
                RecipesTable,
                RecipeIngredientsTable,
                BookmarksTable,
                ScanHistoryTable,
                NotificationSettingsTable,
                RecipeReviewsTable,
                UserCookingHistoryTable,
                DeviceTokensTable,
            )
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            jdbcUrl = AppConfig.dbUrl
            username = AppConfig.dbUser
            password = AppConfig.dbPassword
            maximumPoolSize = AppConfig.dbMaxPoolSize
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }
}
