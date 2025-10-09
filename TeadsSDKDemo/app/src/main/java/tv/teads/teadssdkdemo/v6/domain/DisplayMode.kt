package tv.teads.teadssdkdemo.v6.domain

/**
 * Represents the different display modes available for providers
 */
enum class DisplayMode(val displayName: String) {
    STANDARD("Standard"),
    STANDALONE("Standalone"),
    MEDIA_ONLY("Media Only"),
    FEED_ONLY("Feed Only"),
    MEDIA_WITH_FEED("Media + Feed"),
    FEED_WITH_MEDIA("Feed + Media")
}
