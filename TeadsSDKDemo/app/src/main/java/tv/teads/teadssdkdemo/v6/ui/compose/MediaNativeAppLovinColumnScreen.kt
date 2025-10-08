package tv.teads.teadssdkdemo.v6.ui.compose

import android.util.Log
import android.view.LayoutInflater
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
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.applovin.sdk.AppLovinSdk
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.ui.base.components.AdContainer
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleImage
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleSpacing
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle

@Composable
fun MediaNativeAppLovinColumnScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var maxNativeAdView by remember { mutableStateOf<MaxNativeAdView?>(null) }
    var nativeAdLoader by remember { mutableStateOf<MaxNativeAdLoader?>(null) }

    LaunchedEffect(Unit) {
        // 1. Initialize Teads Helper and AppLovin SDK (can be init once on the start of the app)
        AppLovinSdk.getInstance(context.applicationContext).mediationProvider = "max"
        AppLovinSdk.getInstance(context).initializeSdk { }
        TeadsHelper.initialize()

        // 2. Create native ad view binder
        val binder: MaxNativeAdViewBinder = MaxNativeAdViewBinder
            .Builder(R.layout.applovin_native_ad_view)
            .setTitleTextViewId(R.id.ad_title)
            .setBodyTextViewId(R.id.ad_body)
            .setMediaContentViewGroupId(R.id.teads_mediaview)
            .setOptionsContentViewGroupId(R.id.ad_options_view)
            .build()

        // 3. Create your AppLovin Native AdView
        maxNativeAdView = MaxNativeAdView(binder, context)

        // 4. Create MaxNativeAdLoader for native ads
        nativeAdLoader = MaxNativeAdLoader(DemoSessionConfiguration.getPlacementIdOrDefault(), context)

        // 5. Create TeadsAdapterListener
        val teadsListener = object : TeadsAdapterListener {
            override fun onRatioUpdated(adRatio: AdRatio) {
                // Not required
            }

            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                maxNativeAdView?.addView(trackerView)
            }
        }

        // 6. Attach the listener to TeadsHelper and save the key to add on TeadsMediationSettings
        val key = TeadsHelper.attachListener(teadsListener)

        // 7. Create the mediation settings
        val settingsEncoded = TeadsMediationSettings.Builder()
            .enableDebug() // Enable more logging visibility
            .pageSlotUrl(DemoSessionConfiguration.getArticleUrlOrDefault()) // Your article url
            .setMediationListenerKey(key)
            .build()
            .toJsonEncoded()

        // 8. Add the settings encoded to the nativeAdLoader using this key
        nativeAdLoader?.setLocalExtraParameter("teadsSettings", settingsEncoded)

        // 9. Set native ad listener
        nativeAdLoader?.setNativeAdListener(object : MaxNativeAdListener() {
            override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
                Log.d("MediaNativeAppLovinColumnScreen", "onNativeAdLoaded")
            }

            override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
                Log.e("MediaNativeAppLovinColumnScreen", "onNativeAdLoadFailed: ${error.message}")
            }

            override fun onNativeAdClicked(ad: MaxAd) {
                Log.d("MediaNativeAppLovinColumnScreen", "onNativeAdClicked")
            }
        })

        // 10. Load the native ad
        nativeAdLoader?.loadAd(maxNativeAdView)
    }

    // Cleanup when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            // 11. Clean up resources
            nativeAdLoader?.destroy()
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
        
        // 12. Add in the native ad container
        AdContainer(adView = maxNativeAdView)
        
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_d))
    }
}
