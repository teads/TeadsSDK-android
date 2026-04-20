package tv.teads.teadssdkdemo.v6.ui.compose

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementInterstitial
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementInterstitialConfig
import tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate
import tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement
import tv.teads.teadssdkdemo.BuildConfig
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleLabel
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleSpacing
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle

private const val TAG = "InterstitialDirect"

@Composable
fun InterstitialDirectColumnScreen(
    modifier: Modifier = Modifier,
    activity: Activity
) {
    val context = LocalContext.current
    var isContentUnlocked by remember { mutableStateOf(false) }
    var isWaitingForAd by remember { mutableStateOf(false) }
    var isAdReady by remember { mutableStateOf(false) }
    var placement by remember { mutableStateOf<TeadsAdPlacementInterstitial?>(null) }

    LaunchedEffect(Unit) {
        // 0. Init SDK
        TeadsSDK.configure(
            applicationContext = context.applicationContext,
            appKey = "AndroidSampleApp2014"
        )
        if (BuildConfig.DEBUG) {
            TeadsSDK.testMode = true
        }

        // 1. Init configuration
        val config = TeadsAdPlacementInterstitialConfig(
            articleUrl = DemoSessionConfiguration.getArticleUrlOrDefault(),
            widgetId = DemoSessionConfiguration.getWidgetIdOrDefault(),
            installationKey = DemoSessionConfiguration.getInstallationKeyOrDefault()
        )

        // 2. Create placement — context must be an Activity (SDK attaches a hidden WebView to content view)
        val interstitial = TeadsAdPlacementInterstitial(
            context = activity,
            config = config,
            delegate = { placement, event, data ->
                Log.d(TAG, "$event: $data")
                when (event) {
                    TeadsAdPlacementEventName.READY -> {
                        isAdReady = true
                        // 4. If the user already requested to see the ad, show it now
                        if (isWaitingForAd) {
                            (placement as? TeadsAdPlacementInterstitial)?.show(activity)
                        }
                    }

                    TeadsAdPlacementEventName.FAILED -> {
                        isAdReady = false
                        isContentUnlocked = true
                        isWaitingForAd = false
                    }

                    TeadsAdPlacementEventName.DISMISSED -> {
                        isAdReady = false
                        isContentUnlocked = true
                        isWaitingForAd = false
                    }

                    else -> Unit
                }
            }
        )
        placement = interstitial

        // 3. Request ad
        interstitial.loadAd()
    }

    DisposableEffect(Unit) {
        onDispose { placement?.clean() }
    }

    // Sample use case: article with a paywall that triggers the interstitial ad
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val screenHeight = maxHeight
        val density = LocalDensity.current
        var articleContentHeight by remember { mutableStateOf(0.dp) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
        ) {
            Column(
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    articleContentHeight = with(density) { coordinates.size.height.toDp() }
                }
            ) {
                ArticleLabel()
                ArticleSpacing()
                ArticleTitle()
                ArticleSpacing()
                ArticleBody(text = stringResource(R.string.article_template_body_a))
                ArticleSpacing()
            }

            if (!isContentUnlocked) {
                val paywallMinHeight = (screenHeight - articleContentHeight).coerceAtLeast(300.dp)

                InterstitialDirectPaywallOverlay(
                    isWaitingForAd = isWaitingForAd,
                    minHeight = paywallMinHeight,
                    onWatchAdClick = {
                        val ad = placement
                        if (isAdReady && ad != null) {
                            // 5. Show the ad
                            ad.show(activity)
                        } else {
                            isWaitingForAd = true
                        }
                    }
                )
            } else {
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
}

@Composable
private fun InterstitialDirectPaywallOverlay(
    isWaitingForAd: Boolean,
    minHeight: androidx.compose.ui.unit.Dp,
    onWatchAdClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            ArticleBody(
                text = stringResource(R.string.article_template_body_b),
                modifier = Modifier.padding(bottom = 40.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp)
                .heightIn(min = (minHeight - 80.dp).coerceAtLeast(0.dp))
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Premium Content",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "See ad to read the rest of the content",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isWaitingForAd) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Button(onClick = onWatchAdClick) {
                    Text("Watch Ad")
                }
            }
        }
    }
}
