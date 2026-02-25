package com.fridgerecipe.shared.data.remote.api

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {
    private const val DEFAULT_BASE_URL = "http://10.0.2.2:8080"

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        prettyPrint = false
    }

    fun create(baseUrl: String = DEFAULT_BASE_URL, tokenProvider: suspend () -> String? = { null }): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(json)
            }

            install(Logging) {
                level = LogLevel.BODY
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 10_000
            }

            defaultRequest {
                url(baseUrl)
                contentType(ContentType.Application.Json)
                header("Accept", ContentType.Application.Json.toString())
            }
        }
    }
}
