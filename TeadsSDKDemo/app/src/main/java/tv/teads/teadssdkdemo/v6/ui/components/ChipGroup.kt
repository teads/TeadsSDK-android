package tv.teads.teadssdkdemo.v6.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChipGroup(
    modifier: Modifier = Modifier,
    chips: List<ChipData>,
    onChipClick: (Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        items(chips) { chip ->
            TeadsChip(
                text = chip.text,
                isSelected = chip.isSelected,
                onSelectedChange = { onChipClick(chip.id) }
            )
        }
    }
}

data class ChipData(
    val id: Int,
    val text: String,
    val isSelected: Boolean = false
)