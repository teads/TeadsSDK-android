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
import org.prebid.mobile.api.rendering.pluginrenderer.PrebidMobilePluginRegister
import org.prebid.mobile.configuration.AdUnitConfiguration
import tv.teads.adapter.prebid.TeadsPBMEventListener
import tv.teads.adapter.prebid.TeadsPBMPluginRenderer
import tv.teads.adapter.prebid.TeadsPrebidHelper
import tv.teads.sdk.AdPlacementSettings
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.databinding.FragmentInreadScrollviewBinding
import tv.teads.teadssdkdemo.format.mediation.identifier.PrebidIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment
import kotlin.math.roundToInt

/**
 * PluginRenderer + inRead format within a ScrollView
 */
class InReadPluginRendererScrollViewFragment : BaseFragment(), TeadsPBMEventListener {
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

        val placementSettings = AdPlacementSettings.Builder()
            .enableDebug()
            .build()
        val teadsMediationSettings = TeadsMediationSettings(adPlacementSettings = placementSettings)

        teadsPluginRenderer = TeadsPBMPluginRenderer(requireContext(), teadsMediationSettings)
        PrebidMobilePluginRegister.getInstance().registerPlugin(teadsPluginRenderer)

        bannerView = BannerView(
            requireContext(),
            PrebidIdentifier.getAdUnitFromPid(pid),
            AdSize(300, 250)
        )
        TargetingParams.addExtData("pageUrl", "http://sdk.teads.tv")
        bannerView?.setPluginEventListener(this)
        bannerView?.setBannerListener(object : BannerViewListener {
            override fun onAdLoaded(bannerView: BannerView?) {
                TeadsPrebidHelper.resizePrebidBannerView(bannerView, null)
            }

            override fun onAdDisplayed(bannerView: BannerView?) {
            }

            override fun onAdFailed(bannerView: BannerView?, exception: AdException?) {
                Log.d("PlaybackEvent", "onAdFailed")
            }

            override fun onAdClicked(bannerView: BannerView?) {
                Log.d("PlaybackEvent", "onAdClicked")
            }

            override fun onAdClosed(bannerView: BannerView?) {
                Log.d("PlaybackEvent", "onAdClosed")
            }

        })
        bannerView?.loadAd()
        bannerView?.let {
            bannerView = it
            binding.adSlotView.addView(bannerView)
        }
    }

    override fun onAdRatioUpdate(adRatio: AdRatio) {
    }

    override fun onAdCollapsedFromFullscreen() { Log.d("PrebidScrollView", "onAdCollapsedFromFullscreen") }

    override fun onAdExpandedToFullscreen() { Log.d("PrebidScrollView", "onAdExpandedToFullscreen") }

    override fun onFailToReceiveAd(failReason: String) {
        Log.d("PrebidScrollView", "onFailToReceiveAd ${failReason}")
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerView = null
    }

    override fun getTitle(): String = "Teads Plugin Renderer"
}
