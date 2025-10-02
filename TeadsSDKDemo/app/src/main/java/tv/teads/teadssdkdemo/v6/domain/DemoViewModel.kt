package tv.teads.teadssdkdemo.v6.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tv.teads.teadssdkdemo.v6.data.FormatType
import tv.teads.teadssdkdemo.v6.data.IntegrationType
import tv.teads.teadssdkdemo.v6.data.ProviderType
import tv.teads.teadssdkdemo.v6.ui.components.ChipData

class DemoViewModel : ViewModel() {
    // Static lists
    private val formatTypes = listOf(
        FormatType.MEDIA,
        FormatType.MEDIANATIVE,
        FormatType.FEED,
        FormatType.RECOMMENDATIONS
    )
    
    private val providerTypes = listOf(
        ProviderType.DIRECT,
        ProviderType.ADMOB,
        ProviderType.SMART,
        ProviderType.APPLOVIN,
        ProviderType.PREBID
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

    // Selected states
    var selectedFormat: FormatType? by mutableStateOf(FormatType.MEDIA)
        private set
    
    var selectedProvider: ProviderType? by mutableStateOf(ProviderType.DIRECT)
        private set
    
    
    var selectedIntegration: IntegrationType? by mutableStateOf(null)
        private set

    // Text fields for placement configuration
    private val _placementId = MutableStateFlow("84242") // Default PID for Media format
    val placementId: StateFlow<String> = _placementId.asStateFlow()

    private val _widgetId = MutableStateFlow("MB_1") // Default for Feed format  
    val widgetId: StateFlow<String> = _widgetId.asStateFlow()

    private val _installationKey = MutableStateFlow("")
    val installationKey: StateFlow<String> = _installationKey.asStateFlow()

    private val _articleUrl = MutableStateFlow("https://mobile-demo.outbrain.com/")
    val articleUrl: StateFlow<String> = _articleUrl.asStateFlow()

    // Functions to get static lists
    fun getFormats() = formatTypes
    fun getIntegrationTypes() = integrationTypes
    
    // Get providers based on selected format
    fun getProviders(): List<ProviderType> {
        return when (selectedFormat) {
            FormatType.MEDIA -> mediaProviders
            FormatType.MEDIANATIVE -> mediaNativeProviders
            FormatType.FEED, FormatType.RECOMMENDATIONS -> feedRecommendationsProviders
            null -> providerTypes // Show all if no format selected
        }
    }
    

    // Update functions
    fun updateFormat(format: FormatType) {
        selectedFormat = format
        
        // Reset provider if current provider is not available for the new format
        val availableProviders = getProviders()
        if (selectedProvider != null && selectedProvider !in availableProviders) {
            selectedProvider = availableProviders.firstOrNull() // Set to first available provider, or null if empty
        }
        
        // Clear and set default placement values when format changes
        when (format) {
            FormatType.MEDIA -> {
                _placementId.value = "84242" // Landscape
            }
            FormatType.MEDIANATIVE -> {
                _placementId.value = "124859" // Image
            }
            FormatType.FEED -> {
                _widgetId.value = "MB_1"
            }
            FormatType.RECOMMENDATIONS -> {
                _widgetId.value = "SDK_1"
            }
        }
    }
    
    fun updateProvider(provider: ProviderType) {
        selectedProvider = provider
    }
    
    
    fun updateIntegration(integration: IntegrationType) {
        selectedIntegration = integration
    }
    
    fun updateCustomPid(pid: String) {
        _placementId.value = pid
    }

    fun updateWidgetId(widgetId: String) {
        _widgetId.value = widgetId
    }

    fun updateInstallationKey(installationKey: String) {
        _installationKey.value = installationKey
    }

    fun updateArticleUrl(articleUrl: String) {
        _articleUrl.value = articleUrl
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
            updateFormat(formatTypes[index])
        }
    }

    fun onProviderChipClick(index: Int) {
        val availableProviders = getProviders()
        if (index in availableProviders.indices) {
            updateProvider(availableProviders[index])
        }
    }

    fun onMediaPidChipClick(index: Int) {
        if (index in mediaPids.indices) {
            _placementId.value = mediaPids[index].second
        }


    }

    fun onMediaNativePidChipClick(index: Int) {
        if (index in mediaNativePids.indices) {
            _placementId.value = mediaNativePids[index].second
        }
    }

    fun onIntegrationChipClick(index: Int) {
        if (index in integrationTypes.indices) {
            updateIntegration(integrationTypes[index])
        }
    }

    fun onFeedWidgetIdChipClick(index: Int) {
        if (index in feedWidgetIds.indices) {
            _widgetId.value = feedWidgetIds[index].second
        }
    }

    fun onRecommendationsWidgetIdChipClick(index: Int) {
        if (index in recommendationsWidgetIds.indices) {
            _widgetId.value = recommendationsWidgetIds[index].second
        }
    }

    fun onFeedInstallationKeyChipClick(index: Int) {
        if (index in feedInstallationKeys.indices) {
            _installationKey.value = feedInstallationKeys[index].second
        }
    }

    fun onRecommendationsInstallationKeyChipClick(index: Int) {
        if (index in recommendationsInstallationKeys.indices) {
            _installationKey.value = recommendationsInstallationKeys[index].second
        }
    }
}

