pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT) // ← CAMBIAR A ESTE
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SafeVision"
include(":app")