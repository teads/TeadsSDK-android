import tv.teads.AndroidLibConfig
import tv.teads.Libs
import tv.teads.versionName

plugins {
    id("com.android.library")
    id("kotlin-android") // do not remove
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(AndroidLibConfig.compileSdk)

    resourcePrefix("teads_")

    defaultConfig {
        minSdkVersion(AndroidLibConfig.minSdk)
        targetSdkVersion(AndroidLibConfig.targetSdk)

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
}

dependencies {
    implementation(Libs.COROUTINES_CORE)

    // Teads SDK
    implementation(Libs.Teads.sdk(project.versionName)) {
        isTransitive = true
    }
}