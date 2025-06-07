plugins {
    alias(libs.plugins.project.android.library)
    alias(libs.plugins.project.android.navigation)
    id("project.kotlin.detekt")
}

android {
    namespace = "com.pedroid.core.navigation"
}