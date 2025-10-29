package tv.teads.teadssdkdemo.v6.ui.xml

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementRecommendations
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementRecommendationsConfig
import tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate
import tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement
import tv.teads.teadssdkdemo.BuildConfig
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.ui.base.recommendations.RecommendationsAdView

class RecommendationsScrollViewFragment : Fragment(), TeadsAdPlacementEventsDelegate {

    private lateinit var recommendationsAd: TeadsAdPlacementRecommendations
    private lateinit var recommendationsAdView: RecommendationsAdView

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

        // 0. Init SDK
        initTeadsSDK()

        // 1. Create your custom recommendations ad view
        recommendationsAdView = RecommendationsAdView(requireContext())

        // 2. Add to container
        adContainer.addView(recommendationsAdView)

        // 3. Init configuration
        val config = TeadsAdPlacementRecommendationsConfig(
            articleUrl = DemoSessionConfiguration.getArticleUrlOrDefault().toUri(), // Your article url
            widgetId = DemoSessionConfiguration.getWidgetIdOrDefault() // Your widget id
        )

        // 4. Create placement
        recommendationsAd = TeadsAdPlacementRecommendations(
            config = config,
            delegate = this // events listener
        )

        // 5. Load the recommendations asynchronously and bind to view
        lifecycleScope.launch {
            try {
                val response = recommendationsAd.loadAdSuspend()
                recommendationsAdView.bind(
                    recommendations = response,
                    articleUrl = config.articleUrl
                )
            } catch (e: Exception) {
                Log.e("RecommendationsScrollViewFragment", "Recommendations failed to load", e)
            }
        }
    }

    /**
     * Initialize TeadsSDK - can be init once on the start of the app
     */
    private fun initTeadsSDK() {
        // Mandatory for placements [Feed, Recommendations]
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
        // 6. Stay tuned to lifecycle events
        Log.d("RecommendationsScrollViewFragment", "$placement - $event: $data")
    }
}
