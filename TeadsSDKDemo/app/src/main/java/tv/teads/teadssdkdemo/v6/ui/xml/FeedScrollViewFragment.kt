package tv.teads.teadssdkdemo.v6.ui.xml

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementFeed
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementFeedConfig
import tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate
import tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.utils.BrowserNavigationHelper
import tv.teads.teadssdkdemo.v6.utils.ThemeUtils

class FeedScrollViewFragment : Fragment(), TeadsAdPlacementEventsDelegate {

    private lateinit var feedAd: TeadsAdPlacementFeed

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.article_scroll_view_template_bottom_ad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupContent()
    }

    private fun setupContent() {
        val adContainer = requireView().findViewById<ViewGroup>(R.id.ad_container)

        // 1. Init configuration
        val config = TeadsAdPlacementFeedConfig(
            widgetId = DemoSessionConfiguration.getWidgetIdOrDefault(), // Your unique widget id
            articleUrl = DemoSessionConfiguration.getArticleUrlOrDefault().toUri(), // Your article url
            installationKey = DemoSessionConfiguration.getInstallationKeyOrDefault(), // Your unique installation key
            widgetIndex = 0, // Position of the ad within your article content. Increment the number by 1 for each additional ad
            darkMode = ThemeUtils.isDarkModeEnabled(requireContext())
        )

        // 2. Create placement
        feedAd = TeadsAdPlacementFeed(
            context = requireContext(),
            config = config,
            delegate = this // events listener
        )

        // 3. Request ad
        val adView = feedAd.loadAd()

        // 4. Add in the ad container
        adContainer.addView(adView)
    }

    private fun initTeadsSDK() {
        TeadsSDK.configure(
            applicationContext = requireContext().applicationContext,
            appKey = "AndroidSampleApp2014" // Your unique application key
        )


        // For testing purposes
        if (BuildConfig.DEBUG) {
            TeadsSDK.testMode = true // Enable more logging visibility
            TeadsSDK.testLocation = "us" // Emulates location for placements [Feed, Recommendations]
        }
    }

    override fun onPlacementEvent(
        placement: TeadsAdPlacement<*, *>,
        event: TeadsAdPlacementEventName,
        data: Map<String, Any>?
    ) {
        // 5. Stay tuned to lifecycle events
        Log.d("FeedScrollViewFragment", "$placement - $event: $data")

        if (event == TeadsAdPlacementEventName.CLICKED_ORGANIC) {
            val url = data?.get("url") as? String
            url?.let {
                BrowserNavigationHelper.openInnerBrowser(requireContext(), it)
            }
        }
    }
}
