package tv.teads.teadssdkdemo.v6.ui.compose

import android.util.Log
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
import tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementMediaNative
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementMediaNativeConfig
import tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate
import tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.ui.base.components.AdContainer
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleImage
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleLazyItem
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle
import tv.teads.teadssdkdemo.views.MediaNativeAdView

@Composable
fun MediaNativeLazyColumnScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var mediaNativeAdView by remember { mutableStateOf<MediaNativeAdView?>(null) }
    var mediaNativeAd by remember { mutableStateOf<TeadsAdPlacementMediaNative?>(null) }

    LaunchedEffect(Unit) {
        // 1. Init configuration
        val config = TeadsAdPlacementMediaNativeConfig(
            pid = DemoSessionConfiguration.getPlacementIdOrDefault().toInt(), // Your unique placement id
            articleUrl = DemoSessionConfiguration.getArticleUrlOrDefault().toUri() // Your article url
        )

        // 2. Create placement
        mediaNativeAd = TeadsAdPlacementMediaNative(
            context = context,
            config = config,
            delegate = object : TeadsAdPlacementEventsDelegate {
                override fun onPlacementEvent(
                    placement: TeadsAdPlacement<*, *>,
                    event: TeadsAdPlacementEventName,
                    data: Map<String, Any>?
                ) {
                    // 5. Stay tuned to lifecycle events
                    Log.d("MediaNativeLazyColumnScreen", "$placement - $event: $data")
                    
                    if (event == TeadsAdPlacementEventName.READY) {
                        mediaNativeAdView?.isVisible(true)
                    }
                }
            }
        )

        // 3. Create your custom media native ad view
        mediaNativeAdView = MediaNativeAdView(context)

        // 4. Load the ad and bind it to the native ad view
        mediaNativeAd
            ?.loadAd()
            ?.let { binder ->
                binder(mediaNativeAdView!!.getNativeAdView())
            }
    }

    DisposableEffect(Unit) {
        onDispose {
            // 6. Clean from memory
            mediaNativeAd?.clean()
        }
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
                // 7. Add in the ad container
                AdContainer(adView = mediaNativeAdView)
            }
        }
        
        item {
            ArticleLazyItem {
                ArticleBody(text = stringResource(R.string.article_template_body_d))
            }
        }
    }
}
