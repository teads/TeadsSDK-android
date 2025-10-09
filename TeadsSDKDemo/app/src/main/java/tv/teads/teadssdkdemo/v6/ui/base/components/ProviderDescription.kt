package tv.teads.teadssdkdemo.v6.ui.base.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import tv.teads.teadssdkdemo.v6.domain.ProviderType

/**
 * A reusable component that displays italic description text for different ad providers.
 * The description changes reactively based on the selected provider.
 */
@Composable
fun ProviderDescription(
    selectedProvider: ProviderType?,
    modifier: Modifier = Modifier
) {
    val description = when (selectedProvider) {
        ProviderType.DIRECT -> "— Serve ads directly from Teads without mediation or bidding layers."
        ProviderType.ADMOB -> "— Google's mediation platform connecting multiple networks. Integrate via adapters to maximize fill and yield."
        ProviderType.APPLOVIN -> "— Monetization and user acquisition platform with powerful mediation and A/B optimization."
        ProviderType.SMART -> "— Full-stack SSP offering header bidding and programmatic integrations. Ideal for premium publishers."
        ProviderType.PREBID -> "— Open-source header bidding solution giving transparency and unified auctions across demand partners."
        null -> ""
    }

    if (description.isNotEmpty()) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontStyle = FontStyle.Italic,
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Start,
            modifier = modifier
        )
    }
}
