package tv.teads

object Libs {
    const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2"

    const val APPLOVIN_SDK = "com.applovin:applovin-sdk:11.3.0"

    const val MATERIAL = "com.google.android.material:material:1.4.0"
    const val PLAY_SERVICES_ADS = "com.google.android.gms:play-services-ads:23.5.0"

    const val HUAWEI_IDENTIFIER = "com.huawei.hms:ads-identifier:3.4.28.313"

    private const val smartVersion = "7.14.0"

    const val SMART_CORE_SDK = "com.smartadserver.android:smart-core-sdk:$smartVersion@aar"
    const val SMART_DISPLAY_SDK = "com.smartadserver.android:smart-display-sdk:$smartVersion@aar"

    const val PREBID_SDK = "org.prebid:prebid-mobile-sdk:2.2.1"

    object Teads {
        fun sdk(version: String) = "tv.teads.sdk.android:sdk:$version@aar"
        fun admobAdapter(version: String) = "tv.teads.sdk.android:admobadapter:$version@aar"
        fun applovinAdapter(version: String) = "tv.teads.sdk.android:applovinadapter:$version@aar"
        fun smartAdapter(version: String) = "tv.teads.sdk.android:smartadapter:$version@aar"
        fun prebidAdapter(version: String) = "tv.teads.sdk.android:prebidadapter:$version@aar"
    }

    object AndroidX {
        const val APPCOMPAT = "androidx.appcompat:appcompat:1.3.0"
        const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val CARDVIEW = "androidx.cardview:cardview:1.0.0"
        const val WEBKIT = "androidx.webkit:webkit:1.4.0"
    }

    object Test {
        const val JUNIT = "junit:junit:4.12"
    }
}