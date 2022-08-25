import tv.teads.AndroidLibConfig
import tv.teads.versionName

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdk= AndroidLibConfig.compileSdk

    resourcePrefix = "teads_"

    defaultConfig {
        minSdk = AndroidLibConfig.minSdk
        targetSdk = AndroidLibConfig.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    // Teads SDK
    implementation(tv.teads.Libs.Teads.sdk(project.versionName)) {
        isTransitive = true
    }
}
