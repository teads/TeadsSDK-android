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
import com.smartadserver.android.library.model.SASAdPlacement
import com.smartadserver.android.library.ui.SASBannerView
import com.smartadserver.android.library.util.SASConfiguration
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.inread.extensions.resizeAdContainer
import tv.teads.teadssdkdemo.v6.ui.base.components.AdContainer
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleImage
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleSpacing
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle

@Composable
fun MediaSmartColumnScreen(
    modifier: Modifier = Modifier
) {
    val siteId = 385317 // Your unique site id
    val pageName = "1399205" // Your unique page name
    val formatId = 96445L // Your unique format id
    val teadsExtraKey = "teadsAdSettingsKey"

    val context = LocalContext.current
    var smartAdView by remember { mutableStateOf<SASBannerView?>(null) }

    LaunchedEffect(Unit) {
        // 1. Initialize Teads Helper and Smart SDK
        TeadsHelper.initialize()
        SASConfiguration.getSharedInstance().configure(context, siteId)
        SASConfiguration.getSharedInstance().isLoggingEnabled = true // Enable more logging visibility

        // 2. Create Smart AdView
        smartAdView = SASBannerView(context)

        // 3. Create TeadsAdapterListener
        val teadsListener = object : TeadsAdapterListener {
            override fun onRatioUpdated(adRatio: AdRatio) {
                smartAdView?.resizeAdContainer(adRatio)
            }

            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                smartAdView?.addView(trackerView)
            }
        }

        // 4. Attach the listener to TeadsHelper and save the key to add on TeadsMediationSettings
        val key = TeadsHelper.attachListener(teadsListener)

        // 5. Create the mediation settings
        val settings = TeadsMediationSettings.Builder()
            .enableDebug() // Enable more logging visibility
            .setMediationListenerKey(key)
            .build()

        // 6. Create the ad placement with Teads settings
        val bannerPlacement = SASAdPlacement(
            siteId.toLong(),
            pageName,
            formatId,
            "$teadsExtraKey=${settings.toJsonEncoded()}"
        )

        // 7. Load the ad
        smartAdView?.loadAd(bannerPlacement)
    }

    // Cleanup when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            // 8. Clean up resources
            smartAdView?.onDestroy()
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
        
        // 9. Add in the ad container
        AdContainer(adView = smartAdView)
        
        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_d))
    }
}
