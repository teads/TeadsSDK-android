@file:OptIn(ExperimentalFoundationApi::class)

package tv.teads.teadssdkdemo.v6.ui.base.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Section(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        content()
    }
}

@Preview
@Composable
private fun SectionPreview() {
    MaterialTheme {
        Section(
            title = "Sample Section",
            modifier = Modifier.padding(16.dp)
        ) {
            Text("This is sample content inside the section.")
            Text("Multiple lines can be displayed here.")
        }
    }
}

@Preview
@Composable
private fun SectionWithChipsPreview() {
    MaterialTheme {
        Section(
            title = "Format Selection",
            modifier = Modifier.padding(16.dp)
        ) {
            ChipGroup(
                chips = listOf(
                    ChipData(1, "Media", true),
                    ChipData(2, "Feed", false),
                    ChipData(3, "Recommendations", false)
                ),
                onChipClick = {}
            )
        }
    }
}
