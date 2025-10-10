package tv.teads

/**
 * Centralized dependency declarations for the project.
 * All dependencies are defined here using versions from the Versions object.
 */
object Libs {
    // Coroutines
    const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"

    // Third-party SDKs
    const val APPLOVIN_SDK = "com.applovin:applovin-sdk:${Versions.applovinSdk}"
    const val SMART_CORE_SDK = "com.smartadserver.android:smart-core-sdk:${Versions.smartVersion}@aar"
    const val SMART_DISPLAY_SDK = "com.smartadserver.android:smart-display-sdk:${Versions.smartVersion}@aar"
    const val PREBID_SDK = "org.prebid:prebid-mobile-sdk:${Versions.prebidSdk}"
    const val HUAWEI_IDENTIFIER = "com.huawei.hms:ads-identifier:${Versions.huaweiIdentifier}"

    // Google Services
    const val MATERIAL = "com.google.android.material:material:${Versions.material}"
    const val PLAY_SERVICES_ADS = "com.google.android.gms:play-services-ads:${Versions.playServicesAds}"

    // Image loading
    const val PICASSO = "com.squareup.picasso:picasso:${Versions.picasso}"

    /**
     * Teads SDK dependencies with version parameter
     */
    object Teads {
        fun sdk(version: String) = "tv.teads.sdk.android:sdk:$version@aar"
        fun admobAdapter(version: String) = "tv.teads.sdk.android:admobadapter:$version@aar"
        fun applovinAdapter(version: String) = "tv.teads.sdk.android:applovinadapter:$version@aar"
        fun smartAdapter(version: String) = "tv.teads.sdk.android:smartadapter:$version@aar"
        fun prebidAdapter(version: String) = "tv.teads.sdk.android:prebidadapter:$version@aar"
    }

    /**
     * AndroidX dependencies
     */
    object AndroidX {
        const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.appcompat}"
        const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
        const val CARDVIEW = "androidx.cardview:cardview:${Versions.cardview}"
        const val WEBKIT = "androidx.webkit:webkit:${Versions.webkit}"
    }

    /**
     * Compose dependencies
     */
    object Compose {
        const val BOM = "androidx.compose:compose-bom:${Versions.composeBom}"
        const val UI = "androidx.compose.ui:ui"
        const val UI_GRAPHICS = "androidx.compose.ui:ui-graphics"
        const val UI_TOOLING_PREVIEW = "androidx.compose.ui:ui-tooling-preview"
        const val MATERIAL3 = "androidx.compose.material3:material3"
        const val MATERIAL_ICONS_EXTENDED = "androidx.compose.material:material-icons-extended"
        const val FOUNDATION = "androidx.compose.foundation:foundation"
        const val ACTIVITY_COMPOSE = "androidx.activity:activity-compose:${Versions.activityCompose}"
        const val LIFECYCLE_VIEWMODEL_COMPOSE = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycleViewmodelCompose}"
        const val LIFECYCLE_RUNTIME_COMPOSE = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycleRuntimeCompose}"
    }

    /**
     * Testing dependencies
     */
    object Test {
        const val JUNIT = "junit:junit:${Versions.junit}"
    }
}