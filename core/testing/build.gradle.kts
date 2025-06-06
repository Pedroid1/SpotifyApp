plugins {
    alias(libs.plugins.project.android.library)
    alias(libs.plugins.project.android.hilt)
}

android {
    namespace = "com.pedroid.core.testing"
}

dependencies {
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(libs.androidx.test.rules)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.hilt.android.testing)
}