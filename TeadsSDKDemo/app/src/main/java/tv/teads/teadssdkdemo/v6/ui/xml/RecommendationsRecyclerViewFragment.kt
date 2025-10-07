package tv.teads.teadssdkdemo.v6.ui.xml

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import tv.teads.teadssdkdemo.BuildConfig
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementRecommendations
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementRecommendationsConfig
import tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate
import tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.v6.ui.base.recommendations.RecommendationsAdView

class RecommendationsRecyclerViewFragment : Fragment(), TeadsAdPlacementEventsDelegate {

    private lateinit var recommendationsAd: TeadsAdPlacementRecommendations

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 0. Enable more logging visibility for testing purposes
        TeadsSDK.testMode = BuildConfig.DEBUG

        // 1. Init configuration
        val config = TeadsAdPlacementRecommendationsConfig(
            articleUrl = DemoSessionConfiguration.getArticleUrlOrDefault().toUri(), // Your article url
            widgetId = DemoSessionConfiguration.getWidgetIdOrDefault() // Your widget id
        )

        // 2. Create placement
        recommendationsAd = TeadsAdPlacementRecommendations(
            config = config,
            delegate = this // events listener
        )

        setupRecyclerViewContent(view)
    }

    private fun setupRecyclerViewContent(view: View) {
        val recyclerView = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ArticleRecyclerViewAdapter(recommendationsAd)
        }

        view.findViewById<LinearLayout>(R.id.content_container)?.apply {
            addView(recyclerView)
        }
    }

    override fun onPlacementEvent(
        placement: TeadsAdPlacement<*, *>,
        event: TeadsAdPlacementEventName,
        data: Map<String, Any>?
    ) {
        // 3. Stay tuned to lifecycle events
        Log.d("RecommendationsRecyclerViewFragment", "$placement - $event: $data")
    }

    class ArticleRecyclerViewAdapter(
        private val recommendationsAd: TeadsAdPlacementRecommendations,
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        // ViewHolder for article image
        class ImageItemViewHolder internal constructor(var layout: View) : RecyclerView.ViewHolder(layout)

        // ViewHolder for article title
        class TitleItemViewHolder internal constructor(var layout: View) : RecyclerView.ViewHolder(layout) {
            var textView: TextView = layout.findViewById(R.id.article_title)
        }

        // ViewHolder for article body text
        class TextItemViewHolder internal constructor(var layout: View) : RecyclerView.ViewHolder(layout) {
            var textView: TextView = layout.findViewById(R.id.article_body)
        }

        // ViewHolder for Teads Recommendations placement
        class TeadsPlacementViewHolder internal constructor(var layout: View) : RecyclerView.ViewHolder(layout) {
            var container: FrameLayout = layout.findViewById(R.id.ad_container)
        }

        override fun getItemViewType(position: Int): Int {
            return when (position) {
                0 -> IMAGE_VIEW_TYPE
                1 -> TITLE_VIEW_TYPE
                6 -> TEADS_PLACEMENT_VIEW_TYPE
                else -> TEXT_VIEW_TYPE
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val v: View
            return when (viewType) {
                IMAGE_VIEW_TYPE -> {
                    v = inflater.inflate(R.layout.item_article_image, parent, false)
                    ImageItemViewHolder(v)
                }
                TITLE_VIEW_TYPE -> {
                    v = inflater.inflate(R.layout.item_article_title, parent, false)
                    TitleItemViewHolder(v)
                }
                TEADS_PLACEMENT_VIEW_TYPE -> {
                    v = inflater.inflate(R.layout.item_ad_container, parent, false)
                    TeadsPlacementViewHolder(v)
                }
                else -> {
                    v = inflater.inflate(R.layout.item_article_body, parent, false)
                    TextItemViewHolder(v)
                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (position) {
                2-> {
                    val viewHolder = holder as TextItemViewHolder
                    viewHolder.textView.text = viewHolder.textView.context.resources.getString(R.string.article_template_body_a)
                }
                3 -> {
                    val viewHolder = holder as TextItemViewHolder
                    viewHolder.textView.text = viewHolder.textView.context.resources.getString(R.string.article_template_body_b)
                }
                4 -> {
                    val viewHolder = holder as TextItemViewHolder
                    viewHolder.textView.text = viewHolder.textView.context.resources.getString(R.string.article_template_body_c)
                }
                5 -> {
                    val viewHolder = holder as TextItemViewHolder
                    viewHolder.textView.text = viewHolder.textView.context.resources.getString(R.string.article_template_body_d)
                }
                6 -> {
                    val viewHolder = holder as TeadsPlacementViewHolder
                    if (viewHolder.container.isEmpty()) {
                        createTeadsPlacement(viewHolder.container)
                    }
                }
                else -> {}
            }
        }

        private fun createTeadsPlacement(container: FrameLayout) {
            // 4. Create your custom recommendations ad view
            val recommendationsAdView = RecommendationsAdView(container.context)

            // 5. Add to container
            container.addView(recommendationsAdView)

            // 6. Load the recommendations asynchronously and bind to view
            val fragment = container.context as? Fragment
            fragment?.lifecycleScope?.launch {
            try {
                val response = recommendationsAd.loadAdSuspend()
                recommendationsAdView.bind(
                    recommendations = response,
                    articleUrl = recommendationsAd.config.articleUrl
                )
            } catch (e: Exception) {
                Log.e("RecommendationsRecyclerViewFragment", "Recommendations failed to load", e)
            }
            }
        }

        override fun getItemCount(): Int {
            return TOTAL_ITEM_COUNT
        }

        companion object {
            // RecyclerView view types
            private const val IMAGE_VIEW_TYPE = 1
            private const val TITLE_VIEW_TYPE = 2
            private const val TEXT_VIEW_TYPE = 3
            private const val TEADS_PLACEMENT_VIEW_TYPE = 4

            private const val TOTAL_ITEM_COUNT = 7
        }
    }
}
