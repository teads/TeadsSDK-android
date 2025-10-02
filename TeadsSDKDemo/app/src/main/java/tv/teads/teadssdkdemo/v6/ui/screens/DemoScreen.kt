package tv.teads.teadssdkdemo.v6.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tv.teads.teadssdkdemo.v6.domain.DemoViewModel
import tv.teads.teadssdkdemo.v6.ui.components.ChipGroup
import tv.teads.teadssdkdemo.v6.ui.components.Section
import tv.teads.teadssdkdemo.v6.ui.components.TeadsButton
import tv.teads.teadssdkdemo.v6.ui.components.TeadsChip

@Composable
fun DemoScreen(
    viewModel: DemoViewModel = viewModel()
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Format Section
            Section(title = "Format") {
                ChipGroup {
                    viewModel.getFormats().forEach { format ->
                        TeadsChip(
                            text = format.displayName,
                            isSelected = viewModel.selectedFormat == format,
                            onSelectedChange = { viewModel.updateFormat(format) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // Provider Section  
            Section(title = "Provider") {
                ChipGroup {
                    viewModel.getProviders().forEach { provider ->
                        TeadsChip(
                            text = provider.displayName,
                            isSelected = viewModel.selectedProvider == provider,
                            onSelectedChange = { viewModel.updateProvider(provider) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // Placement ID Section
            Section(title = "Placement ID") {
                TeadsButton(
                    text = "Custom PID",
                    onClick = { 
                        // TODO: Implement custom PID dialog
                    }
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // Creative Types Section
            Section(title = "Creative Types") {
                ChipGroup {
                    viewModel.getCreativeTypes().forEach { creativeType ->
                        TeadsChip(
                            text = creativeType.displayName,
                            isSelected = viewModel.selectedCreativeType == creativeType,
                            onSelectedChange = { viewModel.updateCreativeType(creativeType) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // Integrations Section
            Section(title = "Integrations") {
                ChipGroup {
                    viewModel.getIntegrationTypes().forEach { integration ->
                        TeadsChip(
                            text = integration.displayName,
                            isSelected = viewModel.selectedIntegration == integration,
                            onSelectedChange = { viewModel.updateIntegration(integration) }
                        )
                    }
                }
            }
        }
    }
}