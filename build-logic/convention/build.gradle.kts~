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
        register("androidHilt") {
            id = "project.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("kotlinDetekt") {
            id = "coincap.kotlin.detekt"
            implementationClass = "DetektConventionPlugin"
        }
    }
}
