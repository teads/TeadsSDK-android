package tv.teads.teadssdkdemo.format.inread

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
import tv.teads.sdk.InReadAdPlacement
import tv.teads.sdk.InReadAdViewListener
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.VideoPlaybackListener
import tv.teads.sdk.renderer.InReadAdView
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.databinding.FragmentInreadScrollviewBinding
import tv.teads.teadssdkdemo.format.inread.extensions.resizeAdContainer
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * InRead format within a ScrollView
 */
class InReadScrollViewFragment : BaseFragment() {
    private lateinit var binding: FragmentInreadScrollviewBinding
    private lateinit var adPlacement: InReadAdPlacement
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

        // 2. Create the InReadAdPlacement
        adPlacement = TeadsSDK.createInReadPlacement(requireActivity(), pid, placementSettings)

        // 3. Request the ad and listen its events
        val requestSettings = AdRequestSettings.Builder()
                .pageSlotUrl("http://teads.com")
                .build()
        adPlacement.requestAd(requestSettings,
                object : InReadAdViewListener {
                    override fun onAdReceived(ad: InReadAdView, adRatio: AdRatio) {
                        // Clean and init inReadAdView
                        inReadAdView?.clean()
                        inReadAdView = ad
                        // Add ad to the container and resize
                        binding.adSlotContainer.addView(ad)
                        binding.adSlotContainer.resizeAdContainer {
                            adRatio.calculateHeight(binding.adSlotContainer.measuredWidth)
                        }
                    }

                    override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                        // Resize ad container
                        binding.adSlotContainer.addView(trackerView)
                    }

                    override fun onAdRatioUpdate(adRatio: AdRatio) {
                        // Resize the ad container
                        inReadAdView?.let { adView ->
                            binding.adSlotContainer.resizeAdContainer {
                                adRatio.calculateHeight(adView.measuredWidth)
                            }

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

    override fun getTitle(): String = "InRead Direct ScrollView"
}
