@file:OptIn(ExperimentalFoundationApi::class)

package tv.teads.teadssdkdemo.v6.ui.base.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CollapsibleSection(
    title: String,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header with title and expand/collapse icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleExpanded() }
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            
            val rotation by animateFloatAsState(
                targetValue = if (isExpanded) 180f else 0f,
                animationSpec = tween(durationMillis = 300),
                label = "icon_rotation"
            )
            
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.rotate(rotation)
                )
            }
        }
        
        // Collapsible content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(durationMillis = 300)
                )
        ) {
            if (isExpanded) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun CollapsibleSectionExpandedPreview() {
    MaterialTheme {
        CollapsibleSection(
            title = "Placement Configuration",
            isExpanded = true,
            onToggleExpanded = {},
            modifier = Modifier.padding(16.dp)
        ) {
            Text("This is expanded content inside the collapsible section.")
            Text("Multiple lines can be displayed here.")
            Text("The content will animate when collapsed.")
        }
    }
}

@Preview
@Composable
private fun CollapsibleSectionCollapsedPreview() {
    MaterialTheme {
        CollapsibleSection(
            title = "Placement Configuration",
            isExpanded = false,
            onToggleExpanded = {},
            modifier = Modifier.padding(16.dp)
        ) {
            Text("This content is hidden when collapsed.")
        }
    }
}
