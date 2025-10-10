package tv.teads.teadssdkdemo.v6.ui.compose

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementRecommendations
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementRecommendationsConfig
import tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate
import tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement
import tv.teads.teadssdkdemo.BuildConfig
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.ui.base.components.AdContainer
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleLabel
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleLazyItem
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle
import tv.teads.teadssdkdemo.v6.ui.base.recommendations.RecommendationsAdView

@Composable
fun RecommendationsLazyColumnScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var recommendationsAdView by remember { mutableStateOf<RecommendationsAdView?>(null) }
    var recommendationsAd by remember { mutableStateOf<TeadsAdPlacementRecommendations?>(null) }

    LaunchedEffect(Unit) {
        // 0. Init SDK
        initTeadsSDK(context)
        
        // 1. Init configuration
        val config = TeadsAdPlacementRecommendationsConfig(
            articleUrl = DemoSessionConfiguration.getArticleUrlOrDefault().toUri(), // Your article url
            widgetId = DemoSessionConfiguration.getWidgetIdOrDefault() // Your widget id
        )

        // 2. Create placement
        recommendationsAd = TeadsAdPlacementRecommendations(
            config = config,
            delegate = object : TeadsAdPlacementEventsDelegate {
                override fun onPlacementEvent(
                    placement: TeadsAdPlacement<*, *>,
                    event: TeadsAdPlacementEventName,
                    data: Map<String, Any>?
                ) {
                    // 3. Stay tuned to lifecycle events
                    Log.d("RecommendationsLazyColumnScreen", "$placement - $event: $data")
                }
            }
        )

        // 4. Create your custom recommendations ad view
        recommendationsAdView = RecommendationsAdView(context)

        // 5. Load the recommendations asynchronously and bind to view
        try {
            val response = recommendationsAd!!.loadAdSuspend()
            recommendationsAdView!!.bind(
                recommendations = response,
                articleUrl = config.articleUrl
            )
        } catch (e: Exception) {
            Log.e("RecommendationsLazyColumnScreen", "Recommendations failed to load", e)
        }
    }


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
            ArticleLabel()
        }
        
        item {
            ArticleLazyItem {
                ArticleTitle()
            }
        }
        
        item {
            ArticleLazyItem {
                ArticleBody(text = stringResource(R.string.article_template_body_a))
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
                // 6. Add in the ad container
                AdContainer(adView = recommendationsAdView)
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
