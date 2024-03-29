package tv.teads.teadssdkdemo.format.infeed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import tv.teads.sdk.*
import tv.teads.sdk.utils.userConsent.TCFVersion
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.data.SessionDataSource.FAKE_GDPR_STR
import tv.teads.teadssdkdemo.databinding.FragmentInfeedViewScrollviewBinding
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * InFeed format within a ScrollView
 */
class InFeedScrollViewFragment : BaseFragment() {
    private lateinit var binding: FragmentInfeedViewScrollviewBinding

    private var nativeAd: NativeAd? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfeedViewScrollviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.integration_header).text = getTitle()

        // 1. Setup the settings, don't use fake values on production
        val placementSettings = AdPlacementSettings.Builder()
                .enableDebug()
                .userConsent("1", FAKE_GDPR_STR, TCFVersion.V2, 7)
                .build()

        // 2. Create the NativeAdPlacement
        val adPlacement = TeadsSDK.createNativePlacement(requireActivity(), pid, placementSettings)

        // 3. Request the ad and register to the listener in it
        val requestSettings = AdRequestSettings.Builder()
                .pageSlotUrl("http://teads.com")
                .build()

        adPlacement.requestAd(requestSettings,
                object : NativeAdListener {
                    override fun onAdReceived(nativeAd: NativeAd) {
                        this@InFeedScrollViewFragment.nativeAd = nativeAd
                        binding.nativeAdView.visibility = View.VISIBLE
                        binding.nativeAdView.bind(nativeAd)
                    }

                    override fun onAdClicked() {
                        Log.d("NativeAdListener", "onAdClicked")
                    }

                    override fun onAdError(code: Int, description: String) {
                        Log.d("NativeAdListener", "onAdError")
                    }

                    override fun onAdImpression() {
                        Log.d("NativeAdListener", "onAdImpression")
                    }

                    override fun onFailToReceiveAd(failReason: String) {
                        Log.d("NativeAdListener", "onFailToReceiveAd")
                    }

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
        nativeAd?.clean()
    }

    override fun getTitle(): String = "InFeed Direct ScrollView"
}
