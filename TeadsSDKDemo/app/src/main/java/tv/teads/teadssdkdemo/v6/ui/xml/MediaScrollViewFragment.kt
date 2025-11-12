package tv.teads.teadssdkdemo.v6.ui.xml

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import tv.teads.teadssdkdemo.BuildConfig
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementMedia
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementMediaConfig
import tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate
import tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement
import tv.teads.sdk.framework.FrameworkType
import tv.teads.sdk.framework.FrameworkVersion
import tv.teads.sdk.framework.WrapperFrameworkInfo
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration

class MediaScrollViewFragment : Fragment(), TeadsAdPlacementEventsDelegate {

    private lateinit var mediaAd: TeadsAdPlacementMedia

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.article_scroll_view_template_middle_ad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupContent()
    }

    private fun setupContent() {
        val adContainer = requireView().findViewById<ViewGroup>(R.id.ad_container)

        // 0. Enable more logging visibility for testing purposes
        TeadsSDK.testMode = BuildConfig.DEBUG
        // todo for testing — remove before merge on master
        TeadsSDK.wrapperFrameworkInfo = WrapperFrameworkInfo.reactNative("1.0.0")

        // 1. Init configuration
        val config = TeadsAdPlacementMediaConfig(
            pid = DemoSessionConfiguration.getPlacementIdOrDefault().toInt(), // Your unique placement id
            articleUrl = DemoSessionConfiguration.getArticleUrlOrDefault().toUri() // Your article url
        )

        // 2. Create placement
        mediaAd = TeadsAdPlacementMedia(
            context = requireContext(),
            config = config,
            delegate = this // events listener
        )

        // 3. Request ad
        val adView = mediaAd.loadAd()

        // 4. Add in the ad container
        adContainer.addView(adView)
    }

    override fun onPlacementEvent(
        placement: TeadsAdPlacement<*, *>,
        event: TeadsAdPlacementEventName,
        data: Map<String, Any>?
    ) {
        // 5. Stay tuned to lifecycle events
        Log.d("", "$placement - $event: $data")
    }

    override fun onDestroy() {
        super.onDestroy()
        // 6. Clean from memory
        mediaAd.clean()
    }
}
