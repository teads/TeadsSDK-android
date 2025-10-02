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
}
