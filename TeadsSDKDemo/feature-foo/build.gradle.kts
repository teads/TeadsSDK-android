import tv.teads.AndroidLibConfig
import tv.teads.Libs
import tv.teads.versionName

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "tv.teads.featurefoo"
    compileSdk = AndroidLibConfig.compileSdk

    defaultConfig {
        minSdk = AndroidLibConfig.minSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(Libs.Teads.sdk(project.versionName)) {
        isTransitive = true
    }
}
