plugins {
    alias(libs.plugins.project.jvm.library)
}
dependencies {
    implementation(project(":core:model"))
}