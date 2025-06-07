plugins {
    alias(libs.plugins.project.jvm.library)
    id("project.kotlin.detekt")
}
dependencies {
    implementation(project(":core:model"))
}