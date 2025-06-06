pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

//gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))

rootProject.name = "SpotifyApp"
include(":app")
include(":core")
include(":core:common")
include(":core:designsystem")
include(":core:domain")
include(":core:model")
include(":core:data")
include(":core:navigation")
include(":feature")
include(":feature:home")
include(":feature:playlist")
include(":feature:profile")
include(":core:eventbus")
include(":feature:albums")
include(":feature:albums:privatemodule")
include(":feature:albums:publicmodule")
include(":core:analytics")
