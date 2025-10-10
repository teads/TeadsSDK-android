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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import tv.teads.adapter.admob.nativead.TeadsNativeAdapter
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
fun MediaNativeAdmobColumnScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var nativeAdView by remember { mutableStateOf<NativeAdView?>(null) }

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

        // 2. Create your Admob Native AdView
        val admobNativeAdView = LayoutInflater.from(context)
            .inflate(R.layout.admob_native_ad_view, null, false) as NativeAdView

        // 3. Create AdLoader for native ads
        val adLoader = AdLoader.Builder(context, DemoSessionConfiguration.getPlacementIdOrDefault())
            .forNativeAd { ad: NativeAd ->
                // Configure the native ad view
                with(admobNativeAdView) {
                    mediaView = findViewById(R.id.ad_mob_media)
                    headlineView = findViewById(R.id.ad_mob_headline)
                    bodyView = findViewById(R.id.ad_mob_body)
                    callToActionView = findViewById(R.id.sponsor_more)

                    // Set the native ad content
                    headlineView?.let { it as android.widget.TextView }?.text = ad.headline
                    bodyView?.let { it as android.widget.TextView }?.text = ad.body

                    setNativeAd(ad)
                }
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("MediaNativeAdmobColumnScreen", "onAdFailedToLoad: ${error.message}")
                }

                override fun onAdLoaded() {
                    Log.d("MediaNativeAdmobColumnScreen", "onAdLoaded")
                }

                override fun onAdImpression() {
                    Log.d("MediaNativeAdmobColumnScreen", "onAdImpression")
                }

                override fun onAdClicked() {
                    Log.d("MediaNativeAdmobColumnScreen", "onAdClicked")
                }

                override fun onAdOpened() {
                    Log.d("MediaNativeAdmobColumnScreen", "onAdOpened")
                }

                override fun onAdClosed() {
                    Log.d("MediaNativeAdmobColumnScreen", "onAdClosed")
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        // 4. Create TeadsAdapterListener
        val teadsListener = object : TeadsAdapterListener {
            override fun onRatioUpdated(adRatio: AdRatio) {
                // not required
            }

            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                admobNativeAdView.addView(trackerView)
            }
        }

        // 5. Attach the listener to TeadsHelper and save the key
        val key = TeadsHelper.attachListener(teadsListener)

        // 6. Create the mediation settings
        val extras = TeadsMediationSettings.Builder()
            .enableDebug() // Enable more logging visibility
            .pageSlotUrl(DemoSessionConfiguration.getArticleUrlOrDefault()) // Your article url
            .setMediationListenerKey(key)
            .build()

        // 7. Create the AdRequest with your settings and TeadsNativeAdapter as an extra bundle
        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(TeadsNativeAdapter::class.java, extras.toBundle())
            .build()

        // 8. Load the native ad
        adLoader.loadAd(adRequest)
        
        nativeAdView = admobNativeAdView
    }

    // Cleanup when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            // 9. Clean up resources
            nativeAdView?.destroy()
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
        
        // 9. Add in the native ad container
        AdContainer(adView = nativeAdView)
        
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_d))
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_e))
    }
}
