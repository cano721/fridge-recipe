plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
    application
}

application {
    mainClass.set("com.fridgerecipe.ApplicationKt")
}

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_17)
        localImageName.set("fridge-recipe-api")
        imageTag.set("latest")
    }
}

dependencies {
    // Ktor Server
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.call.logging)

    // Ktor Client (AI service communication)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)

    // Database
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.postgresql)
    implementation(libs.hikari)

    // Redis
    implementation(libs.lettuce)

    // DI
    implementation(libs.koin.ktor)

    // Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)

    // Logging
    implementation(libs.logback)

    // Testing
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit5)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
