package tv.teads.teadssdkdemo.v6.ui.base.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import tv.teads.teadssdkdemo.v6.domain.FormatType

/**
 * A reusable component that displays italic description text for different ad formats.
 * The description changes reactively based on the selected format.
 */
@Composable
fun FormatDescription(
    selectedFormat: FormatType?,
    modifier: Modifier = Modifier
) {
    val description = when (selectedFormat) {
        FormatType.MEDIA -> "— Teads' signature outstream format. Video appears inside editorial content, starts only when 50% visible, pauses when out of view. Built for attention without intrusion."
        FormatType.MEDIANATIVE -> "— Ads appear natively inside editorial or social feeds, matching the look and feel of surrounding content."
        FormatType.FEED -> "— Content Discovery Feed — A recommendation grid of articles or videos (\"You might also like\"). Drives engagement via contextual relevance."
        FormatType.RECOMMENDATIONS -> "— A native grid of sponsored and organic stories (\"You might also like\") that blends with the publisher's content layout. Drives engagement by fitting contextually and visually into the reading experience."
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
