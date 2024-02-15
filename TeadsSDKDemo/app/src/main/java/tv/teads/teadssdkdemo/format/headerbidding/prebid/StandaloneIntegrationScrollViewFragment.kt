package tv.teads.teadssdkdemo.format.headerbidding.prebid

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdPlacementSettings
import tv.teads.sdk.AdRatio
import tv.teads.sdk.AdRequestSettings
import tv.teads.sdk.InReadAdViewListener
import tv.teads.sdk.PrebidAdPlacement
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.VideoPlaybackListener
import tv.teads.sdk.renderer.InReadAdView
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.databinding.FragmentInreadScrollviewBinding
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * Standalone integration where a loadAd entry point receives a bid response in order to
 * render a InRead format within a ScrollView
 */
class StandaloneIntegrationScrollViewFragment : BaseFragment() {
    private lateinit var binding: FragmentInreadScrollviewBinding
    private lateinit var adPlacement: PrebidAdPlacement
    private var inReadAdView: InReadAdView? = null

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

        // 2. Create the adPlacement
        adPlacement = TeadsSDK.createPrebidPlacement(requireActivity(), placementSettings)

        // 3. Load the ad and listen its events
        val requestSettings = AdRequestSettings.Builder()
            // Ensure to inform your url for brand safety matters
            .pageSlotUrl("http://teads.com")
            .build()

        adPlacement.loadAd(
            FAKE_WINNING_BID_RESPONSE,
            requestSettings,
            object : InReadAdViewListener {
                override fun onAdReceived(ad: InReadAdView, adRatio: AdRatio) {
                    val layoutParams = ad.layoutParams
                    binding.adSlotView.addView(ad)
                    layoutParams.height = adRatio.calculateHeight(binding.adSlotView.measuredWidth)
                    binding.adSlotView.layoutParams = layoutParams

                    inReadAdView = ad
                }

                override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                    binding.adSlotView.addView(trackerView)
                }

                override fun onAdRatioUpdate(adRatio: AdRatio) {
                    inReadAdView?.let { inReadAdView ->
                        val layoutParams = inReadAdView.layoutParams
                        layoutParams.height = adRatio.calculateHeight(binding.adSlotView.measuredWidth)
                        binding.adSlotView.layoutParams = layoutParams
                    }
                }

                override fun onAdClicked() {}
                override fun onAdClosed() {}
                override fun onAdError(code: Int, description: String) {}
                override fun onAdImpression() {}
                override fun onAdExpandedToFullscreen() {}
                override fun onAdCollapsedFromFullscreen() {}
                override fun onFailToReceiveAd(failReason: String) {}
            },
            object : VideoPlaybackListener {
                override fun onVideoComplete() {
                    Log.d("PlaybackEvent", "complete")
                }

                override fun onVideoPause() {
                    Log.d("PlaybackEvent", "pause")
                }

                override fun onVideoPlay() {
                    Log.d("PlaybackEvent", "play")
                }

            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        inReadAdView?.clean()
    }

    override fun getTitle(): String = "Standalone Integration"

