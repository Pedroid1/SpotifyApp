plugins {
    alias(libs.plugins.project.android.feature)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.pedroid.feature.home"

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(project(":feature:albums:publicmodule"))
}