package tv.teads.teadssdkdemo.v6.ui.base.components

import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import tv.teads.teadssdkdemo.R

/**
 * Article image placeholder
 */
@Composable
fun ArticleImage(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.image_article),
        contentDescription = "Article image",
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        contentScale = ContentScale.Crop
    )
}

/**
 * Article title
 */
@Composable
fun ArticleTitle(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.article_template_title)
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp
        ),
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    )
}

/**
 * Article body paragraphs
 */
@Composable
fun ArticleBody(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.article_template_body_a)
) {
    Text(
        text = text,
        textAlign = TextAlign.Justify,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontFamily = FontFamily.SansSerif,
            fontSize = 20.sp
        ),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
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
    if (adView != null) {
        AndroidView(
            factory = { adView },
            modifier = modifier.fillMaxWidth()
        )
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
