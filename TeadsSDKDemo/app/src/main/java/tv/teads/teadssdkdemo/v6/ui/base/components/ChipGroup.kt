package tv.teads.teadssdkdemo.v6.ui.base.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@androidx.compose.foundation.ExperimentalFoundationApi
@Composable
fun ChipGroup(
    modifier: Modifier = Modifier,
    chips: List<ChipData>,
    onChipClick: (Int) -> Unit
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        chips.forEach { chip ->
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