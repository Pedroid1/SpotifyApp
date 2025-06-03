plugins {
    alias(libs.plugins.project.android.library)
    id("kotlin-kapt")
}

android {
    namespace = "com.pedroid.core.design_system"

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
}