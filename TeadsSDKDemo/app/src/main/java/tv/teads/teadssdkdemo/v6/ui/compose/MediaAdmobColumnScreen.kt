package tv.teads.teadssdkdemo.v6.ui.compose

import android.util.Log
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import tv.teads.adapter.admob.TeadsAdapter
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.inread.extensions.resizeAdContainer
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.ui.base.components.AdContainer
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleLabel
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleSpacing
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle

@Composable
fun MediaAdmobColumnScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var adView by remember { mutableStateOf<AdView?>(null) }

    LaunchedEffect(Unit) {
        // 1. Initialize AdMob and Teads Helper
        MobileAds.initialize(context)
        TeadsHelper.initialize()

        // For testing purposes - using a test device configuration
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("BAC58D23C8C5265E2C8A56FE7FBAE2C1"))
                .build()
        )

        // 2. Create AdMob view, set up and add it to view hierarchy
        val admobAdView = AdView(context)
        admobAdView.adUnitId = DemoSessionConfiguration.getPlacementIdOrDefault() // Your unique placement id

        // Setting the ad size as MEDIUM_RECTANGLE is mandatory but somewhat of a workaround
        // Your ad container view needs to have height as wrap_content to satisfy different ad formats that Media provides
        admobAdView.setAdSize(AdSize.MEDIUM_RECTANGLE)

        // 3. Listen to lifecycle events
        admobAdView.adListener = object : AdListener() {
            override fun onAdImpression() {
                Log.d("MediaAdmobColumnScreen", "onAdImpression")
            }
            override fun onAdClicked() {
                Log.d("MediaAdmobColumnScreen", "onAdClicked")
            }
            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.e("MediaAdmobColumnScreen", "onAdFailedToLoad: ${error.cause?.message}")
            }
            override fun onAdLoaded() {
                Log.d("MediaAdmobColumnScreen", "onAdLoaded")
            }
            override fun onAdOpened() {
                Log.d("MediaAdmobColumnScreen", "onAdOpened")
            }
            override fun onAdClosed() {
                Log.d("MediaAdmobColumnScreen", "onAdClosed")
            }
        }

        // 4. Create a TeadsAdapterListener, you need to create an instance for each AdMob view
        // It needs to be a strong reference so our helper can clean up when you don't need it anymore.
        val teadsListener = object : TeadsAdapterListener {
            override fun onRatioUpdated(adRatio: AdRatio) {
                adView?.resizeAdContainer(adRatio)
            }
            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                adView?.addView(trackerView)
            }
        }

        // 5. Attach the listener to TeadsHelper and save the key to add on TeadsMediationSettings
        val key = TeadsHelper.attachListener(teadsListener)

        // 6. Create the mediation settings
        val extras = TeadsMediationSettings.Builder()
            .enableDebug() // Enable more logging visibility
            .pageSlotUrl(DemoSessionConfiguration.getArticleUrlOrDefault()) // Your article url
            .setMediationListenerKey(key)
            .build()

        // 7. Create the AdRequest with your settings and TeadsAdapter as an extra bundle
        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(TeadsAdapter::class.java, extras.toBundle())
            .build()

        // 8. Load the ad
        admobAdView.loadAd(adRequest)
        
        adView = admobAdView
    }

    // Cleanup when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            // 9. Clean up resources
            adView?.destroy()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
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
        
        // 9. Add in the ad container
        AdContainer(adView = adView)
        
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_d))
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_e))
    }
}