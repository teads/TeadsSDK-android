package tv.teads.teadssdkdemo.v6.ui.compose

import android.app.Activity
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
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleLabel
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleSpacing
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle

private const val TAG = "InterstitialAdmob"

@Composable
fun InterstitialAdmobColumnScreen(
    modifier: Modifier = Modifier,
    activity: Activity
) {
    val context = LocalContext.current
    var isContentUnlocked by remember { mutableStateOf(false) }
    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    var isWaitingForAd by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // 1. Initialize AdMob
        MobileAds.initialize(context)

        // For testing purposes - using a test device configuration
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("BAC58D23C8C5265E2C8A56FE7FBAE2C1"))
                .build()
        )

        // 2. Create the AdRequest
        val adRequest = AdRequest.Builder().build()

        // 3. Load the interstitial ad
        InterstitialAd.load(
            context,
            DemoSessionConfiguration.getPlacementIdOrDefault(), // Your unique ad unit id
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Ad loaded successfully")

                    // 4. Listen to fullscreen lifecycle events
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdShowedFullScreenContent() {
                            Log.d(TAG, "Ad showed fullscreen")
                        }

                        override fun onAdDismissedFullScreenContent() {
                            Log.d(TAG, "Ad dismissed")
                            interstitialAd = null
                            isContentUnlocked = true
                            isWaitingForAd = false
                        }

                        override fun onAdFailedToShowFullScreenContent(error: AdError) {
                            Log.e(TAG, "Ad failed to show: ${error.message}")
                            interstitialAd = null
                            isContentUnlocked = true
                            isWaitingForAd = false
                        }

                        override fun onAdClicked() {
                            Log.d(TAG, "Ad clicked")
                        }

                        override fun onAdImpression() {
                            Log.d(TAG, "Ad impression")
                        }
                    }

                    interstitialAd = ad

                    // 5. Show the ad — if the user already requested it, present immediately
                    if (isWaitingForAd) {
                        ad.show(activity)
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(TAG, "Ad failed to load: code=${error.code}, message=${error.message}")
                    isWaitingForAd = false
                }
            }
        )
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

                PaywallOverlay(
                    isWaitingForAd = isWaitingForAd,
                    minHeight = paywallMinHeight,
                    onWatchAdClick = {
                        val ad = interstitialAd
                        if (ad != null) {
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

/**
 * Paywall overlay composable that blocks article content behind an ad gate.
 * Shows a faded preview of the next paragraph with a gradient, a prompt message,
 * and a "Watch Ad" button. When the user clicks and the ad is still loading,
 * a progress indicator is displayed until the ad is ready.
 */
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
        // Faded preview of upcoming content to hint there is more to read
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            ArticleBody(
                text = stringResource(R.string.article_template_body_b),
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // Gradient overlay to fade out the preview text
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

        // Paywall card — fills from the gradient down to the bottom of the screen
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
                // User clicked but ad is still loading — show progress until it arrives
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
