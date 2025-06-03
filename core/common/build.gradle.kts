plugins {
    alias(libs.plugins.project.android.library)
    alias(libs.plugins.project.android.hilt)
    id("kotlin-kapt")
    id("project.kotlin.detekt")
}

android {
    namespace = "com.pedroid.core.common"
}

dependencies {

}