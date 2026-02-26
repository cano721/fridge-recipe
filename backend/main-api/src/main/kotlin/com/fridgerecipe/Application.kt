package com.fridgerecipe

import com.fridgerecipe.core.config.AppConfig
import com.fridgerecipe.core.di.appModule
import com.fridgerecipe.core.scheduler.ExpiryNotificationScheduler
import com.fridgerecipe.data.database.DatabaseFactory
import com.fridgerecipe.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val config = AppConfig.load(environment)

    install(Koin) {
        modules(appModule(config))
    }

    DatabaseFactory.init(config)

    configureSerialization()
    configureCORS()
    configureStatusPages()
    configureAuthentication()
    configureRouting()

    val scheduler by inject<ExpiryNotificationScheduler>()
    scheduler.start(this)
}
