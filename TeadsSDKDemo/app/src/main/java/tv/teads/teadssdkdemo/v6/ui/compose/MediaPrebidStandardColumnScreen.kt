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
import org.prebid.mobile.AdSize
import org.prebid.mobile.Host
import org.prebid.mobile.PrebidMobile
import org.prebid.mobile.TargetingParams
import org.prebid.mobile.api.data.InitializationStatus
import org.prebid.mobile.api.exceptions.AdException
import org.prebid.mobile.api.rendering.BannerView
import org.prebid.mobile.api.rendering.listeners.BannerViewListener
import tv.teads.adapter.prebid.TeadsPBMEventListener
import tv.teads.adapter.prebid.TeadsPBMPluginRenderer
import tv.teads.sdk.AdPlacementSettings
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.inread.extensions.resizeAdContainer
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.ui.base.components.AdContainer
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleBody
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleImage
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleSpacing
import tv.teads.teadssdkdemo.v6.ui.base.components.ArticleTitle

@Composable
fun MediaPrebidStandardColumnScreen(
    modifier: Modifier = Modifier
) {
    val fakeTeadsPrebidServer =
        "https://tm3zwelt7nhxurh4rgapwm5smm0gywau.lambda-url.eu-west-1.on.aws/openrtb2/auction?verbose=true"

    val fakeConfigId = "imp-video-300x250"

    val context = LocalContext.current
    var bannerView by remember { mutableStateOf<BannerView?>(null) }
    var teadsPluginRenderer by remember { mutableStateOf<TeadsPBMPluginRenderer?>(null) }
    var isPrebidSDKInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // 0. Init Prebid SDK - can be init once on the start of the app
        PrebidMobile.initializeSdk(context) { status ->
            isPrebidSDKInitialized = status == InitializationStatus.SUCCEEDED
        }
    }

    LaunchedEffect(isPrebidSDKInitialized) {
        if (!isPrebidSDKInitialized) return@LaunchedEffect

        PrebidMobile.setPrebidServerHost(Host.createCustomHost(fakeTeadsPrebidServer)) // Your unique prebid server host

        // 1. Setup the settings
        val placementSettings = AdPlacementSettings.Builder()
            .enableDebug()
            .build()
        val teadsMediationSettings = TeadsMediationSettings(adPlacementSettings = placementSettings)

        // 2. Init Teads Plugin Renderer
        teadsPluginRenderer = TeadsPBMPluginRenderer(context, teadsMediationSettings)

        // 3. Register Teads Plugin Renderer on Prebid SDK
        PrebidMobile.registerPluginRenderer(teadsPluginRenderer)

        // 4. Init your Prebid BannerView
        bannerView = BannerView(
            context,
            fakeConfigId, // Your unique config id
            AdSize(300, 0)
        )

        // 5. Add your article url
        TargetingParams.addExtData("contextUrl", DemoSessionConfiguration.getArticleUrlOrDefault())

        // 6. Stay tuned to Plugin lifecycle events
        bannerView?.setPluginEventListener(object : TeadsPBMEventListener {
            override fun onAdRatioUpdate(adRatio: AdRatio) {
                Log.d("TeadsPBMEventListener", "onAdRatioUpdate")
                bannerView?.resizeAdContainer(adRatio) // Comply with onAdRatioUpdate to have your ad view correctly rendered
            }

            override fun onAdCollapsedFromFullscreen() {
                Log.d("TeadsPBMEventListener", "onAdCollapsedFromFullscreen")
            }

            override fun onAdExpandedToFullscreen() {
                Log.d("TeadsPBMEventListener", "onAdExpandedToFullscreen")
            }

            override fun onFailToReceiveAd(failReason: String) {
                Log.d("TeadsPBMEventListener", "onFailToReceiveAd $failReason")
            }
        })

        // 7. Stay tuned to BannerView lifecycle events
        bannerView?.setBannerListener(object : BannerViewListener {
            override fun onAdLoaded(bannerView: BannerView?) {
                Log.d("BannerViewListener", "onAdLoaded")
            }

            override fun onAdDisplayed(bannerView: BannerView?) {
                Log.d("BannerViewListener", "onAdDisplayed")
            }

            override fun onAdFailed(bannerView: BannerView?, exception: AdException?) {
                Log.d("BannerViewListener", "onAdFailed")
            }

            override fun onAdClicked(bannerView: BannerView?) {
                Log.d("BannerViewListener", "onAdClicked")
            }

            override fun onAdClosed(bannerView: BannerView?) {
                Log.d("BannerViewListener", "onAdClosed")
            }
        })

        // 8. Load the ad
        bannerView?.loadAd()
    }

    // Cleanup when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            // 9. Clean from memory
            bannerView?.destroy()
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

        // 10. Add the ad view to its container
        AdContainer(adView = bannerView)

        ArticleSpacing()
        ArticleBody(text = stringResource(R.string.article_template_body_d))
    }
}
