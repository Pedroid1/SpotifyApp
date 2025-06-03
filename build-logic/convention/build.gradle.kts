import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.pedroid.spotifyapp.buildlogic"

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidFeature") {
            id = "project.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidApplication") {
            id = "project.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "project.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidNavigationComponent") {
            id = "project.android.navigation"
            implementationClass = "AndroidNavigationComponentConventionPlugin"
        }
        register("jvmLibrary") {
            id = "project.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "project.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("kotlinDetekt") {
            id = "project.kotlin.detekt"
            implementationClass = "com.pedroid.convention.plugins.DetektConventionPlugin"
        }
    }
}
