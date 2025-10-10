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

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    
    // AndroidX dependencies
    implementation(Libs.AndroidX.APPCOMPAT)
    implementation(Libs.AndroidX.CONSTRAINT_LAYOUT)
    implementation(Libs.AndroidX.CARDVIEW)
    implementation(Libs.AndroidX.WEBKIT)
    
    // Coroutines
    implementation(Libs.COROUTINES_CORE)
    
    // Material Design
    implementation(Libs.MATERIAL)
    
    // Image loading
    implementation(Libs.PICASSO)

    // Teads SDK
    implementation(Libs.Teads.sdk(project.versionName)) {
        isTransitive = true
    }
    // Teads Adapters
    implementation(Libs.Teads.admobAdapter(project.versionName))
    implementation(Libs.Teads.applovinAdapter(project.versionName))
    implementation(Libs.Teads.smartAdapter(project.versionName))
    implementation(Libs.Teads.prebidAdapter(project.versionName))

    // Third-party SDKs
    implementation(Libs.PLAY_SERVICES_ADS)
    implementation(Libs.APPLOVIN_SDK)
    implementation(Libs.SMART_CORE_SDK)
    implementation(Libs.SMART_DISPLAY_SDK) {
        isTransitive = true
    }
    implementation(Libs.PREBID_SDK)
    implementation(Libs.HUAWEI_IDENTIFIER)

    // Compose BOM
    implementation(platform(Libs.Compose.BOM))
    implementation(Libs.Compose.UI)
    implementation(Libs.Compose.UI_GRAPHICS)
    implementation(Libs.Compose.UI_TOOLING_PREVIEW)
    implementation(Libs.Compose.MATERIAL3)
    implementation(Libs.Compose.MATERIAL_ICONS_EXTENDED)
    implementation(Libs.Compose.ACTIVITY_COMPOSE)
    implementation(Libs.Compose.FOUNDATION)
    
    // ViewModel for Compose
    implementation(Libs.Compose.LIFECYCLE_VIEWMODEL_COMPOSE)
    implementation(Libs.Compose.LIFECYCLE_RUNTIME_COMPOSE)

    // Testing
    testImplementation(Libs.Test.JUNIT)
}
