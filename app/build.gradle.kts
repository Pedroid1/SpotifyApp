import com.pedroid.convention.ProjectBuildType
import java.util.Properties

plugins {
    alias(libs.plugins.project.android.application)
    alias(libs.plugins.project.android.hilt)
    alias(libs.plugins.project.android.navigation)
    alias(libs.plugins.project.android.firebase)
    alias(libs.plugins.kotlin.android)
    id("project.kotlin.detekt")
    id("com.google.devtools.ksp")
}

if (!gradle.startParameter.taskNames.any { it.contains("test") }) {
    pluginManager.apply("com.google.gms.google-services")
    pluginManager.apply("com.google.firebase.crashlytics")
}

android {
    namespace = "com.pedroid.spotifyapp"

    defaultConfig {
        applicationId = "com.pedroid.spotifyapp"
        versionCode = 1
        versionName = "1.0"
        
        testInstrumentationRunner = "com.pedroid.testing.AppTestRunner"

        manifestPlaceholders["redirectSchemeName"] = "pedroid"
        manifestPlaceholders["redirectHostName"] = "callback"
    }

    val props = Properties()
    props.load(project.rootProject.file("keys.properties").inputStream())
    buildTypes {
        val debug by getting {
            applicationIdSuffix = ProjectBuildType.DEBUG.applicationIdSuffix
            buildConfigField("String", "CLIENT_ID", props.getProperty("CLIENT_ID"))
            buildConfigField("String", "CLIENT_SECRET", props.getProperty("CLIENT_SECRET"))
        }
        val release by getting {
            isMinifyEnabled = true
            isShrinkResources = true
            applicationIdSuffix = ProjectBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "CLIENT_ID", props.getProperty("CLIENT_ID"))
            buildConfigField("String", "CLIENT_SECRET", props.getProperty("CLIENT_SECRET"))
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
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