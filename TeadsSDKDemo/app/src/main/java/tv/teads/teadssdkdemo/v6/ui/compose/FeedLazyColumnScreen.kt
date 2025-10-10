package tv.teads.teadssdkdemo.v6.ui.compose

import android.util.Log
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import tv.teads.teadssdkdemo.BuildConfig
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementFeed
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementFeedConfig
import tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate
import tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.ui.base.components.AdContainer
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleImage
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleLazyItem
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle
import tv.teads.teadssdkdemo.v6.utils.BrowserNavigationHelper

@Composable
fun FeedLazyColumnScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var adView by remember { mutableStateOf<ViewGroup?>(null) }
    var feedAd by remember { mutableStateOf<TeadsAdPlacementFeed?>(null) }

    LaunchedEffect(Unit) {
        // 0. Init SDK
        initTeadsSDK(context)
        
        // 1. Init configuration
        val config = TeadsAdPlacementFeedConfig(
            widgetId = DemoSessionConfiguration.getWidgetIdOrDefault(), // Your unique widget id
            articleUrl = DemoSessionConfiguration.getArticleUrlOrDefault().toUri(), // Your article url
            installationKey = DemoSessionConfiguration.getInstallationKeyOrDefault(), // Your unique installation key
            widgetIndex = 0 // Position of the ad within your article content. Increment the number by 1 for each additional ad
        )

        // 2. Create placement
        feedAd = TeadsAdPlacementFeed(
            context = context,
            config = config,
            delegate = object : TeadsAdPlacementEventsDelegate {
                override fun onPlacementEvent(
                    placement: TeadsAdPlacement<*, *>,
                    event: TeadsAdPlacementEventName,
                    data: Map<String, Any>?
                ) {
                    // 5. Stay tuned to lifecycle events
                    Log.d("FeedLazyColumnScreen", "$placement - $event: $data")
                    
                    if (event == TeadsAdPlacementEventName.CLICKED_ORGANIC) {
                        val url = data?.get("url") as? String
                        url?.let {
                            BrowserNavigationHelper.openInnerBrowser(context, it)
                        }
                    }
                }
            }
        )

        // 3. Request ad
        adView = feedAd?.loadAd()
    }


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
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
                ArticleBody(text = stringResource(R.string.article_template_body_a), isBold = true)
            }
        }
        
        item {
            ArticleLazyItem {
                ArticleBody(text = stringResource(R.string.article_template_body_b))
            }
        }
        
        item {
            ArticleLazyItem {
                ArticleBody(text = stringResource(R.string.article_template_body_c))
            }
        }
        
        item {
            ArticleLazyItem {
                ArticleBody(text = stringResource(R.string.article_template_body_d))
            }
        }
        
        item {
            ArticleLazyItem {
                // 5. Add in the ad container
                AdContainer(adView = adView)
            }
        }
    }
}

/**
 * Initialize TeadsSDK - can be init once on the start of the app
 */
private fun initTeadsSDK(context: android.content.Context) {
    // Mandatory for placements [Feed, Recommendations]
    TeadsSDK.configure(
        applicationContext = context.applicationContext,
        appKey = "AndroidSampleApp2014" // Your unique application key
    )

    // For testing purposes
    if (BuildConfig.DEBUG) {
        TeadsSDK.testMode = true // Enable more logging visibility
        TeadsSDK.testLocation = "us" // Emulates location for placements [Feed, Recommendations]
    }
}
