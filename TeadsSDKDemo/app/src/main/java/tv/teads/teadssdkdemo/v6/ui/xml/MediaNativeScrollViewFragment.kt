package tv.teads.teadssdkdemo.v6.ui.xml

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementMediaNative
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementMediaNativeConfig
import tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate
import tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.views.MediaNativeAdView

class MediaNativeScrollViewFragment : Fragment(), TeadsAdPlacementEventsDelegate {

    private lateinit var mediaNativeAd: TeadsAdPlacementMediaNative
    private lateinit var mediaNativeAdView: MediaNativeAdView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.article_scroll_view_template, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupContent()
    }

    private fun setupContent() {
        val adContainer = requireView().findViewById<ViewGroup>(R.id.ad_container)

        // 1. Create your custom media native ad view
        mediaNativeAdView = MediaNativeAdView(requireContext())

        // 2. Add to container
        adContainer.addView(mediaNativeAdView)

        // 3. Init configuration
        val config = TeadsAdPlacementMediaNativeConfig(
            pid = DemoSessionConfiguration.getPlacementIdOrDefault().toInt(), // Your unique placement id
            articleUrl = DemoSessionConfiguration.getArticleUrlOrDefault().toUri() // Your article url
        )

        // 4. Create placement
        mediaNativeAd = TeadsAdPlacementMediaNative(
            context = requireContext(),
            config = config,
            delegate = this // events listener
        )

        // 5. Load the ad and bind it to the native ad view
        mediaNativeAd
            .loadAd()
            .let { binder ->
                binder(mediaNativeAdView.getNativeAdView())
            }
    }

    override fun onPlacementEvent(
        placement: TeadsAdPlacement<*, *>,
        event: TeadsAdPlacementEventName,
        data: Map<String, Any>?
    ) {
        // 6. Stay tuned to lifecycle events
        Log.d("MediaNativeScrollViewFragment", "$placement - $event: $data")

        if (event == TeadsAdPlacementEventName.READY) {
            mediaNativeAdView.isVisible(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 7. Clean from memory
        mediaNativeAd.clean()
    }
}