    companion object {
        val FAKE_ADM_CONTENT = "{\\\"ads\\\":[{\\\"settings\\\":{\\\"values\\\":{\\\"animations\\\":{\\\"expand\\\":0,\\\"collapse\\\":0.5},\\\"placementId\\\":84242,\\\"adType\\\":\\\"video\\\",\\\"placementFormat\\\":\\\"inread\\\",\\\"allowedPlayer\\\":\\\"any\\\",\\\"threshold\\\":50,\\\"pageId\\\":77781},\\\"components\\\":{\\\"closeButton\\\":{\\\"display\\\":false,\\\"countdown\\\":0},\\\"label\\\":{\\\"display\\\":true,\\\"text\\\":\\\"\\\"},\\\"credits\\\":{\\\"display\\\":false},\\\"soundButton\\\":{\\\"display\\\":true,\\\"countdown\\\":0,\\\"type\\\":\\\"equalizer\\\"},\\\"slider\\\":{\\\"closeButtonDisplay\\\":false}},\\\"behaviors\\\":{\\\"smartPosition\\\":{\\\"top\\\":false,\\\"corner\\\":false,\\\"mustBypassWhitelist\\\":true},\\\"slider\\\":{\\\"enable\\\":false},\\\"playerClick\\\":\\\"fullscreen\\\",\\\"soundStart\\\":{\\\"type\\\":\\\"mute\\\"},\\\"soundMute\\\":\\\"threshold\\\",\\\"soundOver\\\":\\\"over\\\",\\\"launch\\\":\\\"auto\\\",\\\"videoStart\\\":\\\"threshold\\\",\\\"videoPause\\\":\\\"threshold\\\",\\\"secure\\\":false,\\\"friendly\\\":false}},\\\"type\\\":\\\"VastXml\\\",\\\"content\\\":\\\"<VAST version=\\\\\\\"3.0\\\\\\\"><Ad id=\\\\\\\"590162\\\\\\\"><Wrapper><AdSystem>TeadsTechnology</AdSystem><VASTAdTagURI><![CDATA[https://s8t.teads.tv/vast/5766402401632256]]></VASTAdTagURI><Error><![CDATA[https://t.teads.tv/track?action=error-vast&code=[ERRORCODE]&pid=84242&vid=b1aefcc416eb3116e70d52e5ac0618abcf6645f4&pfid=1&mediaFileType=[MEDIAFILETYPE]&auctid=a9995fbe-1cb5-4e1a-858f-266903d7d772&sid=460794&scid=18603&ad_source_id=200&dsp_campaign_id=590162&dsp_creative_id=625187&env=sdk-inapp&p=0r6xhthLFKRsJGsJVuuWLOYhyLJnsPDlD8m-NbKCujYAWg&cts=1694097262390&1694097262390]]></Error><Creatives></Creatives></Wrapper></Ad></VAST>\\\",\\\"connection_id\\\":460794,\\\"scenario_id\\\":18603,\\\"dsp_campaign_id\\\":\\\"590162\\\",\\\"ad_source_id\\\":200,\\\"dsp_creative_id\\\":\\\"625187\\\",\\\"insertion_id\\\":590162,\\\"placement_id\\\":84242,\\\"portfolio_item_id\\\":1,\\\"early_click_protection_duration\\\":0,\\\"exclusiveAdOnScreen\\\":false}],\\\"wigoEnabled\\\":false,\\\"placementMetadata\\\":{\\\"84242\\\":{\\\"auctionId\\\":\\\"a9995fbe-1cb5-4e1a-858f-266903d7d772\\\"}},\\\"viewerId\\\":\\\"b1aefcc416eb3116e70d52e5ac0618abcf6645f4\\\"}"
        val FAKE_WINNING_BID_RESPONSE = "{\"id\":\"prebid-demo-response-video-outstream\",\"impid\":\"03ec3cdd-e144-40bd-98cc-1947235ce897\",\"price\":0.11701999999468729,\"nurl\":\"https://localhost:8080/prebid-server/win-notice?data=base64&clearingPrice=${1000}\",\"adm\":\"$FAKE_ADM_CONTENT\",\"adid\":\"test-ad-id-12345\",\"adomain\":[\"prebid.org\"],\"crid\":\"test-creative-id-1\",\"cid\":\"test-cid-1\",\"ext\":{\"prebid\":{\"type\":\"video\",\"targeting\":{\"hb_pb\":\"0.10\",\"hb_env\":\"mobile-app\",\"hb_size_prebid\":\"300x250\",\"hb_pb_prebid\":\"0.10\",\"hb_bidder_prebid\":\"prebid\",\"hb_size\":\"300x250\",\"hb_bidder\":\"prebid\",\"hb_env_prebid\":\"mobile-app\"},\"meta\":{\"renderername\":\"SampleRendererName\",\"rendererversion\":\"1.0\"}},\"origbidcpm\":0.11701999999468729,\"origbidcur\":\"USD\"}}"
    }
}
