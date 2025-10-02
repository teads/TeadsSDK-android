package tv.teads.teadssdkdemo.v6.data

import tv.teads.teadssdkdemo.v6.domain.FormatType
import tv.teads.teadssdkdemo.v6.domain.ProviderType

/**
 * Configuration object that holds default values and current session values
 */
object DemoConfiguration {

    // Current session values
    var currentFormat: FormatType? = null
    var currentProvider: ProviderType? = null
    var currentPlacementId: String = ""
    var currentWidgetId: String = ""
    var currentInstallationKey: String = ""
    var currentArticleUrl: String = ""

    val DEFAULT_FORMAT = FormatType.MEDIA
    val DEFAULT_PROVIDER = ProviderType.DIRECT
    val DEFAULT_MEDIA_PID = "84242" // Landscape
    val DEFAULT_MEDIA_NATIVE_PID = "124859" // Image
    val DEFAULT_FEED_WIDGET_ID = "MB_1"
    val DEFAULT_RECOMMENDATIONS_WIDGET_ID = "SDK_1"
    val DEFAULT_ARTICLE_URL = "https://mobile-demo.outbrain.com/"
    val DEFAULT_INSTALLATION_KEY = "NANOWDGT01"
    
    /**
     * Get current or default format
     */
    fun getFormatOrDefault(): FormatType {
        return currentFormat ?: DEFAULT_FORMAT
    }
    
    /**
     * Get current or default provider
     */
    fun getProviderOrDefault(): ProviderType {
        return currentProvider ?: DEFAULT_PROVIDER
    }
    
    /**
     * Get current or default placement ID based on format
     */
    fun getPlacementIdOrDefault(): String {
        if (currentPlacementId.isNotBlank()) return currentPlacementId
        
        return when (getFormatOrDefault()) {
            FormatType.MEDIA -> DEFAULT_MEDIA_PID
            FormatType.MEDIANATIVE -> DEFAULT_MEDIA_NATIVE_PID
            else -> ""
        }
    }
    
    /**
     * Get current or default widget ID based on format
     */
    fun getWidgetIdOrDefault(): String {
        if (currentWidgetId.isNotBlank()) return currentWidgetId
        
        return when (getFormatOrDefault()) {
            FormatType.FEED -> DEFAULT_FEED_WIDGET_ID
            FormatType.RECOMMENDATIONS -> DEFAULT_RECOMMENDATIONS_WIDGET_ID
            else -> ""
        }
    }
    
    /**
     * Get current or default article URL
     */
    fun getArticleUrlOrDefault(): String {
        return currentArticleUrl.ifBlank { DEFAULT_ARTICLE_URL }
    }
    
    /**
     * Get current or default installation key
     */
    fun getInstallationKeyOrDefault(): String {
        return currentInstallationKey.ifBlank { DEFAULT_INSTALLATION_KEY }
    }
}
