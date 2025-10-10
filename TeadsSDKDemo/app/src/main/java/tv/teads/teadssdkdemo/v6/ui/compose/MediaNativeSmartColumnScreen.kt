package tv.teads.teadssdkdemo.v6.ui.compose

import android.util.Log
import android.view.View
import android.widget.FrameLayout
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
import com.smartadserver.android.library.model.SASAdPlacement
import com.smartadserver.android.library.model.SASNativeAdElement
import com.smartadserver.android.library.model.SASNativeAdManager
import com.smartadserver.android.library.util.SASConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tv.teads.adapter.smart.nativead.TeadsSmartViewBinder
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.ui.base.components.AdContainer
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleImage
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleSpacing
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle

@Composable
fun MediaNativeSmartColumnScreen(
    modifier: Modifier = Modifier
) {
    val siteId = 385317 // Your unique site id
    val pageName = "1399205" // Your unique page name
    val formatId = 102803L // Your unique format id
    val teadsExtraKey = "teadsAdSettingsKey"

    val context = LocalContext.current
    val smartAdContainer by remember { mutableStateOf(FrameLayout(context)) }
    var smartNativeAdView by remember { mutableStateOf<View?>(null) }
    var nativeAdManager by remember { mutableStateOf<SASNativeAdManager?>(null) }

    LaunchedEffect(Unit) {
        // 1. Initialize Teads Helper and Smart SDK
        TeadsHelper.initialize()
        SASConfiguration.getSharedInstance().configure(context, siteId)
        SASConfiguration.getSharedInstance().isLoggingEnabled = true

        // 2. Create TeadsAdapterListener
        val teadsListener = object : TeadsAdapterListener {
            override fun onRatioUpdated(adRatio: AdRatio) {
                // Not required
            }

            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                // Add tracker view to the native ad view if available
                smartAdContainer.addView(trackerView)
            }
        }

        // 3. Attach the listener to TeadsHelper and save the key to add on TeadsMediationSettings
        val key = TeadsHelper.attachListener(teadsListener)

        // 4. Create the mediation settings
        val settingsEncoded = TeadsMediationSettings.Builder()
            .enableDebug() // Enable more logging visibility
            .setMediationListenerKey(key)
            .build()
            .toJsonEncoded()

        // 5. Create the ad placement with Teads settings
        val adPlacement = SASAdPlacement(
            siteId.toLong(),
            pageName,
            formatId,
            "$teadsExtraKey=${settingsEncoded}",
            ""
        )

        // 6. Create native ad manager
        val smartNativeAdManager = SASNativeAdManager(context, adPlacement)

        // 7. Set native ad listener and add the ad to container
        smartNativeAdManager.nativeAdListener = object : SASNativeAdManager.NativeAdListener {
            override fun onNativeAdLoaded(ad: SASNativeAdElement) {
                Log.d("MediaNativeSmartColumnScreen", "onNativeAdLoaded")

                // Ensure UI operations happen on main thread (same as working adapter)
                CoroutineScope(Dispatchers.Main).launch {
                    // Create the native ad view using the correct layout
                    smartNativeAdView = TeadsSmartViewBinder(context, R.layout.smart_native_ad_view, ad)
                        .title(R.id.ad_title)
                        .body(R.id.ad_body)
                        .iconImage(R.id.teads_icon)
                        .callToAction(R.id.teads_cta)
                        .mediaLayout(R.id.teads_mediaview)
                        .adChoice(R.id.ad_choice)
                        .bind()

                    smartAdContainer.addView(smartNativeAdView)
                }
            }

            override fun onNativeAdFailedToLoad(e: Exception) {
                Log.e("MediaNativeSmartColumnScreen", "onNativeAdFailedToLoad: ${e.message}")
            }
        }

        // 8. Load the native ad
        smartNativeAdManager.loadNativeAd()

        nativeAdManager = smartNativeAdManager
    }

    // Cleanup when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            // 9. Clean up resources
            nativeAdManager?.onDestroy()
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

        // 10. Add in the native ad container
        AdContainer(adView = smartAdContainer)

        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_d))
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_e))
    }
}
