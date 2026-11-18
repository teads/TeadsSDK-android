package tv.teads.teadssdkdemo.v6.domain

/**
 * Represents the different ad providers available in the demo
 */
enum class ProviderType(val displayName: String) {
    DIRECT("Direct"),
    ADMOB("AdMob"),
    EQUATIV("Equativ"),
    APPLOVIN("AppLovin"),
    PREBID("Prebid")
}
