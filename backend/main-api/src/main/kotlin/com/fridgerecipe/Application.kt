package com.fridgerecipe

import com.fridgerecipe.core.config.AppConfig
import com.fridgerecipe.core.di.appModule
import com.fridgerecipe.data.database.DatabaseFactory
import com.fridgerecipe.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    embeddedServer(
        Netty,
        port = AppConfig.serverPort,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    DatabaseFactory.init()

    configureSerialization()
    configureAuthentication()
    configureCORS()
    configureStatusPages()
    configureCallLogging()
    configureRouting()
}
