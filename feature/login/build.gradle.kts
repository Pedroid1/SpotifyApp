plugins {
    alias(libs.plugins.project.android.feature)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.pedroid.feature.login"

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

}