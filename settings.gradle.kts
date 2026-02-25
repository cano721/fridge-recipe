pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolution {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "fridge-recipe"

include(":shared")
include(":androidApp")
include(":backend:main-api")
