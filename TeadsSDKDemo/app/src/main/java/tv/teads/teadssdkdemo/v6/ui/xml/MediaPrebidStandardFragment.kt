package tv.teads.teadssdkdemo.v6.ui.xml

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.prebid.mobile.AdSize
import org.prebid.mobile.PrebidMobile
import org.prebid.mobile.TargetingParams
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
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration.PREBID_AD_CONFIG_ID

/**
 * Media Prebid Standard format within a ScrollView
 */
class MediaPrebidStandardFragment : BaseFragment() {
    private lateinit var teadsPluginRenderer: TeadsPBMPluginRenderer
    private var bannerView: BannerView? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.article_scroll_view_template_middle_ad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 0. Ensure to init Prebid SDK beforehand, check ./teadssdkdemo/SplashScreen.kt

        // 1. Setup AdPlacementSettings
        val placementSettings = AdPlacementSettings.Builder()
            .enableDebug()
            .build()
        val teadsMediationSettings = TeadsMediationSettings(adPlacementSettings = placementSettings)

        // 2. Init Teads Plugin Renderer
        teadsPluginRenderer = TeadsPBMPluginRenderer(requireContext(), teadsMediationSettings)

        // 3. Register Teads Plugin Renderer on Prebid SDK
        PrebidMobile.registerPluginRenderer(teadsPluginRenderer)

        // 4. Init your Prebid BannerView
        bannerView = BannerView(
            requireContext(),
            PREBID_AD_CONFIG_ID,
            AdSize(300, 250)
        )

        // 5. Add your article url
        TargetingParams.addExtData("contextUrl", DemoSessionConfiguration.getArticleUrlOrDefault())

        // 6. Stay tuned to Plugin lifecycle events
        bannerView?.setPluginEventListener(object : TeadsPBMEventListener{
            override fun onAdRatioUpdate(adRatio: AdRatio) {
                Log.d("TeadsPBMEventListener", "onAdRatioUpdate")
                // Resize
                val adContainer = view.findViewById<ViewGroup>(R.id.ad_container)
                adContainer.resizeAdContainer(adRatio)
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

        // 8. Add the ad view to its container
        val adContainer = view.findViewById<ViewGroup>(R.id.ad_container)
        bannerView?.let { adContainer.addView(it) }

        // 9. Load the ad
        bannerView?.loadAd()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 10. Clean from memory
        bannerView = null
    }

    override fun getTitle(): String = "Media Prebid Standard"
}
