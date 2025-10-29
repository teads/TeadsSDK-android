package tv.teads

/**
 * Centralized version management for all dependencies used in the project.
 * This object helps maintain consistency and makes version updates easier.
 */
object Versions {
    // Android SDK versions
    const val compileSdk = 35
    const val minSdk = 24
    const val targetSdk = 35
    
    // Kotlin and Compose
    const val kotlin = "2.1.21"
    const val composeBom = "2025.09.01"
    
    // AndroidX Core
    const val appcompat = "1.3.0"
    const val constraintLayout = "2.0.4"
    const val cardview = "1.0.0"
    const val webkit = "1.4.0"
    
    // Material Design
    const val material = "1.4.0"
    
    // Google Play Services
    const val playServicesAds = "23.0.0"
    
    // Coroutines
    const val coroutines = "1.5.2"
    
    // Third-party SDKs
    const val applovinSdk = "11.3.0"
    const val smartVersion = "7.14.0"
    const val prebidSdk = "2.2.1"
    const val huaweiIdentifier = "3.4.28.313"
    
    // Image loading
    const val picasso = "2.8"
    
    // Compose dependencies
    const val activityCompose = "1.9.2"
    const val lifecycleViewmodelCompose = "2.7.0"
    const val lifecycleRuntimeCompose = "2.7.0"
    
    // Testing
    const val junit = "4.12"
}
