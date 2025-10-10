package tv.teads.teadssdkdemo.v6.ui.base

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tv.teads.teadssdkdemo.v6.ui.base.components.ChipGroup
import tv.teads.teadssdkdemo.v6.ui.base.components.DemoTextField
import tv.teads.teadssdkdemo.v6.ui.base.components.FormatDescription
import tv.teads.teadssdkdemo.v6.ui.base.components.ProviderDescription
import tv.teads.teadssdkdemo.v6.ui.base.components.Section
import tv.teads.teadssdkdemo.v6.ui.base.components.TeadsButton

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DemoScreen(
    modifier: Modifier = Modifier,
    viewModel: DemoViewModel = viewModel()
) {
    // Collect StateFlow values for reactive text fields
    val placementId by viewModel.placementId.collectAsState()
    val widgetId by viewModel.widgetId.collectAsState()
    val installationKey by viewModel.installationKey.collectAsState()
    val articleUrl by viewModel.articleUrl.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(viewModel.scrollState)
    ) {
        // Format Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Section(title = "Format") {
                ChipGroup(
                    chips = viewModel.getFormatChips(),
                    onChipClick = viewModel::onFormatChipClick
                )

                // Format Description
                FormatDescription(
                    selectedFormat = viewModel.selectedFormat
                )
            }
        }

        // Provider Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.08f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Section(title = "Provider") {
                ChipGroup(
                    chips = viewModel.getProviderChips(),
                    onChipClick = viewModel::onProviderChipClick
                )

                // Provider Description
                ProviderDescription(
                    selectedProvider = viewModel.selectedProvider
                )
            }
        }

        // Placement ID Configuration Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Section(title = "Placement Configuration") {

                // ##### init collapsable area
                Column {
                    // Article URL Text Field
                    DemoTextField(
                        value = articleUrl,
                        onValueChange = viewModel::onArticleUrlChange,
                        label = "Article URL",
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (viewModel.hasPlacementId()) {
                        // Placement ID Text Field
                        DemoTextField(
                            value = placementId,
                            onValueChange = viewModel::updatePlacementId,
                            label = "Placement ID",
                            keyboardType = viewModel.getInputMethod(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // PID Chips
                        ChipGroup(
                            chips = viewModel.getPidChips(),
                            onChipClick = viewModel::onPidChipClick
                        )
                    }

                    if (viewModel.hasWidgetId()) {
                        // Widget ID Text Field
                        DemoTextField(
                            value = widgetId,
                            onValueChange = viewModel::updateWidgetId,
                            label = "Widget ID",
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Widget ID Chips
                        ChipGroup(
                            chips = viewModel.getWidgetChips(),
                            onChipClick = viewModel::onWidgetChipClick
                        )

                        DemoTextField(
                            value = installationKey,
                            onValueChange = viewModel::updateInstallationKey,
                            label = "Installation Key",
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Feed Installation Key Chips
                        ChipGroup(
                            chips = viewModel.getInstallationKeyChips(),
                            onChipClick = viewModel::onInstallationKeyChipClick
                        )
                    }

                    // ##### finish collapsable area

                    // Display Mode Section (for Prebid provider or Media/Feed with Direct)
                    if (viewModel.shouldShowDisplayModeSection()) {
                        Section(
                            title = "Display Mode",
                            modifier = Modifier.padding(top = 18.dp)
                        ) {
                            ChipGroup(
                                chips = viewModel.getDisplayModeChips(),
                                onChipClick = viewModel::onDisplayModeChipClick
                            )
                        }
                    }
                }
            }
        }

        // Integrations Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.08f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Section(title = "Integrations") {
                ChipGroup(
                    chips = viewModel.getIntegrationChips(),
                    onChipClick = viewModel::onIntegrationChipClick
                )
            }
        }

        // LAUNCH Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            TeadsButton(
                text = "LAUNCH ARTICLE",
                onClick = { viewModel.launchNavigation() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}