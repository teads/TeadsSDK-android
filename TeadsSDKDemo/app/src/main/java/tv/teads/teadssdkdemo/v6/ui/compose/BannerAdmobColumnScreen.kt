package tv.teads.teadssdkdemo.v6.ui.compose

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleLabel
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleSpacing
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle

private const val TAG = "BannerAdmobColumn"

@Composable
fun BannerAdmobColumnScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val context = LocalContext.current
    var adView by remember { mutableStateOf<AdView?>(null) }

    LaunchedEffect(Unit) {
        // 1. Initialize AdMob (the Banner adapter on the AdMob server returns a
        //    Teads anchored placement; events are routed internally by the adapter,
        //    so the publisher integration is just a bare AdView)
        MobileAds.initialize(context)

        // 2. Create AdMob view and configure it as an anchored adaptive banner
        val admobAdView = AdView(context)
        admobAdView.adUnitId = DemoSessionConfiguration.getPlacementIdOrDefault() // Your unique ad unit id

        // Use anchored adaptive banner sizing — the Teads JS engine reports its
        // own dynamic height via HEIGHT_UPDATED, so the host container view should
        // be wrap_content to accommodate the reported size.
        val widthDp = context.resources.configuration.screenWidthDp
        admobAdView.setAdSize(
            AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, widthDp)
        )

        // 3. Listen to lifecycle events
        admobAdView.adListener = object : AdListener() {
            override fun onAdImpression() {
                Log.d(TAG, "onAdImpression")
            }
            override fun onAdClicked() {
                Log.d(TAG, "onAdClicked")
            }
            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.e(TAG, "onAdFailedToLoad: ${error.cause?.message}")
            }
            override fun onAdLoaded() {
                Log.d(TAG, "onAdLoaded")
            }
            override fun onAdOpened() {
                Log.d(TAG, "onAdOpened")
            }
            override fun onAdClosed() {
                Log.d(TAG, "onAdClosed")
            }
        }

        // 4. Load the ad
        admobAdView.loadAd(AdRequest.Builder().build())

        adView = admobAdView
    }

    // Cleanup when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            // 5. Clean up resources
            adView?.destroy()
        }
    }

    // 6. Render the article in a Scaffold whose bottomBar pins the banner to the
    //    bottom of the screen (mirrors the combinedsdk-demo Column use case).
    //    No topBar — the parent activity already supplies one.
    Scaffold(
        modifier = modifier,
        bottomBar = {
            adView?.let { view ->
                AndroidView(
                    factory = { view },
                    modifier = Modifier.fillMaxWidth()
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
