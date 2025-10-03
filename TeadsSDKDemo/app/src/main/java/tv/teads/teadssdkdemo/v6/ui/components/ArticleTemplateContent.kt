package tv.teads.teadssdkdemo.v6.ui.components

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Article image placeholder
 */
@Composable
fun ArticleImage(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Build,
            contentDescription = "Image placeholder",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Article title
 */
@Composable
fun ArticleTitle(
    modifier: Modifier = Modifier,
    title: String = "Lorem Ipsum Article Title"
) {
    Text(
        text = "Lorem Ipsum Article Title",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * Article body paragraphs
 */
@Composable
fun ArticleBody(
    modifier: Modifier = Modifier,
    text: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * Ad container - flexible container for Android Views
 */
@Composable
fun AdContainer(
    modifier: Modifier = Modifier,
    adView: ViewGroup? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        if (adView != null) {
            AndroidView(
                factory = { adView },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Utility composable for consistent spacing
 */
@Composable
fun ArticleSpacing(
    height: Int = 12
) {
    Spacer(modifier = Modifier.height(height.dp))
}

/**
 * Reusable LazyColumn item wrapper
 */
@Composable
fun ArticleLazyItem(
    modifier: Modifier = Modifier,
    paddingTop: Int = 12,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.padding(top = paddingTop.dp)
    ) {
        content()
    }
}
