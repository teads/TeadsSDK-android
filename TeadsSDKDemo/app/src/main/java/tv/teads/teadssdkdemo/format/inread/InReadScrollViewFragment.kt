package tv.teads.teadssdkdemo.format.inread

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_inread_scrollview.*
import tv.teads.sdk.*
import tv.teads.sdk.renderer.InReadAdView
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * InRead format within a ScrollView
 */
class InReadScrollViewFragment : BaseFragment() {

    private lateinit var adPlacement: InReadAdPlacement
    private var inReadAdView: InReadAdView? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_inread_scrollview, container, false)

        v.findViewById<TextView>(R.id.integration_header).text = getTitle()

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Setup the settings
        val placementSettings = AdPlacementSettings.Builder()
                .enableDebug()
                .build()

        // 2. Create the InReadAdPlacement
        adPlacement = TeadsSDK.createInReadPlacement(requireActivity(), pid, placementSettings)

        // 3. Request the ad and register to the listener in it
        val requestSettings = AdRequestSettings.Builder()
                .pageSlotUrl("http://teads.com")
                .build()
        adPlacement.requestAd(requestSettings,
                object : InReadAdListener {
                    override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                        adSlotView.addView(trackerView)
                    }

                    override fun onAdReceived(inReadAdView: InReadAdView, adRatio: AdRatio) {
                        this@InReadScrollViewFragment.inReadAdView = inReadAdView
                        adSlotView.addView(inReadAdView, 0)
                    }

                    override fun onAdClicked() {}
                    override fun onAdClosed() {}
                    override fun onAdError(code: Int, description: String) {}
                    override fun onAdImpression() {}
                    override fun onAdExpandedToFullscreen() {}
                    override fun onAdCollapsedFromFullscreen() {}
                    override fun onAdRatioUpdate(adRatio: AdRatio) {}
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

    override fun getTitle(): String = "InRead Direct ScrollView"
}
