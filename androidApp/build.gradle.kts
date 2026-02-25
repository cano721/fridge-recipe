plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget()

    sourceSets {
        androidMain.dependencies {
            implementation(project(":shared"))
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.koin.android)
        }
    }
}

android {
    namespace = "com.fridgerecipe.android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.fridgerecipe.android"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
