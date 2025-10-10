package tv.teads.teadssdkdemo.v6.ui.compose

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementFeed
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementMedia
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementFeedConfig
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementMediaConfig
import tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate
import tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement
import tv.teads.teadssdkdemo.BuildConfig
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.ui.base.components.AdContainer
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleImage
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleSpacing
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle
import tv.teads.teadssdkdemo.v6.utils.BrowserNavigationHelper

@Composable
fun MediaFeedColumnScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var mediaAdView by remember { mutableStateOf<ViewGroup?>(null) }
    var mediaAd by remember { mutableStateOf<TeadsAdPlacementMedia?>(null) }

    var feedAdView by remember { mutableStateOf<ViewGroup?>(null) }
    var feedAd by remember { mutableStateOf<TeadsAdPlacementFeed?>(null) }

    LaunchedEffect(Unit) {
        fun initMediaAdView() {
            // Init configuration
            val config = TeadsAdPlacementMediaConfig(
                pid = DemoSessionConfiguration.getPlacementIdOrDefault().toInt(), // Your unique placement id
                articleUrl = DemoSessionConfiguration.getArticleUrlOrDefault().toUri() // Your article url
            )

            // Create placement
            mediaAd = TeadsAdPlacementMedia(
                context = context,
                config = config,
                delegate = object : TeadsAdPlacementEventsDelegate {
                    override fun onPlacementEvent(
                        placement: TeadsAdPlacement<*, *>,
                        event: TeadsAdPlacementEventName,
                        data: Map<String, Any>?
                    ) {
                        // Stay tuned to lifecycle events
                        Log.d("MediaFeedColumnScreen", "$placement - $event: $data")
                    }
                }
            )

            // Request ad
            val loadedAdView = mediaAd?.loadAd()
            mediaAdView = loadedAdView
        }

        fun initFeedAdview() {
            // Init configuration
            val config = TeadsAdPlacementFeedConfig(
                widgetId = DemoSessionConfiguration.getWidgetIdOrDefault(), // Your unique widget id
                articleUrl = DemoSessionConfiguration.getArticleUrlOrDefault().toUri(), // Your article url
                installationKey = DemoSessionConfiguration.getInstallationKeyOrDefault(), // Your unique installation key
                widgetIndex = 0 // Position of the ad within your article contenIncrement the number by 1 for each additional ad
            )

            // Create placement
            feedAd = TeadsAdPlacementFeed(
                context = context,
                config = config,
                delegate = object : TeadsAdPlacementEventsDelegate {
                    override fun onPlacementEvent(
                        placement: TeadsAdPlacement<*, *>,
                        event: TeadsAdPlacementEventName,
                        data: Map<String, Any>?
                    ) {
                        // Stay tuned to lifecycle events
                        Log.d("MediaFeedColumnScreen", "$placement - $event: $data")

                        if (event == TeadsAdPlacementEventName.CLICKED_ORGANIC) {
                            val url = data?.get("url") as? String
                            url?.let {
                                BrowserNavigationHelper.openInnerBrowser(context, it)
                            }
                        }
                    }
                }
            )

            // Request ad
            val loadedAdView = feedAd?.loadAd()
            feedAdView = loadedAdView
        }

        // Init SDK
        initTeadsSDK(context)

        // Init ad placements
        initMediaAdView()
        initFeedAdview()
    }

    // Cleanup when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            // Clean from memory
            mediaAd?.clean()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ArticleImage()
        ArticleSpacing()
        ArticleTitle()
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_a))
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_b))
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_c))
        ArticleSpacing()
        
        // Add media in the ad container
        AdContainer(adView = mediaAdView)
        
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_d))
        ArticleBody(text = stringResource(R.string.article_template_body_d))

        // Add feed in the ad container
        AdContainer(adView = feedAdView)
    }
}

/**
 * Initialize TeadsSDK - can be init once on the start of the app
 */
private fun initTeadsSDK(context: Context) {
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
