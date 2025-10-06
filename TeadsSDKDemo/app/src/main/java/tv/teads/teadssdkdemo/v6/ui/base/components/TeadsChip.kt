package tv.teads.teadssdkdemo.v6.ui.base.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeadsChip(
    text: String,
    isSelected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    FilterChip(
        onClick = { onSelectedChange(!isSelected) },
        label = { Text(text) },
        selected = isSelected,
        modifier = modifier,
        enabled = enabled
    )
}

@Preview
@Composable
private fun TeadsChipPreview() {
    MaterialTheme {
        Column {
            TeadsChip(
                text = "Selected Chip",
                isSelected = true,
                onSelectedChange = {}
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TeadsChip(
                text = "Unselected Chip",
                isSelected = false,
                onSelectedChange = {}
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TeadsChip(
                text = "Disabled Chip",
                isSelected = false,
                onSelectedChange = {},
                enabled = false
            )
        }
    }
}