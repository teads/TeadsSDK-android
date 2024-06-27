package tv.teads.teadssdkdemo.format.inread

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
import tv.teads.teadssdkdemo.databinding.FragmentMultipleSlotsInreadScrollviewBinding
import tv.teads.teadssdkdemo.utils.BaseFragment


/**
 * InRead format within a ScrollView
 */
class MultipleSlotsInReadScrollViewFragment : BaseFragment() {
    private lateinit var binding: FragmentMultipleSlotsInreadScrollviewBinding
    private lateinit var adPlacement: InReadAdPlacement
    private var inReadAdView: InReadAdView? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentMultipleSlotsInreadScrollviewBinding.inflate(layoutInflater)
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

        mutableListOf(
            binding.adSlotViewDynamicSize,
            binding.adSlotView300x250,
            binding.adSlotView300x150,
            binding.adSlotView300x80,
            binding.adSlotView300x50
        ).forEach {
            adPlacement.requestAdMultipleSlots(requestSettings, it)
        }
    }

    private fun InReadAdPlacement.requestAdMultipleSlots(
        requestSettings: AdRequestSettings,
        adSlotView: FrameLayout
    ) {
        adPlacement.requestAd(requestSettings,
            object : InReadAdViewListener {
                override fun onAdReceived(ad: InReadAdView, adRatio: AdRatio) {
                    val layoutParams = ad.layoutParams
                    adSlotView.addView(ad)
                    layoutParams.height = adRatio.calculateHeight(adSlotView.measuredWidth)
                    adSlotView.layoutParams = layoutParams

                    inReadAdView = ad
                }

                override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                    adSlotView.addView(trackerView)
                }

                override fun onAdRatioUpdate(adRatio: AdRatio) {
                    inReadAdView?.let { inReadAdView ->
                        val layoutParams = inReadAdView.layoutParams
                        layoutParams.height = adRatio.calculateHeight(adSlotView.measuredWidth)
                        adSlotView.layoutParams = layoutParams
                    }
                }

                override fun onAdClicked() {}
                override fun onAdClosed() {}
                override fun onAdError(code: Int, description: String) {}
                override fun onAdImpression() {
                    Log.d("Impression", "AdSlot Impression ${adSlotView.tag}")
                }
                override fun onAdExpandedToFullscreen() {}
                override fun onAdCollapsedFromFullscreen() {}
                override fun onFailToReceiveAd(failReason: String) {}
            },
            object : VideoPlaybackListener {
                override fun onVideoComplete() {}
                override fun onVideoPause() {}
                override fun onVideoPlay() {}
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        inReadAdView?.clean()
    }

    override fun getTitle(): String = "Multiple ad slots sizes test"
}
