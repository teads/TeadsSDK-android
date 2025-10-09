package tv.teads.teadssdkdemo.v6.ui.base

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.domain.DisplayMode
import tv.teads.teadssdkdemo.v6.domain.FormatType
import tv.teads.teadssdkdemo.v6.domain.IntegrationType
import tv.teads.teadssdkdemo.v6.domain.ProviderType
import tv.teads.teadssdkdemo.v6.ui.base.components.ChipData
import tv.teads.teadssdkdemo.v6.ui.base.navigation.Route
import tv.teads.teadssdkdemo.v6.ui.base.navigation.RouteFactory

class DemoViewModel : ViewModel() {

    // Navigation callback
    private var onNavigateCallback: ((Route) -> Unit)? = null

    // Scroll state persistence for DemoScreen
    private var _scrollState: ScrollState? = null
    val scrollState: ScrollState
        get() = _scrollState ?: ScrollState(0).also { _scrollState = it }

    // Selected states
    var selectedFormat: FormatType? by mutableStateOf(null)
        private set

    var selectedProvider: ProviderType? by mutableStateOf(null)
        private set

    private var selectedDisplayMode: DisplayMode? by mutableStateOf(null)

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
        selectedFormat = DemoSessionConfiguration.getFormatOrDefault()
        selectedProvider = DemoSessionConfiguration.getProviderOrDefault()
        selectedDisplayMode = DemoSessionConfiguration.getDisplayModeOrNull()
        selectedIntegration = DemoSessionConfiguration.getIntegrationOrDefault()
        _placementId.value = DemoSessionConfiguration.getPlacementIdOrDefault()
        _widgetId.value = DemoSessionConfiguration.getWidgetIdOrDefault()
        _installationKey.value = DemoSessionConfiguration.getInstallationKeyOrDefault()
        _articleUrl.value = DemoSessionConfiguration.getArticleUrlOrDefault()
    }

    /**
     * Set navigation callback
     */
    fun setOnNavigateCallback(callback: (Route) -> Unit) {
        onNavigateCallback = callback
    }

    /**
     * Trigger navigation based on current configuration
     */
    fun launchNavigation() {
        val route = RouteFactory.createRoute(
            format = DemoSessionConfiguration.getFormatOrDefault(),
            provider = DemoSessionConfiguration.getProviderOrDefault(),
            integration = DemoSessionConfiguration.getIntegrationOrDefault(),
            displayMode = DemoSessionConfiguration.getDisplayModeOrNull()
        )
        onNavigateCallback?.invoke(route)
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
        ProviderType.APPLOVIN,
        ProviderType.SMART,
        ProviderType.PREBID
    )

    // Provider types available for Media Native format
    private val mediaNativeProviders = listOf(
        ProviderType.DIRECT,
        ProviderType.ADMOB,
        ProviderType.APPLOVIN,
        ProviderType.SMART
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

    private val mediaAdmobPids = listOf(
        "Landscape" to "ca-app-pub-3068786746829754/3486435166",
        "Vertical" to "ca-app-pub-3068786746829754/1731249109",
        "Square" to "ca-app-pub-3068786746829754/5867288248",
        "Carousel" to "ca-app-pub-3068786746829754/1761017118"
    )

    private val mediaNativeAdmobPids = listOf(
        "Image" to "ca-app-pub-3068786746829754/9820813147",
        "Google Test Ad" to "ca-app-pub-3940256099942544/2247696110"
    )

    private val mediaApplovinPids = listOf(
        "Landscape" to "3359d5bcb0cf612b",
        "Vertical" to "74481c0cee5c73b1",
        "Square" to "accecf03a9e0a672",
        "Carousel" to "d1fb90cd8eeb350e"
    )

    private val mediaNativeApplovinPids = listOf(
        "Image" to "a416d5d67e65ddcd"
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

    private val singleIntegrationType = listOf(
        IntegrationType.COLUMN
    )

    private val partialIntegrationTypes = listOf(
        IntegrationType.COLUMN,
        IntegrationType.SCROLLVIEW,
    )

    private val fullIntegrationTypes = listOf(
        IntegrationType.COLUMN,
        IntegrationType.LAZYCOLUMN,
        IntegrationType.SCROLLVIEW,
        IntegrationType.RECYCLERVIEW
    )

    // Display mode options for Prebid provider
    private val prebidDisplayModes = listOf(
        DisplayMode.STANDARD,
        DisplayMode.STANDALONE
    )

    // Display mode options for Media provider
    private val mediaDisplayModes = listOf(
        DisplayMode.MEDIA_ONLY,
        DisplayMode.MEDIA_WITH_FEED
    )

    // Display mode options for Feed provider
    private val feedDisplayModes = listOf(
        DisplayMode.FEED_ONLY,
        DisplayMode.FEED_WITH_MEDIA
    )

    // Get providers based on selected format
    private fun getProviders(): List<ProviderType> {
        return when (selectedFormat) {
            FormatType.MEDIA -> mediaProviders
            FormatType.MEDIANATIVE -> mediaNativeProviders
            FormatType.FEED, FormatType.RECOMMENDATIONS -> feedRecommendationsProviders
            else -> throw IllegalAccessException("Impossible format")
        }
    }

    // Get display mode options based on selected provider and format
    private fun getDisplayModes(): List<DisplayMode> {
        return when (selectedProvider to selectedFormat) {
            ProviderType.PREBID to FormatType.MEDIA -> prebidDisplayModes
            ProviderType.DIRECT to FormatType.MEDIA -> mediaDisplayModes
            ProviderType.DIRECT to FormatType.FEED -> feedDisplayModes
            else -> emptyList()
        }
    }

    // Check if display mode section should be shown
    fun shouldShowDisplayModeSection(): Boolean {
        return when (selectedProvider) {
            ProviderType.PREBID -> true
            ProviderType.DIRECT -> selectedFormat in listOf(FormatType.MEDIA, FormatType.FEED)
            else -> false
        }
    }

    // Update functions
    private fun updateDefaultsByFormat(format: FormatType) {
        selectedFormat = format
        DemoSessionConfiguration.setFormat(format)

        // Reset provider if current provider is not available for the new format
        val availableProviders = getProviders()
        if (selectedProvider != null && selectedProvider !in availableProviders) {
            updateProvider(availableProviders.first())
        }

        // Reset type if current type is not available for the new format/provider combination
        val availableDisplayModes = getDisplayModes()
        if (selectedDisplayMode != null && selectedDisplayMode !in availableDisplayModes) {
            availableDisplayModes.firstOrNull()?.let { updateDisplayMode(it) }
        }

        updatePlacementConfiguration()
    }

    private fun updatePlacementConfiguration() {
        when (selectedProvider to selectedFormat) {
            ProviderType.DIRECT to FormatType.MEDIA ->
                updatePlacementId(DemoSessionConfiguration.DEFAULT_MEDIA_PID)

            ProviderType.DIRECT to FormatType.MEDIANATIVE ->
                updatePlacementId(DemoSessionConfiguration.DEFAULT_MEDIA_NATIVE_PID)

            ProviderType.ADMOB to FormatType.MEDIA ->
                updatePlacementId(DemoSessionConfiguration.DEFAULT_MEDIA_ADMOB_PID)

            ProviderType.ADMOB to FormatType.MEDIANATIVE ->
                updatePlacementId(DemoSessionConfiguration.DEFAULT_MEDIANATIVE_ADMOB_PID)

            ProviderType.APPLOVIN to FormatType.MEDIA ->
                updatePlacementId(DemoSessionConfiguration.DEFAULT_MEDIA_APPLOVIN_PID)

            ProviderType.APPLOVIN to FormatType.MEDIANATIVE ->
                updatePlacementId(DemoSessionConfiguration.DEFAULT_MEDIANATIVE_APPLOVIN_PID)

            ProviderType.DIRECT to FormatType.FEED ->
                updateWidgetId(DemoSessionConfiguration.DEFAULT_FEED_WIDGET_ID)

            ProviderType.DIRECT to FormatType.RECOMMENDATIONS ->
                updateWidgetId(DemoSessionConfiguration.DEFAULT_RECOMMENDATIONS_WIDGET_ID)
        }
    }

    private fun updateProvider(provider: ProviderType) {
        selectedProvider = provider
        DemoSessionConfiguration.setProvider(provider)

        // Reset type if current type is not available for the new provider/format combination
        val availableDisplayModes = getDisplayModes()
        if (selectedDisplayMode != null && selectedDisplayMode !in availableDisplayModes) {
            availableDisplayModes.firstOrNull()?.let { updateDisplayMode(it) }
        }
    }


    private fun updateIntegration(integration: IntegrationType) {
        selectedIntegration = integration
        DemoSessionConfiguration.setIntegration(integration)
    }

    private fun updateDisplayMode(type: DisplayMode) {
        selectedDisplayMode = type
        DemoSessionConfiguration.setDisplayMode(type)
    }

    fun updatePlacementId(pid: String) {
        _placementId.value = pid
        DemoSessionConfiguration.setPlacementId(pid)
    }

    fun updateWidgetId(widgetId: String) {
        _widgetId.value = widgetId
        DemoSessionConfiguration.setWidgetId(widgetId)
    }

    fun updateInstallationKey(installationKey: String) {
        _installationKey.value = installationKey
        DemoSessionConfiguration.setInstallationKey(installationKey)
    }

    private fun updateArticleUrl(articleUrl: String) {
        _articleUrl.value = articleUrl
        DemoSessionConfiguration.setArticleUrl(articleUrl)
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

    fun getDisplayModeChips(): List<ChipData> = getDisplayModes().mapIndexed { index, type ->
        ChipData(
            id = index,
            text = type.displayName,
            isSelected = selectedDisplayMode == type
        )
    }

    fun hasPlacementId(): Boolean {
        return listOf(ProviderType.DIRECT, ProviderType.ADMOB, ProviderType.APPLOVIN).contains(selectedProvider)
    }

    fun getInputMethod(): KeyboardType = when (selectedProvider to selectedFormat) {
        ProviderType.DIRECT to FormatType.MEDIA, ProviderType.DIRECT to FormatType.MEDIANATIVE -> KeyboardType.Number
        else -> KeyboardType.Text
    }

    fun getPidChips(): List<ChipData> = when (selectedProvider to selectedFormat) {
        ProviderType.DIRECT to FormatType.MEDIA -> getMediaPidChips()
        ProviderType.DIRECT to FormatType.MEDIANATIVE -> getMediaNativePidChips()
        ProviderType.ADMOB to FormatType.MEDIA -> getMediaAdmobPidChips()
        ProviderType.ADMOB to FormatType.MEDIANATIVE -> getMediaNativeAdmobPidChips()
        ProviderType.APPLOVIN to FormatType.MEDIA -> getMediaApplovinPidChips()
        ProviderType.APPLOVIN to FormatType.MEDIANATIVE -> getMediaNativeApplovinPidChips()
        else -> throw IllegalAccessException("Impossible combination")
    }


    private fun getMediaPidChips(): List<ChipData> = mediaPids.mapIndexed { index, (label, _) ->
        ChipData(
            id = index,
            text = label,
            isSelected = _placementId.value == mediaPids[index].second
        )
    }

    private fun getMediaNativePidChips(): List<ChipData> = mediaNativePids.mapIndexed { index, (label, _) ->
        ChipData(
            id = index,
            text = label,
            isSelected = _placementId.value == mediaNativePids[index].second
        )
    }

    private fun getMediaAdmobPidChips(): List<ChipData> = mediaPids.mapIndexed { index, (label, _) ->
        ChipData(
            id = index,
            text = label,
            isSelected = _placementId.value == mediaAdmobPids[index].second
        )
    }

    private fun getMediaNativeAdmobPidChips(): List<ChipData> = mediaNativeAdmobPids.mapIndexed { index, (label, _) ->
        ChipData(
            id = index,
            text = label,
            isSelected = _placementId.value == mediaNativeAdmobPids[index].second
        )
    }

    private fun getMediaApplovinPidChips(): List<ChipData> = mediaApplovinPids.mapIndexed { index, (label, _) ->
        ChipData(
            id = index,
            text = label,
            isSelected = _placementId.value == mediaApplovinPids[index].second
        )
    }

    private fun getMediaNativeApplovinPidChips(): List<ChipData> = mediaNativeApplovinPids.mapIndexed { index, (label, _) ->
        ChipData(
            id = index,
            text = label,
            isSelected = _placementId.value == mediaNativeApplovinPids[index].second
        )
    }

    fun getIntegrationChips(): List<ChipData> {

        return getFilteredIntegrationList().mapIndexed { index, integration ->
            ChipData(
                id = index,
                text = integration.displayName,
                isSelected = selectedIntegration == integration
            )
        }
    }

    private fun getFilteredIntegrationList(): List<IntegrationType> {
        return when {
            selectedProvider == ProviderType.DIRECT
                    && selectedFormat in listOf(FormatType.FEED, FormatType.MEDIA)
                    && selectedDisplayMode in listOf(DisplayMode.FEED_WITH_MEDIA, DisplayMode.MEDIA_WITH_FEED) -> singleIntegrationType

            selectedProvider == ProviderType.ADMOB
                    || selectedProvider == ProviderType.SMART
                    || selectedProvider == ProviderType.APPLOVIN
                    || selectedProvider == ProviderType.PREBID -> partialIntegrationTypes

            else -> fullIntegrationTypes
        }
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
            updateDefaultsByFormat(format)
        }
    }

    fun onProviderChipClick(index: Int) {
        val availableProviders = getProviders()
        if (index in availableProviders.indices) {
            val provider = availableProviders[index]
            updateProvider(provider)
            updatePlacementConfiguration()
        }
    }

    fun onDisplayModeChipClick(index: Int) {
        val availableTypes = getDisplayModes()
        if (index in availableTypes.indices) {
            val type = availableTypes[index]
            updateDisplayMode(type)
        }
    }

    fun onPidChipClick(index: Int) = when (selectedProvider to selectedFormat) {
        ProviderType.DIRECT to FormatType.MEDIA -> onMediaPidChipClick(index)
        ProviderType.DIRECT to FormatType.MEDIANATIVE -> onMediaNativePidChipClick(index)
        ProviderType.ADMOB to FormatType.MEDIA -> onMediaAdmobPidChipClick(index)
        ProviderType.ADMOB to FormatType.MEDIANATIVE -> onMediaNativeAdmobPidChipClick(index)
        ProviderType.APPLOVIN to FormatType.MEDIA -> onMediaApplovinPidChipClick(index)
        ProviderType.APPLOVIN to FormatType.MEDIANATIVE -> onMediaNativeApplovinPidChipClick(index)
        ProviderType.SMART to FormatType.MEDIA -> TODO("wip")
        ProviderType.SMART to FormatType.MEDIANATIVE -> TODO("wip")
        ProviderType.PREBID to FormatType.MEDIA -> TODO("wip")
        ProviderType.PREBID to FormatType.MEDIANATIVE -> TODO("wip")
        else -> throw IllegalAccessException("Impossible combination")
    }


    private fun onMediaPidChipClick(index: Int) {
        if (index in mediaPids.indices) {
            val pid = mediaPids[index].second
            updatePlacementId(pid)
        }
    }

    private fun onMediaNativePidChipClick(index: Int) {
        if (index in mediaNativePids.indices) {
            val pid = mediaNativePids[index].second
            updatePlacementId(pid)
        }
    }

    private fun onMediaAdmobPidChipClick(index: Int) {
        if (index in mediaAdmobPids.indices) {
            val pid = mediaAdmobPids[index].second
            updatePlacementId(pid)
        }
    }

    private fun onMediaNativeAdmobPidChipClick(index: Int) {
        if (index in mediaNativeAdmobPids.indices) {
            val pid = mediaNativeAdmobPids[index].second
            updatePlacementId(pid)
        }
    }

    private fun onMediaApplovinPidChipClick(index: Int) {
        if (index in mediaApplovinPids.indices) {
            val pid = mediaApplovinPids[index].second
            updatePlacementId(pid)
        }
    }

    private fun onMediaNativeApplovinPidChipClick(index: Int) {
        if (index in mediaNativeApplovinPids.indices) {
            val pid = mediaNativeApplovinPids[index].second
            updatePlacementId(pid)
        }
    }

    fun onFeedWidgetIdChipClick(index: Int) {
        if (index in feedWidgetIds.indices) {
            val widgetId = feedWidgetIds[index].second
            updateWidgetId(widgetId)
        }
    }

    fun onRecommendationsWidgetIdChipClick(index: Int) {
        if (index in recommendationsWidgetIds.indices) {
            val widgetId = recommendationsWidgetIds[index].second
            updateWidgetId(widgetId)
        }
    }

    fun onFeedInstallationKeyChipClick(index: Int) {
        if (index in feedInstallationKeys.indices) {
            val installationKey = feedInstallationKeys[index].second
            updateInstallationKey(installationKey)
        }
    }

    fun onRecommendationsInstallationKeyChipClick(index: Int) {
        if (index in recommendationsInstallationKeys.indices) {
            val installationKey = recommendationsInstallationKeys[index].second
            updateInstallationKey(installationKey)
        }
    }

    fun onArticleUrlChange(articleUrl: String) {
        updateArticleUrl(articleUrl)
    }

    fun onIntegrationChipClick(index: Int) {
        val integrationList = getFilteredIntegrationList()

        if (index in integrationList.indices) {
            val integration = integrationList[index]
            updateIntegration(integration)
        }
    }
}

