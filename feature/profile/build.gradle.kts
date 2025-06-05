plugins {
    alias(libs.plugins.project.android.feature)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.pedroid.feature.profile"

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core:eventbus"))
}