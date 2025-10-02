import tv.teads.AndroidLibConfig
import tv.teads.Libs
import tv.teads.versionCode
import tv.teads.versionName

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "tv.teads.teadssdkdemo"
    compileSdk = AndroidLibConfig.compileSdk

    defaultConfig {
        applicationId = "tv.teads.teadssdkdemo"
        minSdk = AndroidLibConfig.minSdk
        targetSdk = AndroidLibConfig.targetSdk
        versionCode = project.versionCode
        versionName = project.versionName
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            // R8 full (optimized) mode + resource shrinking
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libs.AndroidX.APPCOMPAT)
    implementation(Libs.AndroidX.CONSTRAINT_LAYOUT)
    implementation(Libs.COROUTINES_CORE)
    implementation(Libs.AndroidX.CARDVIEW)
    implementation(Libs.AndroidX.WEBKIT)
    implementation(Libs.MATERIAL)
    implementation("com.squareup.picasso:picasso:2.8")

    // Teads SDK
    implementation(Libs.Teads.sdk(project.versionName)) {
        isTransitive = true
    }
    // Teads Adapters
    implementation(Libs.Teads.admobAdapter(project.versionName))
    implementation(Libs.Teads.applovinAdapter(project.versionName))
    implementation(Libs.Teads.smartAdapter(project.versionName))
    implementation(Libs.Teads.prebidAdapter(project.versionName))

    implementation(Libs.PLAY_SERVICES_ADS)
    implementation(Libs.APPLOVIN_SDK)
    implementation(Libs.SMART_CORE_SDK)
    implementation(Libs.SMART_DISPLAY_SDK) {
        isTransitive = true
    }
    implementation(Libs.PREBID_SDK)

    implementation(projects.webviewhelper)

    // Huawei ads identifier sdk
    implementation(Libs.HUAWEI_IDENTIFIER)

    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.compose.foundation:foundation")
    
    // ViewModel for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")


    testImplementation(Libs.Test.JUNIT)
}
