import com.pedroid.convention.ProjectBuildType

plugins {
    alias(libs.plugins.project.android.application)
    alias(libs.plugins.project.android.hilt)
    alias(libs.plugins.project.android.navigation)
    id("project.kotlin.detekt")
    id("com.google.devtools.ksp")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.pedroid.spotifyapp"

    defaultConfig {
        applicationId = "com.pedroid.spotifyapp"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["redirectSchemeName"] = "pedroid"
        manifestPlaceholders["redirectHostName"] = "callback"
    }

    buildTypes {
        val debug by getting {
            applicationIdSuffix = ProjectBuildType.DEBUG.applicationIdSuffix
        }
        val release by getting {
            isMinifyEnabled = true
            isShrinkResources = true
            applicationIdSuffix = ProjectBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    val listExcludes = listOf(
        ":core",
        ":feature",
        ":feature:albums",
        ":build-logic",
        ":app"
    )
    rootProject.subprojects.forEach { module ->
        if (module.path !in listExcludes) implementation(project(module.path))
    }

    implementation (libs.androidx.core.splashscreen)
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.auth)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}