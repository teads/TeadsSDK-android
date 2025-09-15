package tv.teads.teadssdkdemo.format.inread

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import tv.teads.sdk.AdPlacementSettings
import tv.teads.sdk.AdRequestSettings
import tv.teads.sdk.InReadAdPlacement
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementMedia
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementMediaConfig
import tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate
import tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement
import tv.teads.sdk.renderer.InReadAdView
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.databinding.FragmentInreadScrollviewBinding
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

        val config = TeadsAdPlacementMediaConfig(pid = pid)

        // 2. Create the InReadAdPlacement
        adPlacement = TeadsSDK.createInReadPlacement(requireActivity(), pid, placementSettings)

        val placement = TeadsAdPlacementMedia(
            requireContext(), config, object : TeadsAdPlacementEventsDelegate {
                override fun onPlacementEvent(
                    placement: TeadsAdPlacement<*, *>,
                    event: TeadsAdPlacementEventName,
                    data: Map<String, Any>?
                ) {
                    Log.d("onPlacementEvent", "$event $data")
                }

            }
        )



        // 3. Request the ad and listen its events
        val requestSettings = AdRequestSettings.Builder()
            .pageSlotUrl("http://teads.com")
            .build()

//        adPlacement.requestAd(requestSettings,
//            object : InReadAdViewListener {
//                override fun onAdReceived(ad: InReadAdView, adRatio: AdRatio) {
//                     Clean and init inReadAdView
//                    inReadAdView?.clean()
//                    inReadAdView = ad
//                     Add ad to the container and resize
//                    binding.adSlotContainer.addView(ad)
//                    binding.adSlotContainer.resizeAdContainer(adRatio)
//                }
//
//                override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
//                    binding.adSlotContainer.addView(trackerView)
//                }
//
//                override fun onAdRatioUpdate(adRatio: AdRatio) {
//                     Resize the ad container
//                    binding.adSlotContainer.resizeAdContainer(adRatio)
//                }
//
//                override fun onAdClicked() {}
//                override fun onAdClosed() {}
//                override fun onAdError(code: Int, description: String) {}
//                override fun onAdImpression() {}
//                override fun onAdExpandedToFullscreen() {}
//                override fun onAdCollapsedFromFullscreen() {}
//                override fun onFailToReceiveAd(failReason: String) {}
//            },
//            object : VideoPlaybackListener {
//                override fun onVideoComplete() {
//                    Log.d("PlaybackEvent", "complete")
//                }
//
//                override fun onVideoPause() {
//                    Log.d("PlaybackEvent", "pause")
//                }
//
//                override fun onVideoPlay() {
//                    Log.d("PlaybackEvent", "play")
//                }
//
//            }
//        )

        val adView = placement.loadAd()
        binding.adSlotContainer.addView(adView)
    }

    override fun onDestroy() {
        super.onDestroy()
        inReadAdView?.clean()
    }

    override fun getTitle(): String = "InRead Direct ScrollView"
}
