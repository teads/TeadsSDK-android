package tv.teads.teadssdkdemo.v6.ui.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.ui.base.components.AdContainer
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleImage
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleLazyItem
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle

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
                ArticleBody(text = stringResource(R.string.article_template_body))
            }
        }
        
        item {
            ArticleLazyItem {
                ArticleBody(text = stringResource(R.string.article_template_body_2))
            }
        }
        
        item {
            ArticleLazyItem {
                ArticleBody(text = stringResource(R.string.article_template_body_3))
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
                ArticleBody(text = stringResource(R.string.article_template_body_4))
            }
        }
    }
}
