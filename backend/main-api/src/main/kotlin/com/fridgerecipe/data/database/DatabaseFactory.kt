package com.fridgerecipe.data.database

import com.fridgerecipe.core.config.AppConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    private lateinit var dataSource: HikariDataSource

    fun init(config: AppConfig) {
        val hikariConfig = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = "jdbc:postgresql://${config.db.host}:${config.db.port}/${config.db.name}"
            username = config.db.user
            password = config.db.password
            maximumPoolSize = config.db.maxPoolSize
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        dataSource = HikariDataSource(hikariConfig)
        Database.connect(dataSource)
    }

    fun ping(): Boolean {
        return transaction {
            exec("SELECT 1") { it.next() }
            true
        }
    }
}
