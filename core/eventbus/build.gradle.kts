plugins {
    alias(libs.plugins.project.android.library)
    alias(libs.plugins.project.android.hilt)
    id("project.kotlin.detekt")
}

android {
    namespace = "com.pedroid.core.eventbus"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.lifecycle.livedata.ktx)
    ksp(libs.androidx.lifecycle.compiler)
}