package tv.teads.teadssdkdemo.v6.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tv.teads.teadssdkdemo.v6.ui.components.AdContainer
import tv.teads.teadssdkdemo.v6.ui.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.components.ArticleImage
import tv.teads.teadssdkdemo.v6.ui.components.ArticleSpacing
import tv.teads.teadssdkdemo.v6.ui.components.ArticleTitle

@Composable
fun MediaColumnScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ArticleImage()
        ArticleSpacing()
        ArticleTitle()
        ArticleSpacing()
        ArticleBody()
        ArticleSpacing()
        ArticleBody()
        ArticleSpacing()
        ArticleBody()
        ArticleSpacing()
        // Ad container
        AdContainer(
            onAdContainerClick = {
                // Placeholder for ad interaction
            }
        )
        ArticleSpacing()
        ArticleBody()
    }
}
