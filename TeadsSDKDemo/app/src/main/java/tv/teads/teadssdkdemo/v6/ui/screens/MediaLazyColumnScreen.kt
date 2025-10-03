package tv.teads.teadssdkdemo.v6.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tv.teads.teadssdkdemo.v6.ui.components.AdContainer
import tv.teads.teadssdkdemo.v6.ui.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.components.ArticleImage
import tv.teads.teadssdkdemo.v6.ui.components.ArticleLazyItem
import tv.teads.teadssdkdemo.v6.ui.components.ArticleTitle

@Composable
fun MediaLazyColumnScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        item {
            ArticleImage()
        }
        
        item {
            ArticleLazyItem {
                ArticleTitle()
            }
        }
        
        item {
            ArticleLazyItem {
                ArticleBody()
            }
        }
        
        item {
            ArticleLazyItem {
                ArticleBody()
            }
        }
        
        item {
            ArticleLazyItem {
                ArticleBody()
            }
        }
        
        item {
            ArticleLazyItem {
                // Ad container
                AdContainer()
            }
        }
        
        item {
            ArticleLazyItem {
                ArticleBody()
            }
        }
    }
}
