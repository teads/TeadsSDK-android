import tv.teads.AndroidLibConfig
import tv.teads.Libs
import tv.teads.Versions
import tv.teads.versionCode
import tv.teads.versionName

plugins {
    id("com.android.application")
    id("kotlin-android")
    // NOTE: org.jetbrains.kotlin.plugin.compose is Kotlin 2.0+ only.
    // On Kotlin 1.9.24 we use the legacy composeOptions block below.
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
        compose = true
    }

    // Kotlin 1.9.24 uses the legacy Compose compiler extension path.
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
}

// ---------------------------------------------------------------------------
// Publisher-on-Kotlin-1.9.24 workaround
// ---------------------------------------------------------------------------
// The Teads SDK is compiled with Kotlin 2.1.21 and its class files carry
// Kotlin metadata version 2.1.0. Compiler 1.9.x can only read up to metadata
// 2.0 by default and will fail with:
//   "Class '...' was compiled with an incompatible version of Kotlin.
//    The actual metadata version is 2.1.0, but the compiler version 1.9.0
//    can read versions up to 2.0.0."
// This flag tells the 1.9 compiler to trust and read the newer metadata.
// Safe because the SDK's public API does not expose any Kotlin 2.x-only
// language feature.
// ---------------------------------------------------------------------------
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xskip-metadata-version-check"
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
