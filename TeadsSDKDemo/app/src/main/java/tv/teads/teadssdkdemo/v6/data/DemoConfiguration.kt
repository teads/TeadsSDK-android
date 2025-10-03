package tv.teads.teadssdkdemo.v6.data

import tv.teads.teadssdkdemo.v6.domain.FormatType
import tv.teads.teadssdkdemo.v6.domain.ProviderType
import tv.teads.teadssdkdemo.v6.domain.IntegrationType

/**
 * Configuration object that holds default values and current session values
 */
object DemoConfiguration {

    // Current session values (private - accessed via setter methods and getXXXOrDefault methods)
    private var currentFormat: FormatType? = null
    private var currentProvider: ProviderType? = null
    private var currentPlacementId: String = ""
    private var currentWidgetId: String = ""
    private var currentInstallationKey: String = ""
    private var currentArticleUrl: String = ""
    private var currentIntegration: IntegrationType? = null

    val DEFAULT_FORMAT = FormatType.MEDIA
    val DEFAULT_PROVIDER = ProviderType.DIRECT
    val DEFAULT_INTEGRATION = IntegrationType.COLUMN
    val DEFAULT_MEDIA_PID = "84242" // Landscape
    val DEFAULT_MEDIA_NATIVE_PID = "124859" // Image
    val DEFAULT_MEDIA_ADMOB_PID = "ca-app-pub-3068786746829754/3486435166" // Landscape
    val DEFAULT_MEDIANATIVE_ADMOB_PID = "ca-app-pub-3068786746829754/9820813147" // Image
    val DEFAULT_MEDIA_APPLOVIN_PID = "3359d5bcb0cf612b" // Landscape
    val DEFAULT_MEDIANATIVE_APPLOVIN_PID = "a416d5d67e65ddcd" // Image
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
     * Get current or default integration
     */
    fun getIntegrationOrDefault(): IntegrationType {
        return currentIntegration ?: DEFAULT_INTEGRATION
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
    
    // Setter functions
    fun setFormat(format: FormatType?) {
        currentFormat = format
    }
    
    fun setProvider(provider: ProviderType?) {
        currentProvider = provider
    }
    
    fun setPlacementId(placementId: String) {
        currentPlacementId = placementId
    }
    
    fun setWidgetId(widgetId: String) {
        currentWidgetId = widgetId
    }
    
    fun setInstallationKey(installationKey: String) {
        currentInstallationKey = installationKey
    }
    
    fun setArticleUrl(articleUrl: String) {
        currentArticleUrl = articleUrl
    }
    
    fun setIntegration(integration: IntegrationType?) {
        currentIntegration = integration
    }
}
