plugins {
    alias(libs.plugins.project.android.library)
    alias(libs.plugins.project.android.hilt)
    id("kotlin-kapt")
    id("project.kotlin.detekt")
}

android {
    namespace = "com.pedroid.core.common"

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.material)
    implementation(libs.coil)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(project(":core:analytics"))
}