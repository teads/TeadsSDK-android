package tv.teads.teadssdkdemo.format.inread

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementFeed
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementMedia
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementMediaNative
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementRecommendations
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementFeedConfig
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementMediaConfig
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementMediaNativeConfig
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementRecommendationsConfig
import tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate
import tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.databinding.FragmentCombinedSdkScrollviewBinding
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.views.MediaNativeAdView
import tv.teads.teadssdkdemo.views.RecommendationsAdView

/**
 * Media format within a ScrollView
 */
class CombinedSDKScrollViewFragment : BaseFragment() {
    private lateinit var binding: FragmentCombinedSdkScrollviewBinding
    private var mediaNativeAdView: MediaNativeAdView? = null

    private val articleUrl = "https://mobile-demo.outbrain.com/".toUri()

    private val eventsDelegate = object : TeadsAdPlacementEventsDelegate {
        override fun onPlacementEvent(
            placement: TeadsAdPlacement<*, *>,
            event: TeadsAdPlacementEventName,
            data: Map<String, Any>?
        ) {
            Log.d("TeadsAdPlacementEvents", "Placement: $placement; Event: $event; Data: $data")

            if (placement is TeadsAdPlacementMediaNative
                && event == TeadsAdPlacementEventName.READY) {
                mediaNativeAdView?.visibility = View.VISIBLE
            }

            if (event == TeadsAdPlacementEventName.CLICKED_ORGANIC) {
                val url = data?.get("url") as? String
                val intent = Intent(Intent.ACTION_VIEW, url?.toUri())
                startActivity(intent)
            }
        }
    }

    private var media: TeadsAdPlacementMedia? = null
    private var mediaNative: TeadsAdPlacementMediaNative? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCombinedSdkScrollviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.integration_header).text = getTitle()

        initSDK()

        setMediaPlacement()
        setMediaNativePlacement()
        setRecommendationsPlacement()
        setFeedPlacement()
    }

    fun setMediaPlacement() {
        // 1. Create config
        val config = TeadsAdPlacementMediaConfig(pid = 84242, articleUrl = articleUrl)

        // 2. Create placement
        media = TeadsAdPlacementMedia(
            context = requireContext(),
            config = config,
            delegate = eventsDelegate
        )
        // 3. Request the ad and add to container
        val adView = media?.loadAd()
        binding.mediaAdSlotContainer.addView(adView)
    }

    fun setMediaNativePlacement() {
        // 1. Create config
        val config = TeadsAdPlacementMediaNativeConfig(
            pid = 124859,
            articleUrl = "http://teads.com".toUri()
        )

        // 2. Create placement
        mediaNative = TeadsAdPlacementMediaNative(
            context = requireContext(),
            config = config,
            delegate = eventsDelegate
        )

        // 3. Create MediaNativeAdView
        mediaNativeAdView = MediaNativeAdView(requireContext())
        binding.mediaNativeAdSlotContainer.addView(mediaNativeAdView)

        // 4. Request the ad and bind it to the native ad view
        mediaNative
            ?.loadAd()
            ?.let { binder ->
                binder(mediaNativeAdView!!.getNativeAdView())
            }
    }

    fun setRecommendationsPlacement() {
        // 1. Create config
        val config = TeadsAdPlacementRecommendationsConfig(
            articleUrl = articleUrl,
            widgetId = "SDK_1",
            widgetIndex = 0
        )

        // 2. Create placement
        val placement = TeadsAdPlacementRecommendations(
            config = config,
            delegate = eventsDelegate
        )

        // 3. Create RecommendationsAdView
        val recommendationsAdView = RecommendationsAdView(requireContext())
        binding.recommendationsAdSlotContainer.addView(recommendationsAdView)

        // 4. Load recommendations asynchronously and bind to view
        lifecycleScope.launch {
            try {
                val response = placement.loadAdSuspend()
                Log.d("TeadsAdPlacementEvents", "Recommendations loaded: ${response.all.size} items")
                recommendationsAdView.bind(response, articleUrl)
            } catch (e: Exception) {
                Log.e("TeadsAdPlacementEvents", "Recommendations failed to load", e)
            }
        }
    }

    fun setFeedPlacement() {
        // 1. Create config
        val config = TeadsAdPlacementFeedConfig(
            articleUrl = articleUrl,
            widgetId = "MB_1",
            installationKey = "NANOWDGT01",
            widgetIndex = 0
        )

        // 2. Create placement
        val placement = TeadsAdPlacementFeed(
            context = requireContext(),
            config = config,
            delegate = eventsDelegate
        )

        // 3. Request the ad and add to container
        val adView = placement.loadAd()
        binding.feedAdSlotContainer.addView(adView)
    }

    private fun initSDK() {
        with(TeadsSDK) {
            configure(requireContext(), "AndroidSampleApp2014") // fixme application context
            testMode = true // Tests only
            testLocation = "us" // Tests only
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        media?.clean()
        mediaNative?.clean()
    }

    override fun getTitle(): String = "Combined SDK ScrollView"
}
