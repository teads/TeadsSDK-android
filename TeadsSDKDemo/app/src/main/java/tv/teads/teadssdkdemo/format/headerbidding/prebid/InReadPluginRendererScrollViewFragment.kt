package tv.teads.teadssdkdemo.format.headerbidding.prebid

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.prebid.mobile.api.exceptions.AdException
import org.prebid.mobile.api.rendering.pluginrenderer.PrebidMobilePluginRegister
import org.prebid.mobile.configuration.AdUnitConfiguration
import org.prebid.mobile.rendering.bidding.data.bid.BidResponse
import org.prebid.mobile.rendering.bidding.listeners.DisplayVideoListener
import org.prebid.mobile.rendering.bidding.listeners.DisplayViewListener
import tv.teads.adapter.prebid.TeadsPBMEventListener
import tv.teads.adapter.prebid.TeadsPBMPluginRenderer
import tv.teads.sdk.AdPlacementSettings
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.renderer.InReadAdView
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.databinding.FragmentInreadScrollviewBinding
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * PluginRenderer + inRead format within a ScrollView
 */
class InReadPluginRendererScrollViewFragment : BaseFragment(), TeadsPBMEventListener {
    private lateinit var binding: FragmentInreadScrollviewBinding
    private var inReadAdView: InReadAdView? = null
    private val adUnitConfiguration = AdUnitConfiguration()
    private val bidResponse = BidResponse(FAKE_BID_RESPONSE, adUnitConfiguration)

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

        val teadsPBMPluginRenderer = TeadsPBMPluginRenderer(requireContext(), teadsMediationSettings)

        val adView = teadsPBMPluginRenderer.createBannerAdView(
            context = requireContext(),
            displayViewListener = object : DisplayViewListener {
                override fun onAdLoaded() { Log.d("PrebidScrollView", "onAdLoaded") }

                override fun onAdDisplayed() { Log.d("PrebidScrollView", "onAdDisplayed") }

                override fun onAdFailed(exception: AdException?) { Log.d("PrebidScrollView", "AdException ${exception}") }

                override fun onAdClicked() { Log.d("PrebidScrollView", "onAdClicked") }

                override fun onAdClosed() { Log.d("PrebidScrollView", "onAdClosed") }
            },
            displayVideoListener = object : DisplayVideoListener {
                override fun onVideoCompleted() { Log.d("PrebidScrollView", "onVideoCompleted") }

                override fun onVideoPaused() { Log.d("PrebidScrollView", "onVideoPaused") }

                override fun onVideoResumed() { Log.d("PrebidScrollView", "onVideoResumed") }

                override fun onVideoUnMuted() { Log.d("PrebidScrollView", "onVideoUnMuted") }

                override fun onVideoMuted() { Log.d("PrebidScrollView", "onVideoMuted") }
            },
            adUnitConfiguration = adUnitConfiguration,
            bidResponse = bidResponse
        )

        (adView as? InReadAdView)?.let {
            inReadAdView = it
            binding.adSlotView.addView(inReadAdView)
        }

