package tv.teads.teadssdkdemo.v6.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DemoScreen(
    modifier: Modifier = Modifier,
    viewModel: DemoViewModel = viewModel(),
    onLaunch: () -> Unit = {}
) {
    // Collect StateFlow values for reactive text fields
    val placementId by viewModel.placementId.collectAsState()
    val widgetId by viewModel.widgetId.collectAsState()
    val installationKey by viewModel.installationKey.collectAsState()
    val articleUrl by viewModel.articleUrl.collectAsState()

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

        // Placement ID Configuration Section
        Section(title = "Placement ID Configuration") {
            Column {
                when (viewModel.selectedFormat) {
                    FormatType.MEDIA -> {
                        // Article URL Text Field (first)
                        DemoTextField(
                            value = articleUrl,
                            onValueChange = viewModel::onArticleUrlChange,
                            label = "Article URL",
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        // Placement ID Text Field
                        DemoTextField(
                            value = placementId,
                            onValueChange = viewModel::updatePlacementId,
                            label = "Placement ID",
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        // Media PID Chips below Placement ID
                        ChipGroup(
                            chips = viewModel.getMediaPidChips(),
                            onChipClick = viewModel::onMediaPidChipClick
                        )
                    }
                    FormatType.MEDIANATIVE -> {
                        // Article URL Text Field (first)
                        DemoTextField(
                            value = articleUrl,
                            onValueChange = viewModel::onArticleUrlChange,
                            label = "Article URL",
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        // Placement ID Text Field
                        DemoTextField(
                            value = placementId,
                            onValueChange = viewModel::updatePlacementId,
                            label = "Placement ID",
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        // Media Native PID Chips below Placement ID
                        ChipGroup(
                            chips = viewModel.getMediaNativePidChips(),
                            onChipClick = viewModel::onMediaNativePidChipClick
                        )
                    }
                    FormatType.FEED -> {
                        // Article URL Text Field (first)
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
                        
                        // Feed Widget ID Chips below Widget ID
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
                        
                        // Feed Installation Key Chips below Installation Key
                        
                        ChipGroup(
                            chips = viewModel.getFeedInstallationKeyChips(),
                            onChipClick = viewModel::onFeedInstallationKeyChipClick
                        )
                    }
                    FormatType.RECOMMENDATIONS -> {
                        // Article URL Text Field (first)
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
                        
                        // Recommendations Widget ID Chips below Widget ID
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
                        
                        // Recommendations Installation Key Chips below Installation Key
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

        // Integrations Section
        Section(title = "Integrations") {
            ChipGroup(
                chips = viewModel.getIntegrationChips(),
                onChipClick = viewModel::onIntegrationChipClick
            )
        }

        // LAUNCH Button
        Button(
            onClick = onLaunch,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("LAUNCH")
        }
    }
}