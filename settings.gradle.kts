pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MiVuelto"
include(":app")
include(":core")
include(":core-data")
include(":core-ui")
include(":feature-purchase")
include(":feature-home")
include(":feature-instant-debit")
include(":feature-send-change")
