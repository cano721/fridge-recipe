package com.fridgerecipe.core.di

import com.fridgerecipe.auth.OAuthService
import com.fridgerecipe.core.client.AiServiceClient
import com.fridgerecipe.core.config.AppConfig
import com.fridgerecipe.core.middleware.RateLimiter
import com.fridgerecipe.core.security.JwtService
import com.fridgerecipe.core.security.TokenBlacklist
import com.fridgerecipe.core.scheduler.ExpiryNotificationScheduler
import com.fridgerecipe.data.repository.BookmarkRepositoryImpl
import com.fridgerecipe.data.repository.DeviceTokenRepositoryImpl
import com.fridgerecipe.data.repository.IngredientMasterRepositoryImpl
import com.fridgerecipe.data.repository.IngredientRepositoryImpl
import com.fridgerecipe.data.repository.NotificationRepositoryImpl
import com.fridgerecipe.data.repository.RecipeRepositoryImpl
import com.fridgerecipe.data.repository.RefreshTokenRepositoryImpl
import com.fridgerecipe.data.repository.ScanRepositoryImpl
import com.fridgerecipe.data.repository.UserRepositoryImpl
import com.fridgerecipe.domain.repository.BookmarkRepository
import com.fridgerecipe.domain.repository.DeviceTokenRepository
import com.fridgerecipe.domain.repository.IngredientMasterRepository
import com.fridgerecipe.domain.repository.IngredientRepository
import com.fridgerecipe.domain.repository.NotificationRepository
import com.fridgerecipe.domain.repository.RecipeRepository
import com.fridgerecipe.domain.repository.RefreshTokenRepository
import com.fridgerecipe.domain.repository.ScanRepository
import com.fridgerecipe.domain.repository.UserRepository
import com.fridgerecipe.domain.service.AuthService
import com.fridgerecipe.domain.service.IngredientService
import com.fridgerecipe.domain.service.NotificationService
import com.fridgerecipe.domain.service.RecipeService
import com.fridgerecipe.domain.service.ScanService
import com.fridgerecipe.domain.service.UserService
import org.koin.dsl.module

fun appModule(config: AppConfig) = module {
    single { config }
    single { config.db }
    single { config.redis }
    single { config.aiService }
    single { config.jwt }
    single { JwtService(get<AppConfig>().jwt) }
    single { TokenBlacklist(get<AppConfig>().redis) }
    single { RateLimiter(get<AppConfig>().redis) }

    // repositories
    single<UserRepository> { UserRepositoryImpl() }
    single<RefreshTokenRepository> { RefreshTokenRepositoryImpl() }
    single<IngredientRepository> { IngredientRepositoryImpl() }
    single<IngredientMasterRepository> { IngredientMasterRepositoryImpl() }
    single<ScanRepository> { ScanRepositoryImpl() }
    single<RecipeRepository> { RecipeRepositoryImpl() }
    single<BookmarkRepository> { BookmarkRepositoryImpl() }
    single<NotificationRepository> { NotificationRepositoryImpl() }
    single<DeviceTokenRepository> { DeviceTokenRepositoryImpl() }

    // services
    single { OAuthService() }
    single { AuthService(get(), get(), get(), get(), get()) }
    single { UserService(get()) }
    single { IngredientService(get(), get()) }
    single { AiServiceClient(get<AppConfig>().aiService) }
    single { ScanService(get(), get()) }
    single { RecipeService(get(), get(), get(), get()) }
    single { NotificationService(get(), get()) }
    single { ExpiryNotificationScheduler(get(), get(), get()) }
}
