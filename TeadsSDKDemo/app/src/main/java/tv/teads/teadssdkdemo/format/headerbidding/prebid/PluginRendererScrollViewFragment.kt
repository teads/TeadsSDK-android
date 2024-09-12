package tv.teads.teadssdkdemo.format.headerbidding.prebid

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import tv.teads.teadssdkdemo.data.CreativeSize
import tv.teads.teadssdkdemo.databinding.FragmentInreadScrollviewBinding
import tv.teads.teadssdkdemo.format.mediation.identifier.PrebidIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * PluginRenderer + inRead format within a ScrollView
 */
class PluginRendererScrollViewFragment : BaseFragment() {
    private lateinit var binding: FragmentInreadScrollviewBinding

    private lateinit var teadsPluginRenderer: TeadsPBMPluginRenderer
    private var bannerView: BannerView? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentInreadScrollviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.integration_header).text = getTitle()

        // 1. Setup the settings
        val placementSettings = AdPlacementSettings.Builder()
            .enableDebug()
            .build()
        val teadsMediationSettings = TeadsMediationSettings(adPlacementSettings = placementSettings)

        // 2. Init Teads plugin renderer instance
        teadsPluginRenderer = TeadsPBMPluginRenderer(requireContext(), teadsMediationSettings)

        // 3. Register on Prebid SDK Teads plugin renderer
        PrebidMobile.registerPluginRenderer(teadsPluginRenderer)

        // 4. Manage your Prebid banner view
        bannerView = BannerView(
            requireContext(),
            PrebidIdentifier.getAdUnitFromPid(CreativeSize.LANDSCAPE),
            AdSize(300, 250)
        )

        // 5. Add for brand safety purposes the article URL or you domain URL when article is not applied
        TargetingParams.addExtData("contextUrl", "http://teads.com")

        // 6 . Listen TeadsPBMEventListener events and manage onAdRatioUpdate to have your view correctly displayed
        bannerView?.setPluginEventListener(object : TeadsPBMEventListener{
            override fun onAdRatioUpdate(adRatio: AdRatio) { // todo update logic
                Log.d("TeadsPBMEventListener", "onAdRatioUpdate")

                bannerView?.let {
                    val adViewParams = binding.adSlotContainer.layoutParams
                    adViewParams.height = adRatio.calculateHeight(it.measuredWidth)
                    binding.adSlotContainer.layoutParams = adViewParams
                }
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

        // 7. Listen to your ad lifecycle
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
        bannerView?.let { binding.adSlotContainer.addView(it) }

        // 9. Load the ad
        bannerView?.loadAd()
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerView = null
    }

    override fun getTitle(): String = "Teads Plugin Renderer"
}
