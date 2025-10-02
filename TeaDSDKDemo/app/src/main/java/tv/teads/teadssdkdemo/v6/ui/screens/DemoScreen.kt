package tv.teads.teadssdkdemo.v6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tv.teads.teadssdkdemo.v6.domain.DemoViewModel
import tv.teads.teadssdkdemo.v6.ui.components.*

@Composable
fun DemoScreen(
    viewModel: DemoViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val formats by viewModel.getFormats()
    val providers by viewModel.getProviders()
    val creativeTypes by viewModel.getCreativeTypes()
    val integrationTypes by viewModel.getIntegrationTypes()
    
    val selectedFormat by viewModel.selectedFormat
    val selectedProvider by viewModel.selectedProvider
    val selectedCreativeType by viewModel.selectedCreativeType
    val currentPid by viewModel.getCurrentPid()
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Format Section
        Section(
            title = "Formats"
        ) {
            ChipGroup(
                items = formats,
                selectedItem = selectedFormat,
                onItemSelected = { viewModel.updateFormat(it) },
                itemText = { it.displayName }
            )
        }
        
        // Provider Section
        Section(
            title = "Providers"
        ) {
            ChipGroup(
                items = providers,
                selectedItem = selectedProvider,
                onItemSelected = { viewModel.updateProvider(it) },
                itemText = { it.displayName }
            )
        }
        
        // Placement ID Section
        Section(
            title = "Placement ID"
        ) {
            TeadsButton(
                text = "Custom PID: $currentPid",
                onClick = { /* TODO: Implement PID custom dialog */ },
                variant = TeadsButtonVariant.OUTLINE
            )
        }
        
        // Creative Types Section
        Section(
            title = "Creative Types"
        ) {
            ChipGroup(
                items = creativeTypes,
                selectedItem = selectedCreativeType,
                onItemSelected = { viewModel.updateCreativeType(it) },
                itemText = { it.displayName }
            )
        }
        
        // Integrations Section
        Section(
            title = "Integrations"
        ) {
            integrationTypes.forEach { integrationType ->
                TeadsButton(
                    text = integrationType.displayName,
                    onClick = { /* TODO: Navigate to integration */ },
                    variant = TeadsButtonVariant.SECONDARY,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
        
        // Configuration Summary
        Section(
            title = "Configuration Summary"
        ) {
            Text(
                text = "Format: ${selectedFormat.displayName}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Provider: ${selectedProvider.displayName}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Creative Type: ${selectedCreativeType.displayName} (PID: ${selectedCreativeType.pid})",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Current PID: $currentPid",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
