package tv.teads.teadssdkdemo.v6.data

/**
 * Represents the different ad providers available in the demo
 */
enum class ProviderType(val displayName: String) {
    DIRECT("Direct"),
    ADMOB("AdMob"),
    SMART("Smart"),
    APPLOVIN("AppLovin"),
    PREBID("Prebid"),
    MEDIATION("Mediation"),
    IRONSOURCE("IronSource"),
    UNITY("Unity Ads")
}
