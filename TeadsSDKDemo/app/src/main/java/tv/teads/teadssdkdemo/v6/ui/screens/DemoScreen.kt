package tv.teads.teadssdkdemo.v6.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tv.teads.teadssdkdemo.v6.domain.DemoViewModel
import tv.teads.teadssdkdemo.v6.domain.FormatType
import tv.teads.teadssdkdemo.v6.ui.components.ChipGroup
import tv.teads.teadssdkdemo.v6.ui.components.DemoTextField
import tv.teads.teadssdkdemo.v6.ui.components.Section
import tv.teads.teadssdkdemo.v6.ui.components.TeadsButton

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
            .verticalScroll(rememberScrollState())
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
                Column {
                    when (viewModel.selectedFormat) {
                        FormatType.MEDIA, FormatType.MEDIANATIVE -> {
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
                        }
                        FormatType.FEED -> {
                            // Article URL Text Field
                            DemoTextField(
                                value = articleUrl,
                                onValueChange = viewModel::onArticleUrlChange,
                                label = "Article URL",
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Widget ID Text Field
                            DemoTextField(
                                value = widgetId,
                                onValueChange = viewModel::updateWidgetId,
                                label = "Widget ID",
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Feed Widget ID Chips
                            ChipGroup(
                                chips = viewModel.getFeedWidgetIdChips(),
                                onChipClick = viewModel::onFeedWidgetIdChipClick
                            )

                            // Installation Key Text Field
                            DemoTextField(
                                value = installationKey,
                                onValueChange = viewModel::updateInstallationKey,
                                label = "Installation Key",
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Feed Installation Key Chips
                            ChipGroup(
                                chips = viewModel.getFeedInstallationKeyChips(),
                                onChipClick = viewModel::onFeedInstallationKeyChipClick
                            )
                        }
                        FormatType.RECOMMENDATIONS -> {
                            // Article URL Text Field
                            DemoTextField(
                                value = articleUrl,
                                onValueChange = viewModel::onArticleUrlChange,
                                label = "Article URL",
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Widget ID Text Field
                            DemoTextField(
                                value = widgetId,
                                onValueChange = viewModel::updateWidgetId,
                                label = "Widget ID",
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Recommendations Widget ID Chips
                            ChipGroup(
                                chips = viewModel.getRecommendationsWidgetIdChips(),
                                onChipClick = viewModel::onRecommendationsWidgetIdChipClick
                            )

                            // Installation Key Text Field
                            DemoTextField(
                                value = installationKey,
                                onValueChange = viewModel::updateInstallationKey,
                                label = "Installation Key",
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Recommendations Installation Key Chips
                            ChipGroup(
                                chips = viewModel.getRecommendationsInstallationKeyChips(),
                                onChipClick = viewModel::onRecommendationsInstallationKeyChipClick
                            )
                        }
                        else -> {
                            // No format selected
                            Text(
                                text = "Select a format to see placement configuration options",
                                modifier = Modifier.fillMaxWidth()
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