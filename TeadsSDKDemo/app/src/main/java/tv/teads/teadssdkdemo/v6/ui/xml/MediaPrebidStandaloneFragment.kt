package tv.teads.teadssdkdemo.v6.ui.xml

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdPlacementExtraKey
import tv.teads.sdk.AdPlacementSettings
import tv.teads.sdk.AdRatio
import tv.teads.sdk.AdRequestSettings
import tv.teads.sdk.InReadAdViewListener
import tv.teads.sdk.PrebidAdPlacement
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.VideoPlaybackListener
import tv.teads.sdk.renderer.InReadAdView
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.inread.extensions.resizeAdContainer
import tv.teads.teadssdkdemo.utils.BaseFragment

class MediaPrebidStandaloneFragment : BaseFragment() {
    private lateinit var adPlacement: PrebidAdPlacement
    private var inReadAdView: InReadAdView? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.article_scroll_view_template_middle_ad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Setup AdPlacementSettings
        val placementSettings = AdPlacementSettings.Builder()
            .enableDebug()
            .build()

        // 2. Create the adPlacement
        adPlacement = TeadsSDK.createPrebidPlacement(requireActivity(), placementSettings)

        // 3. Load the ad and listen its events
        val requestSettings = AdRequestSettings.Builder()
            // Ensure to inform your url for brand safety matters
            .pageSlotUrl("http://teads.com")
            // Add this extra to enable your standalone integration
            .addExtra(AdPlacementExtraKey.STANDALONE_PREBID_INTEGRATION, "1")
            .build()

        adPlacement.loadAd(
            FAKE_WINNING_BID_RESPONSE,
            requestSettings,
            object : InReadAdViewListener {
                override fun onAdReceived(ad: InReadAdView, adRatio: AdRatio) {
                    // Clean and init inReadAdView
                    inReadAdView?.clean()
                    inReadAdView = ad
                    // Add ad to the container and resize
                    val adContainer = view.findViewById<ViewGroup>(R.id.ad_container)
                    adContainer.addView(ad)
                    adContainer.resizeAdContainer(adRatio)
                }

                override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                    val adContainer = view.findViewById<ViewGroup>(R.id.ad_container)
                    adContainer.addView(trackerView)
                }

                override fun onAdRatioUpdate(adRatio: AdRatio) {
                    // Resize
                    val adContainer = view.findViewById<ViewGroup>(R.id.ad_container)
                    adContainer.resizeAdContainer(adRatio)
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

    override fun getTitle(): String = "Media Prebid Standalone"

    companion object {
        val FAKE_ADM_CONTENT_VAST_URL = "{\\\"ads\\\":[{\\\"settings\\\":{\\\"values\\\":{\\\"animations\\\":{\\\"expand\\\":0,\\\"collapse\\\":0.5},\\\"placementId\\\":84242,\\\"adType\\\":\\\"video\\\",\\\"placementFormat\\\":\\\"inread\\\",\\\"allowedPlayer\\\":\\\"any\\\",\\\"threshold\\\":50,\\\"pageId\\\":77781},\\\"components\\\":{\\\"closeButton\\\":{\\\"display\\\":false,\\\"countdown\\\":0},\\\"label\\\":{\\\"display\\\":true,\\\"text\\\":\\\"\\\"},\\\"credits\\\":{\\\"display\\\":false},\\\"soundButton\\\":{\\\"display\\\":true,\\\"countdown\\\":0,\\\"type\\\":\\\"equalizer\\\"},\\\"slider\\\":{\\\"closeButtonDisplay\\\":false}},\\\"behaviors\\\":{\\\"smartPosition\\\":{\\\"top\\\":false,\\\"corner\\\":false,\\\"mustBypassWhitelist\\\":true},\\\"slider\\\":{\\\"enable\\\":false},\\\"playerClick\\\":\\\"fullscreen\\\",\\\"soundStart\\\":{\\\"type\\\":\\\"mute\\\"},\\\"soundMute\\\":\\\"threshold\\\",\\\"soundOver\\\":\\\"over\\\",\\\"launch\\\":\\\"auto\\\",\\\"videoStart\\\":\\\"threshold\\\",\\\"videoPause\\\":\\\"threshold\\\",\\\"secure\\\":false,\\\"friendly\\\":false}},\\\"type\\\":\\\"VastUrl\\\",\\\"content\\\":\\\"https://s8t.teads.tv/vast/a7165340-d714-11ee-9d18-8d6ab288a268\\\",\\\"connection_id\\\":460794,\\\"scenario_id\\\":18603,\\\"dsp_campaign_id\\\":\\\"590162\\\",\\\"ad_source_id\\\":200,\\\"dsp_creative_id\\\":\\\"625187\\\",\\\"insertion_id\\\":590162,\\\"placement_id\\\":84242,\\\"portfolio_item_id\\\":1,\\\"early_click_protection_duration\\\":0,\\\"exclusiveAdOnScreen\\\":false}],\\\"wigoEnabled\\\":false,\\\"placementMetadata\\\":{\\\"84242\\\":{\\\"auctionId\\\":\\\"a9995fbe-1cb5-4e1a-858f-266903d7d772\\\"}},\\\"viewerId\\\":\\\"b1aefcc416eb3116e70d52e5ac0618abcf6645f4\\\"}"
        val FAKE_WINNING_BID_RESPONSE = "{\"id\":\"prebid-demo-response-video-outstream\",\"impid\":\"03ec3cdd-e144-40bd-98cc-1947235ce897\",\"price\":0.11701999999468729,\"nurl\":\"https://localhost:8080/prebid-server/win-notice?data=base64&clearingPrice=${1000}\",\"adm\":\"${FAKE_ADM_CONTENT_VAST_URL}\",\"adid\":\"test-ad-id-12345\",\"adomain\":[\"prebid.org\"],\"crid\":\"test-creative-id-1\",\"cid\":\"test-cid-1\",\"ext\":{\"prebid\":{\"type\":\"video\",\"targeting\":{\"hb_pb\":\"0.10\",\"hb_env\":\"mobile-app\",\"hb_size_prebid\":\"300x250\",\"hb_pb_prebid\":\"0.10\",\"hb_bidder_prebid\":\"prebid\",\"hb_size\":\"300x250\",\"hb_bidder\":\"prebid\",\"hb_env_prebid\":\"mobile-app\"},\"meta\":{\"renderername\":\"SampleRendererName\",\"rendererversion\":\"1.0\"}},\"origbidcpm\":0.11701999999468729,\"origbidcur\":\"USD\"}}"
    }
}
