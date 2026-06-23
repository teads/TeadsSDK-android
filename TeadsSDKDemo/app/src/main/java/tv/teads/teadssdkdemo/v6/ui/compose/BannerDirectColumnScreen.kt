package tv.teads.teadssdkdemo.v6.ui.compose

import android.util.Log
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementBanner
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementBannerConfig
import tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate
import tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement
import tv.teads.teadssdkdemo.BuildConfig
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleLabel
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleSpacing
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle
import tv.teads.teadssdkdemo.v6.utils.BrowserNavigationHelper

private const val TAG = "BannerDirectColumn"

@Composable
fun BannerDirectColumnScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val context = LocalContext.current
    var adView by remember { mutableStateOf<ViewGroup?>(null) }
    var bannerAd by remember { mutableStateOf<TeadsAdPlacementBanner?>(null) }

    LaunchedEffect(Unit) {
        // 0. Init SDK
        initTeadsSDK(context)

        // 1. Init configuration
        val config = TeadsAdPlacementBannerConfig(
            articleUrl = DemoSessionConfiguration.getArticleUrlOrDefault().toUri(), // Your article url
            widgetId = DemoSessionConfiguration.getWidgetIdOrDefault(), // Your unique banner widget id
            installationKey = DemoSessionConfiguration.getInstallationKeyOrDefault(), // Your unique installation key
            widgetIndex = 0 // Position of the ad within your article. Increment by 1 for each additional ad on the same page
        )

        // 2. Create placement
        bannerAd = TeadsAdPlacementBanner(
            context = context,
            config = config,
            delegate = object : TeadsAdPlacementEventsDelegate {
                override fun onPlacementEvent(
                    placement: TeadsAdPlacement<*, *>,
                    event: TeadsAdPlacementEventName,
                    data: Map<String, Any>?
                ) {
                    // 4. Stay tuned to lifecycle events
                    Log.d(TAG, "$placement - $event: $data")

                    if (event == TeadsAdPlacementEventName.CLICKED_ORGANIC) {
                        val url = data?.get("url") as? String
                        url?.let {
                            BrowserNavigationHelper.openInnerBrowser(context, it)
                        }
                    }
                }
            }
        )

        // 3. Request ad — anchored banner sizing is owned by the JS engine,
        //    container should be wrap_content (height reported via HEIGHT_UPDATED)
        adView = bannerAd?.loadAd()
    }

    // 5. Render the article in a Scaffold whose bottomBar pins the banner to the
    //    bottom of the screen (mirrors the combinedsdk-demo Column use case).
    //    No topBar — the parent activity already supplies one.
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            adView?.let { view ->
                AndroidView(
                    factory = { view },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = contentPadding.calculateBottomPadding())
                )
            }
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding())
                .padding(bottom = scaffoldPadding.calculateBottomPadding())
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
        ) {
            ArticleLabel()
            ArticleSpacing()
            ArticleTitle()
            ArticleSpacing()
            ArticleBody(text = stringResource(R.string.article_template_body_a))
            ArticleSpacing()
            ArticleBody(text = stringResource(R.string.article_template_body_b))
            ArticleSpacing()
            ArticleBody(text = stringResource(R.string.article_template_body_c))
            ArticleSpacing()
            ArticleBody(text = stringResource(R.string.article_template_body_d))
            ArticleSpacing()
            ArticleBody(text = stringResource(R.string.article_template_body_e))
        }
    }
}

/**
 * Initialize TeadsSDK - can be init once on the start of the app
 */
private fun initTeadsSDK(context: android.content.Context) {
    // Mandatory for placements [Feed, Recommendations, Banner]
    TeadsSDK.configure(
        applicationContext = context.applicationContext,
        appKey = "AndroidSampleApp2014" // Your unique application key
    )

    // For testing purposes
    if (BuildConfig.DEBUG) {
        TeadsSDK.testMode = true // Enable more logging visibility
        TeadsSDK.testLocation = "us" // Emulates location for placements [Feed, Recommendations, Banner]
    }
}
