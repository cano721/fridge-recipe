package com.fridgerecipe.shared.core.di

import com.fridgerecipe.shared.data.remote.api.ApiClient
import io.ktor.client.*
import org.koin.dsl.module

val sharedModule = module {
    single<HttpClient> { ApiClient.create() }
}
