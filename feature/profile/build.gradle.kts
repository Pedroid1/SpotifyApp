plugins {
    alias(libs.plugins.project.android.feature)
    alias(libs.plugins.kotlin.android)
    id("project.kotlin.detekt")
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

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.turbine)
    testImplementation(project(":core:testing"))
}