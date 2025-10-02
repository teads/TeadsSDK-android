package tv.teads.teadssdkdemo.v6.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tv.teads.teadssdkdemo.v6.data.DemoConfiguration
import tv.teads.teadssdkdemo.v6.ui.components.ChipData

class DemoViewModel : ViewModel() {

    // Selected states
    var selectedFormat: FormatType? by mutableStateOf(null)
        private set

    private var selectedProvider: ProviderType? by mutableStateOf(null)


    private var selectedIntegration: IntegrationType? by mutableStateOf(null)

    // Text fields for placement configuration
    private val _placementId = MutableStateFlow("")
    val placementId: StateFlow<String> = _placementId.asStateFlow()

    private val _widgetId = MutableStateFlow("")
    val widgetId: StateFlow<String> = _widgetId.asStateFlow()

    private val _installationKey = MutableStateFlow("")
    val installationKey: StateFlow<String> = _installationKey.asStateFlow()

    private val _articleUrl = MutableStateFlow("")
    val articleUrl: StateFlow<String> = _articleUrl.asStateFlow()

    init {
        // Initialize with defaults and sync with DemoConfiguration
        selectedFormat = DemoConfiguration.getFormatOrDefault()
        selectedProvider = DemoConfiguration.getProviderOrDefault()
        selectedIntegration = DemoConfiguration.getIntegrationOrDefault()
        _placementId.value = DemoConfiguration.getPlacementIdOrDefault()
        _widgetId.value = DemoConfiguration.getWidgetIdOrDefault()
        _installationKey.value = DemoConfiguration.getInstallationKeyOrDefault()
        _articleUrl.value = DemoConfiguration.getArticleUrlOrDefault()
    }

    // Static lists
    private val formatTypes = listOf(
        FormatType.MEDIA,
        FormatType.MEDIANATIVE,
        FormatType.FEED,
        FormatType.RECOMMENDATIONS
    )
    
    // Provider types available for Media format
    private val mediaProviders = listOf(
        ProviderType.DIRECT,
        ProviderType.ADMOB,
        ProviderType.SMART,
        ProviderType.APPLOVIN,
        ProviderType.PREBID
    )
    
    // Provider types available for Media Native format
    private val mediaNativeProviders = listOf(
        ProviderType.DIRECT,
        ProviderType.ADMOB,
        ProviderType.SMART,
        ProviderType.APPLOVIN
    )
    
    // Provider types available for Feed/Recommendations formats
    private val feedRecommendationsProviders = listOf(
        ProviderType.DIRECT
    )
    
    
    // PID presets for Media format
    private val mediaPids = listOf(
        "Landscape" to "84242",
        "Vertical" to "127546",
        "Square" to "127547",
        "Carousel" to "128779"
    )
    
    // PID presets for Media Native format
    private val mediaNativePids = listOf(
        "Image" to "124859"
    )
    
    // Widget ID presets for Feed format
    private val feedWidgetIds = listOf(
        "MB_1" to "MB_1",
        "MB_2" to "MB_2", 
        "TEST_FEED" to "TEST_FEED"
    )
    
    // Widget ID presets for Recommendations format
    private val recommendationsWidgetIds = listOf(
        "SDK_1" to "SDK_1",
        "RECS_1" to "RECS_1",
        "TEST_RECS" to "TEST_RECS"
    )
    
    // Installation Key presets for Feed format
    private val feedInstallationKeys = listOf(
        "NANOWDGT01" to "NANOWDGT01",
        "TESTKEY01" to "TESTKEY01"
    )
    
    // Installation Key presets for Recommendations format
    private val recommendationsInstallationKeys = listOf(
        "NANOWDGT01" to "NANOWDGT01",
        "TESTKEY01" to "TESTKEY01"
    )
    
    private val integrationTypes = listOf(
        IntegrationType.COLUMN,
        IntegrationType.LAZYCOLUMN,
        IntegrationType.SCROLLVIEW,
        IntegrationType.RECYCLERVIEW
    )
    
    // Get providers based on selected format
    private fun getProviders(): List<ProviderType> {
        return when (selectedFormat) {
            FormatType.MEDIA -> mediaProviders
            FormatType.MEDIANATIVE -> mediaNativeProviders
            FormatType.FEED, FormatType.RECOMMENDATIONS -> feedRecommendationsProviders
            else -> throw IllegalAccessException("selectedFormat is null")
        }
    }
    

    // Update functions
    private fun updateDefaultsFormat(format: FormatType) {
        selectedFormat = format
        DemoConfiguration.currentFormat = format

        // Reset provider if current provider is not available for the new format
        val availableProviders = getProviders()
        if (selectedProvider != null && selectedProvider !in availableProviders) {
            selectedProvider = availableProviders.firstOrNull() // Set to first available provider, or null if empty
        }
        
        // Set appropriate values when format changes (no empty checks!)
        when (format) {
            FormatType.MEDIA -> {
                _placementId.value = DemoConfiguration.DEFAULT_MEDIA_PID
            }
            FormatType.MEDIANATIVE -> {
                _placementId.value = DemoConfiguration.DEFAULT_MEDIA_NATIVE_PID
            }
            FormatType.FEED -> {
                _widgetId.value = DemoConfiguration.DEFAULT_FEED_WIDGET_ID
            }
            FormatType.RECOMMENDATIONS -> {
                _widgetId.value = DemoConfiguration.DEFAULT_RECOMMENDATIONS_WIDGET_ID
            }
        }
    }
    