        // todo dummy prebid sdk usage
        PrebidMobilePluginRegister.getInstance().registerPlugin(teadsPBMPluginRenderer)
        PrebidMobilePluginRegister.getInstance().registerEventListener(this, adUnitConfiguration.fingerprint)
    }

    override fun onAdRatioUpdate(adRatio: AdRatio) {
        inReadAdView?.let { inReadAdView ->
            val layoutParams = inReadAdView.layoutParams
            layoutParams.height = adRatio.calculateHeight(binding.adSlotView.measuredWidth)
            binding.adSlotView.layoutParams = layoutParams
        }
    }

    override fun onAdCollapsedFromFullscreen() { Log.d("PrebidScrollView", "onAdCollapsedFromFullscreen") }

    override fun onAdExpandedToFullscreen() { Log.d("PrebidScrollView", "onAdExpandedToFullscreen") }

    override fun onFailToReceiveAd(failReason: String) {
        Log.d("PrebidScrollView", "onFailToReceiveAd ${failReason}")
    }

    override fun onDestroy() {
        super.onDestroy()
        inReadAdView?.clean()
    }

    override fun getTitle(): String = "Teads Plugin Renderer"

    companion object {
        const val FAKE_ADM_CONTENT = "{\\\"ads\\\":[{\\\"settings\\\":{\\\"values\\\":{\\\"animations\\\":{\\\"expand\\\":0,\\\"collapse\\\":0.5},\\\"placementId\\\":84242,\\\"adType\\\":\\\"video\\\",\\\"placementFormat\\\":\\\"inread\\\",\\\"allowedPlayer\\\":\\\"any\\\",\\\"threshold\\\":50,\\\"pageId\\\":77781},\\\"components\\\":{\\\"closeButton\\\":{\\\"display\\\":false,\\\"countdown\\\":0},\\\"label\\\":{\\\"display\\\":true,\\\"text\\\":\\\"\\\"},\\\"credits\\\":{\\\"display\\\":false},\\\"soundButton\\\":{\\\"display\\\":true,\\\"countdown\\\":0,\\\"type\\\":\\\"equalizer\\\"},\\\"slider\\\":{\\\"closeButtonDisplay\\\":false}},\\\"behaviors\\\":{\\\"smartPosition\\\":{\\\"top\\\":false,\\\"corner\\\":false,\\\"mustBypassWhitelist\\\":true},\\\"slider\\\":{\\\"enable\\\":false},\\\"playerClick\\\":\\\"fullscreen\\\",\\\"soundStart\\\":{\\\"type\\\":\\\"mute\\\"},\\\"soundMute\\\":\\\"threshold\\\",\\\"soundOver\\\":\\\"over\\\",\\\"launch\\\":\\\"auto\\\",\\\"videoStart\\\":\\\"threshold\\\",\\\"videoPause\\\":\\\"threshold\\\",\\\"secure\\\":false,\\\"friendly\\\":false}},\\\"type\\\":\\\"VastXml\\\",\\\"content\\\":\\\"<VAST version=\\\\\\\"3.0\\\\\\\"><Ad id=\\\\\\\"590162\\\\\\\"><Wrapper><AdSystem>TeadsTechnology</AdSystem><VASTAdTagURI><![CDATA[https://s8t.teads.tv/vast/5766402401632256]]></VASTAdTagURI><Error><![CDATA[https://t.teads.tv/track?action=error-vast&code=[ERRORCODE]&pid=84242&vid=b1aefcc416eb3116e70d52e5ac0618abcf6645f4&pfid=1&mediaFileType=[MEDIAFILETYPE]&auctid=a9995fbe-1cb5-4e1a-858f-266903d7d772&sid=460794&scid=18603&ad_source_id=200&dsp_campaign_id=590162&dsp_creative_id=625187&env=sdk-inapp&p=0r6xhthLFKRsJGsJVuuWLOYhyLJnsPDlD8m-NbKCujYAWg&cts=1694097262390&1694097262390]]></Error><Creatives></Creatives></Wrapper></Ad></VAST>\\\",\\\"connection_id\\\":460794,\\\"scenario_id\\\":18603,\\\"dsp_campaign_id\\\":\\\"590162\\\",\\\"ad_source_id\\\":200,\\\"dsp_creative_id\\\":\\\"625187\\\",\\\"insertion_id\\\":590162,\\\"placement_id\\\":84242,\\\"portfolio_item_id\\\":1,\\\"early_click_protection_duration\\\":0,\\\"exclusiveAdOnScreen\\\":false}],\\\"wigoEnabled\\\":false,\\\"placementMetadata\\\":{\\\"84242\\\":{\\\"auctionId\\\":\\\"a9995fbe-1cb5-4e1a-858f-266903d7d772\\\"}},\\\"viewerId\\\":\\\"b1aefcc416eb3116e70d52e5ac0618abcf6645f4\\\"}"
        const val FAKE_BID_RESPONSE = "{\"id\":\"fecb01f9-bb9d-46c0-8ad9-58a6ae042a9c\",\"seatbid\":[{\"bid\":[{\"id\":\"prebid-ita-banner-320-50-meta-custom-renderer\",\"impid\":\"fecb01f9-bb9d-46c0-8ad9-58a6ae042a9c\",\"price\":0.1,\"adm\":\"$FAKE_ADM_CONTENT\",\"crid\":\"540944516\",\"w\":320,\"h\":50,\"ext\":{\"prebid\":{\"type\":\"banner\",\"targeting\":{\"hb_pb\":\"0.10\",\"hb_env\":\"mobile-app\",\"hb_size_prebid\":\"320x50\",\"hb_pb_prebid\":\"0.10\",\"hb_bidder_prebid\":\"prebid\",\"hb_size\":\"320x50\",\"hb_bidder\":\"prebid\",\"hb_env_prebid\":\"mobile-app\"},\"meta\":{\"rendererName\":\"teads\",\"rendererVersion\":\"1.0.0\"}},\"origbidcpm\":0.1,\"origbidcur\":\"USD\"}}],\"seat\":\"prebid\",\"group\":0}],\"cur\":\"USD\",\"ext\":{\"warnings\":{\"prebid\":[{\"code\":999,\"message\":\"WARNING: request.imp[0].ext must contain at least one valid bidder\"}]},\"responsetimemillis\":{\"prebid\":0},\"tmaxrequest\":5000,\"prebid\":{\"auctiontimestamp\":1695641919377}}}"
    }
}
