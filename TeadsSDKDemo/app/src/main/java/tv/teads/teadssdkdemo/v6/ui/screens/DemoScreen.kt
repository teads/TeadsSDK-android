package tv.teads.teadssdkdemo.v6.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tv.teads.teadssdkdemo.v6.domain.DemoViewModel
import tv.teads.teadssdkdemo.v6.ui.components.ChipGroup
import tv.teads.teadssdkdemo.v6.ui.components.Section
import tv.teads.teadssdkdemo.v6.ui.components.TeadsButton

@Composable
fun DemoScreen(
    modifier: Modifier = Modifier,
    viewModel: DemoViewModel = viewModel()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
            // Format Section
            Section(title = "Format") {
                ChipGroup(
                    chips = viewModel.getFormatChips(),
                    onChipClick = viewModel::onFormatChipClick
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // Provider Section  
            Section(title = "Provider") {
                ChipGroup(
                    chips = viewModel.getProviderChips(),
                    onChipClick = viewModel::onProviderChipClick
                )
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
                ChipGroup(
                    chips = viewModel.getCreativeTypeChips(),
                    onChipClick = viewModel::onCreativeTypeChipClick
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // Integrations Section
            Section(title = "Integrations") {
                ChipGroup(
                    chips = viewModel.getIntegrationChips(),
                    onChipClick = viewModel::onIntegrationChipClick
                )
            }
    }
}