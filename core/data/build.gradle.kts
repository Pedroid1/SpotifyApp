plugins {
    alias(libs.plugins.project.android.library)
    alias(libs.plugins.project.android.hilt)
    id("project.kotlin.detekt")
}

android {
    namespace = "com.pedroid.core.data"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
}