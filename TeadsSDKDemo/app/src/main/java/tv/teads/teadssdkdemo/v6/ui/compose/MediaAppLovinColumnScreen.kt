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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.sdk.AppLovinSdk
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
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleImage
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleSpacing
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle

@Composable
fun MediaAppLovinColumnScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var maxAdView by remember { mutableStateOf<MaxAdView?>(null) }

    LaunchedEffect(Unit) {
        // 1. Initialize Teads Helper and AppLovin SDK (can be init once on the start of the app)
        AppLovinSdk.getInstance(context.applicationContext).mediationProvider = "max"
        AppLovinSdk.getInstance(context).initializeSdk { }
        TeadsHelper.initialize()

        // 2. Create MaxAdView view and add it to view hierarchy
        maxAdView = MaxAdView(DemoSessionConfiguration.getPlacementIdOrDefault(), MaxAdFormat.MREC, context)

        // 3. Listen to lifecycle events
        maxAdView?.setListener(object : MaxAdViewAdListener {
            override fun onAdLoaded(ad: MaxAd?) {
                Log.d("MediaAppLovinColumnScreen", "onAdLoaded")
            }

            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                Log.e("MediaAppLovinColumnScreen", "onAdLoadFailed: ${error?.message}")
            }

            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                Log.e("MediaAppLovinColumnScreen", "onAdDisplayFailed: ${error?.message}")
            }

            override fun onAdExpanded(ad: MaxAd?) {
                Log.d("MediaAppLovinColumnScreen", "onAdExpanded")
            }

            override fun onAdCollapsed(ad: MaxAd?) {
                Log.d("MediaAppLovinColumnScreen", "onAdCollapsed")
            }

            override fun onAdDisplayed(ad: MaxAd?) {
                Log.d("MediaAppLovinColumnScreen", "onAdDisplayed")
            }

            override fun onAdHidden(ad: MaxAd?) {
                Log.d("MediaAppLovinColumnScreen", "onAdHidden")
            }

            override fun onAdClicked(ad: MaxAd?) {
                Log.d("MediaAppLovinColumnScreen", "onAdClicked")
            }
        })

        // 4. Create a TeadsAdapterListener
        // You need to create an instance for each instance of MaxAdView view
        // It needs to be a strong reference to it, so our helper can clean up when you don't need it anymore
        val teadsListener = object : TeadsAdapterListener {
            override fun onRatioUpdated(adRatio: AdRatio) {
                maxAdView?.resizeAdContainer(adRatio)
            }

            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                maxAdView?.addView(trackerView)
            }
        }

        // 5. Attach the listener to TeadsHelper and save the key to add on TeadsMediationSettings
        val key = TeadsHelper.attachListener(teadsListener)

        // 6. Create the mediation settings to customize our Teads AdView
        val settingsEncoded = TeadsMediationSettings.Builder()
            .enableDebug() // Enable more logging visibility
            .pageSlotUrl(DemoSessionConfiguration.getArticleUrlOrDefault()) // Your article url
            .setMediationListenerKey(key)
            .build()
            .toJsonEncoded()

        // 7. Add the settings encoded to the adView using this key
        maxAdView?.setLocalExtraParameter("teadsSettings", settingsEncoded)

        // 8. Disable ad auto refresh, if you don't disable it, use it carefully since
        // if you receive ads that are not displayed too often it will reduce your fill rate
        maxAdView?.setExtraParameter("allow_pause_auto_refresh_immediately", "true")
        maxAdView?.stopAutoRefresh()

        // 9. Load the ad
        maxAdView?.loadAd()
    }

    // Cleanup when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            // 10. Clean up resources
            maxAdView?.destroy()
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
        
        // 11. Add in the ad container
        AdContainer(adView = maxAdView)
        
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_d))
    }
}
