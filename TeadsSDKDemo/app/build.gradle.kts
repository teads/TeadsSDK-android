import tv.teads.AndroidLibConfig
import tv.teads.Libs
import tv.teads.versionCode
import tv.teads.versionName

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(AndroidLibConfig.compileSdk)

    defaultConfig {
        applicationId = "tv.teads.teadssdkdemo"
        minSdkVersion(AndroidLibConfig.minSdk)
        targetSdkVersion(AndroidLibConfig.targetSdk)
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
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        sourceCompatibility(JavaVersion.VERSION_1_8)
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libs.AndroidX.APPCOMPAT)
    implementation(Libs.AndroidX.CONSTRAINT_LAYOUT)
    implementation(Libs.AndroidX.CARDVIEW)
    implementation(Libs.AndroidX.WEBKIT)
    implementation(Libs.MATERIAL)
    implementation(Libs.PLAY_SERVICES_ADS)

    // Teads SDK
    implementation(Libs.Teads.sdk(project.versionName)) {
        isTransitive = true
    }
    // Teads Adapters
    implementation(Libs.Teads.admobAdapter(project.versionName))
    implementation(Libs.Teads.applovinAdapter(project.versionName))

    implementation(Libs.APPLOVIN_SDK)

    implementation(Libs.AD_COLONY_SDK)

//    implementation(projects.webviewhelper)
    implementation(project(":webviewhelper"))

    //Huawei ads identifier sdk
    implementation(Libs.HUAWEI_IDENTIFIER)

//    testImplementation(Libs.Test.JUNIT)
}