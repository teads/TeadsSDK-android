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
import com.equativ.displaysdk.ad.interstitial.SASInterstitialManager
import com.equativ.displaysdk.exception.SASException
import com.equativ.displaysdk.model.SASAdInfo
import com.equativ.displaysdk.util.SASConfiguration
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleLabel
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleSpacing
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle

private const val TAG = "InterstitialEquativ"

@Composable
fun InterstitialEquativColumnScreen(
    modifier: Modifier = Modifier,
    activity: Activity
) {
    val context = LocalContext.current
    var isContentUnlocked by remember { mutableStateOf(false) }
    var interstitialManager by remember { mutableStateOf<SASInterstitialManager?>(null) }
    var isAdLoaded by remember { mutableStateOf(false) }
    var isWaitingForAd by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!SASConfiguration.isConfigured) {
            SASConfiguration.configure(context)
        }
        SASConfiguration.isLoggingEnabled = true

        val placement = DemoSessionConfiguration.getEquativInterstitialPreset().placement
        val activityContext: Context = activity
        val manager = SASInterstitialManager(activityContext, placement)
        manager.interstitialManagerListener = object :
            SASInterstitialManager.InterstitialManagerListener {
            override fun onInterstitialAdLoaded(adInfo: SASAdInfo) {
                Log.d(TAG, "Ad loaded: $adInfo")
                isAdLoaded = true
                if (isWaitingForAd) {
                    manager.show()
                }
            }

            override fun onInterstitialAdFailedToLoad(exception: SASException) {
                Log.e(TAG, "Ad failed to load: ${exception.type}", exception)
                isAdLoaded = false
                isWaitingForAd = false
            }

            override fun onInterstitialAdShown() {
                Log.d(TAG, "Ad shown")
                isAdLoaded = false
            }

            override fun onInterstitialAdFailedToShow(exception: SASException) {
                Log.e(TAG, "Ad failed to show: ${exception.type}", exception)
                isAdLoaded = false
                interstitialManager?.onDestroy()
                interstitialManager = null
                isContentUnlocked = true
                isWaitingForAd = false
            }

            override fun onInterstitialAdClosed() {
                Log.d(TAG, "Ad closed")
                isAdLoaded = false
                interstitialManager?.onDestroy()
                interstitialManager = null
                isContentUnlocked = true
                isWaitingForAd = false
            }

            override fun onInterstitialAdClicked() {
                Log.d(TAG, "Ad clicked")
            }

            override fun onInterstitialAdAudioStart() {
                Log.d(TAG, "Ad audio started")
            }

            override fun onInterstitialAdAudioStop() {
                Log.d(TAG, "Ad audio stopped")
            }
        }
        interstitialManager = manager
        manager.loadAd()
    }

    DisposableEffect(Unit) {
        onDispose {
            isAdLoaded = false
            interstitialManager?.onDestroy()
            interstitialManager = null
        }
    }

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

                PaywallOverlay(
                    isWaitingForAd = isWaitingForAd,
                    minHeight = paywallMinHeight,
                    onWatchAdClick = {
                        val manager = interstitialManager
                        if (manager != null && isAdLoaded) {
                            manager.show()
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
private fun PaywallOverlay(
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
