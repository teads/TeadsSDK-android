import tv.teads.AndroidLibConfig
import tv.teads.Libs
import tv.teads.versionName

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "tv.teads.webviewhelper"
    compileSdk= AndroidLibConfig.compileSdk

    resourcePrefix = "teads_"

    defaultConfig {
        minSdk = AndroidLibConfig.minSdk
        lint.targetSdk = AndroidLibConfig.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(Libs.COROUTINES_CORE)

    // Teads SDK
    implementation(Libs.Teads.sdk(project.versionName)) {
        isTransitive = true
    }
}
