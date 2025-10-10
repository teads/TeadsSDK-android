package tv.teads.teadssdkdemo.v6.ui.compose

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
import androidx.compose.ui.text.font.FontWeight
import androidx.core.net.toUri
import tv.teads.teadssdkdemo.BuildConfig
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementMedia
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementMediaConfig
import tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate
import tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.ui.base.components.AdContainer
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleImage
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleSpacing
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle

@Composable
fun MediaColumnScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var adView by remember { mutableStateOf<ViewGroup?>(null) }
    var mediaAd by remember { mutableStateOf<TeadsAdPlacementMedia?>(null) }

    LaunchedEffect(Unit) {
        // 0. Enable more logging visibility for testing purposes
        TeadsSDK.testMode = BuildConfig.DEBUG
        
        // 1. Init configuration
        val config = TeadsAdPlacementMediaConfig(
            pid = DemoSessionConfiguration.getPlacementIdOrDefault().toInt(), // Your unique placement id
            articleUrl = DemoSessionConfiguration.getArticleUrlOrDefault().toUri() // Your article url
        )

        // 2. Create placement
        mediaAd = TeadsAdPlacementMedia(
            context = context,
            config = config,
            delegate = object : TeadsAdPlacementEventsDelegate {
                override fun onPlacementEvent(
                    placement: TeadsAdPlacement<*, *>,
                    event: TeadsAdPlacementEventName,
                    data: Map<String, Any>?
                ) {
                    // 5. Stay tuned to lifecycle events
                    Log.d("MediaColumnScreen", "$placement - $event: $data")
                }
            }
        )

        // 3. Request ad
        val loadedAdView = mediaAd?.loadAd()
        adView = loadedAdView
    }

    // Cleanup when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            // 4. Clean from memory
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
        ArticleBody(text = stringResource(R.string.article_template_body_a), isBold = true)
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_b))
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_c))
        ArticleSpacing()
        
        // 5. Add in the ad container
        AdContainer(adView = adView)
        
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_d))
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_e))
    }
}
