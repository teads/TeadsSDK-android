package tv.teads.teadssdkdemo.v6.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tv.teads.teadssdkdemo.v6.data.CreativeType
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
    
    private val creativeTypes = listOf(
        CreativeType.LANDSCAPE,
        CreativeType.VERTICAL,
        CreativeType.SQUARE,
        CreativeType.CAROUSEL
    )
    
    private val integrationTypes = listOf(
        IntegrationType.COLUMN,
        IntegrationType.LAZYCOLUMN,
        IntegrationType.SCROLLVIEW,
        IntegrationType.RECYCLERVIEW
    )

    // Selected states
    var selectedFormat: FormatType? by mutableStateOf(null)
        private set
    
    var selectedProvider: ProviderType? by mutableStateOf(null)
        private set
    
    var selectedCreativeType: CreativeType? by mutableStateOf(null)
        private set
    
    var selectedIntegration: IntegrationType? by mutableStateOf(null)
        private set

    // Custom PID
    private val _customPid = MutableStateFlow("")
    val customPid: StateFlow<String> = _customPid.asStateFlow()

    // Functions to get static lists
    fun getFormats() = formatTypes
    fun getProviders() = providerTypes
    fun getCreativeTypes() = creativeTypes
    fun getIntegrationTypes() = integrationTypes
    
    fun getCurrentPid(): String = _customPid.value

    // Update functions
    fun updateFormat(format: FormatType) {
        selectedFormat = format
    }
    
    fun updateProvider(provider: ProviderType) {
        selectedProvider = provider
    }
    
    fun updateCreativeType(creativeType: CreativeType) {
        selectedCreativeType = creativeType
        // Update PID based on creative type
        val pid = when (creativeType) {
            CreativeType.LANDSCAPE -> "landscape-pid"
            CreativeType.VERTICAL -> "vertical-pid" 
            CreativeType.SQUARE -> "square-pid"
            CreativeType.CAROUSEL -> "carousel-pid"
        }
        _customPid.value = pid
    }
    
    fun updateIntegration(integration: IntegrationType) {
        selectedIntegration = integration
    }
    
    fun updateCustomPid(pid: String) {
        _customPid.value = pid
    }

    // Chip data helper methods
    fun getFormatChips(): List<ChipData> = formatTypes.mapIndexed { index, format ->
        ChipData(
            id = index,
            text = format.displayName,
            isSelected = selectedFormat == format
        )
    }

    fun getProviderChips(): List<ChipData> = providerTypes.mapIndexed { index, provider ->
        ChipData(
            id = index,
            text = provider.displayName,
            isSelected = selectedProvider == provider
        )
    }

    fun getCreativeTypeChips(): List<ChipData> = creativeTypes.mapIndexed { index, creativeType ->
        ChipData(
            id = index,
            text = creativeType.displayName,
            isSelected = selectedCreativeType == creativeType
        )
    }

    fun getIntegrationChips(): List<ChipData> = integrationTypes.mapIndexed { index, integration ->
        ChipData(
            id = index,
            text = integration.displayName,
            isSelected = selectedIntegration == integration
        )
    }

    // Chip click handlers
    fun onFormatChipClick(index: Int) {
        if (index in formatTypes.indices) {
            selectedFormat = formatTypes[index]
        }
    }

    fun onProviderChipClick(index: Int) {
        if (index in providerTypes.indices) {
            selectedProvider = providerTypes[index]
        }
    }

    fun onCreativeTypeChipClick(index: Int) {
        if (index in creativeTypes.indices) {
            updateCreativeType(creativeTypes[index])
        }
    }

    fun onIntegrationChipClick(index: Int) {
        if (index in integrationTypes.indices) {
            selectedIntegration = integrationTypes[index]
        }
    }
}