    fun updateProvider(provider: ProviderType) {
        selectedProvider = provider
        DemoConfiguration.currentProvider = provider
    }
    
    
    fun updateIntegration(integration: IntegrationType) {
        selectedIntegration = integration
        DemoConfiguration.currentIntegration = integration
    }
    
    fun updatePlacementId(pid: String) {
        _placementId.value = pid
        DemoConfiguration.currentPlacementId = pid
    }

    fun updateWidgetId(widgetId: String) {
        _widgetId.value = widgetId
        DemoConfiguration.currentWidgetId = widgetId
    }

    fun updateInstallationKey(installationKey: String) {
        _installationKey.value = installationKey
        DemoConfiguration.currentInstallationKey = installationKey
    }

    fun updateArticleUrl(articleUrl: String) {
        _articleUrl.value = articleUrl
        DemoConfiguration.currentArticleUrl = articleUrl
    }

    // Chip data helper methods
    fun getFormatChips(): List<ChipData> = formatTypes.mapIndexed { index, format ->
        ChipData(
            id = index,
            text = format.displayName,
            isSelected = selectedFormat == format
        )
    }

    fun getProviderChips(): List<ChipData> = getProviders().mapIndexed { index, provider ->
        ChipData(
            id = index,
            text = provider.displayName,
            isSelected = selectedProvider == provider
        )
    }

    fun getMediaPidChips(): List<ChipData> = mediaPids.mapIndexed { index, (label, _) ->
        ChipData(
            id = index,
            text = label,
            isSelected = _placementId.value == mediaPids[index].second
        )
    }

    fun getMediaNativePidChips(): List<ChipData> = mediaNativePids.mapIndexed { index, (label, _) ->
        ChipData(
            id = index,
            text = label,
            isSelected = _placementId.value == mediaNativePids[index].second
        )
    }

    fun getIntegrationChips(): List<ChipData> = integrationTypes.mapIndexed { index, integration ->
        ChipData(
            id = index,
            text = integration.displayName,
            isSelected = selectedIntegration == integration
        )
    }

    fun getFeedWidgetIdChips(): List<ChipData> = feedWidgetIds.mapIndexed { index, (label, _) ->
        ChipData(
            id = index,
            text = label,
            isSelected = _widgetId.value == feedWidgetIds[index].second
        )
    }

    fun getRecommendationsWidgetIdChips(): List<ChipData> = recommendationsWidgetIds.mapIndexed { index, (label, _) ->
        ChipData(
            id = index,
            text = label,
            isSelected = _widgetId.value == recommendationsWidgetIds[index].second
        )
    }

    fun getFeedInstallationKeyChips(): List<ChipData> = feedInstallationKeys.mapIndexed { index, (label, _) ->
        ChipData(
            id = index,
            text = label,
            isSelected = _installationKey.value == feedInstallationKeys[index].second
        )
    }

    fun getRecommendationsInstallationKeyChips(): List<ChipData> = recommendationsInstallationKeys.mapIndexed { index, (label, _) ->
        ChipData(
            id = index,
            text = label,
            isSelected = _installationKey.value == recommendationsInstallationKeys[index].second
        )
    }

    // Chip click handlers
    fun onFormatChipClick(index: Int) {
        if (index in formatTypes.indices) {
            val format = formatTypes[index]
            updateDefaultsFormat(format)
        }
    }

    fun onProviderChipClick(index: Int) {
        val availableProviders = getProviders()
        if (index in availableProviders.indices) {
            val provider = availableProviders[index]
            updateProvider(provider)
        }
    }

    fun onMediaPidChipClick(index: Int) {
        if (index in mediaPids.indices) {
            val pid = mediaPids[index].second
            _placementId.value = pid
        }


    }

    fun onMediaNativePidChipClick(index: Int) {
        if (index in mediaNativePids.indices) {
            val pid = mediaNativePids[index].second
            _placementId.value = pid
        }
    }

    fun onFeedWidgetIdChipClick(index: Int) {
        if (index in feedWidgetIds.indices) {
            val widgetId = feedWidgetIds[index].second
            _widgetId.value = widgetId
        }
    }

    fun onRecommendationsWidgetIdChipClick(index: Int) {
        if (index in recommendationsWidgetIds.indices) {
            val widgetId = recommendationsWidgetIds[index].second
            _widgetId.value = widgetId
        }
    }

    fun onFeedInstallationKeyChipClick(index: Int) {
        if (index in feedInstallationKeys.indices) {
            val installationKey = feedInstallationKeys[index].second
            _installationKey.value = installationKey
        }
    }

    fun onRecommendationsInstallationKeyChipClick(index: Int) {
        if (index in recommendationsInstallationKeys.indices) {
            val installationKey = recommendationsInstallationKeys[index].second
            _installationKey.value = installationKey
        }
    }

    fun onArticleUrlChange(articleUrl: String) {
        _articleUrl.value = articleUrl
        DemoConfiguration.currentArticleUrl = articleUrl
    }

    fun onIntegrationChipClick(index: Int) {
        if (index in integrationTypes.indices) {
            val integration = integrationTypes[index]
            updateIntegration(integration)
            DemoConfiguration.currentIntegration = integration
        }
    }
}

