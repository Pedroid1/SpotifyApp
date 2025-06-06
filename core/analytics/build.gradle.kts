plugins {
    alias(libs.plugins.project.android.library)
    alias(libs.plugins.project.android.hilt)
    alias(libs.plugins.project.android.firebase)
    id("project.kotlin.detekt")
}

android {
    namespace = "com.pedroid.core.analytics"
}
