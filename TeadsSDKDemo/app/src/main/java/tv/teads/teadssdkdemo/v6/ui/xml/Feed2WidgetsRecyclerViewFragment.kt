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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.BuildConfig
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

class Feed2WidgetsRecyclerViewFragment : Fragment(), TeadsAdPlacementEventsDelegate {

    private lateinit var feedAd: TeadsAdPlacementFeed
    private lateinit var feedAd2: TeadsAdPlacementFeed
    private lateinit var articleAdapter: ArticleRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 0. Init SDK
        initTeadsSDK()

        // 1. Init configuration for first ad (widgetIndex = 0)
        val config = TeadsAdPlacementFeedConfig(
            widgetId = DemoSessionConfiguration.getWidgetIdOrDefault(),
            articleUrl = DemoSessionConfiguration.getArticleUrlOrDefault().toUri(),
            installationKey = DemoSessionConfiguration.getInstallationKeyOrDefault(),
            widgetIndex = 0, // First ad position
            darkMode = ThemeUtils.isDarkModeEnabled(requireContext())
        )

        // 2. Init configuration for second ad (widgetIndex = 1)
        val config2 = config.copy(
            widgetIndex = 1
        )

        // 3. Create placements
        feedAd = TeadsAdPlacementFeed(
            context = requireContext(),
            config = config,
            delegate = this
        )

        feedAd2 = TeadsAdPlacementFeed(
            context = requireContext(),
            config = config2,
            delegate = this
        )

        setupRecyclerViewContent(view)
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

    private fun setupRecyclerViewContent(view: View) {
        articleAdapter = ArticleRecyclerViewAdapter(feedAd, feedAd2)

        val recyclerView = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
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
        Log.d(
            "Feed2WidgetsRecyclerView",
            "$placement (index = ${(placement as TeadsAdPlacementFeed).config.widgetIndex}) - $event: $data"
        )

        // Wait for the first ad to be fully loaded before loading the next one
        if (placement === feedAd && event == TeadsAdPlacementEventName.LOADED) {
            articleAdapter.isFirstAdReady = true
            view?.post {
                articleAdapter.notifyItemChanged(ArticleRecyclerViewAdapter.SECOND_AD_POSITION)
            }
        }

        if (event == TeadsAdPlacementEventName.CLICKED_ORGANIC) {
            val url = data?.get("url") as? String
            url?.let {
                BrowserNavigationHelper.openInnerBrowser(requireContext(), it)
            }
        }
    }

    class ArticleRecyclerViewAdapter(
        private val feedAd: TeadsAdPlacementFeed,
        private val feedAd2: TeadsAdPlacementFeed,
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var isFirstAdReady = false

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

        // ViewHolder for Teads Feed placement
        class TeadsPlacementViewHolder internal constructor(var layout: View) : RecyclerView.ViewHolder(layout) {
            var container: FrameLayout = layout.findViewById(R.id.ad_container)
        }

        override fun getItemViewType(position: Int): Int {
            return when (position) {
                0 -> IMAGE_VIEW_TYPE
                1 -> TITLE_VIEW_TYPE
                FIRST_AD_POSITION -> TEADS_PLACEMENT_VIEW_TYPE
                SECOND_AD_POSITION -> TEADS_PLACEMENT_VIEW_TYPE_2
                else -> TEXT_VIEW_TYPE
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val v: View
            return when (viewType) {
                IMAGE_VIEW_TYPE -> {
                    v = inflater.inflate(R.layout.item_article_label, parent, false)
                    ImageItemViewHolder(v)
                }
                TITLE_VIEW_TYPE -> {
                    v = inflater.inflate(R.layout.item_article_title, parent, false)
                    TitleItemViewHolder(v)
                }
                TEADS_PLACEMENT_VIEW_TYPE, TEADS_PLACEMENT_VIEW_TYPE_2 -> {
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
                2, 7 -> {
                    val viewHolder = holder as TextItemViewHolder
                    viewHolder.textView.text = viewHolder.textView.context.resources.getString(R.string.article_template_body_a)
                }
                3, 8 -> {
                    val viewHolder = holder as TextItemViewHolder
                    viewHolder.textView.text = viewHolder.textView.context.resources.getString(R.string.article_template_body_b)
                }
                4, 9 -> {
                    val viewHolder = holder as TextItemViewHolder
                    viewHolder.textView.text = viewHolder.textView.context.resources.getString(R.string.article_template_body_c)
                }
                5, 10 -> {
                    val viewHolder = holder as TextItemViewHolder
                    viewHolder.textView.text = viewHolder.textView.context.resources.getString(R.string.article_template_body_d)
                }
                FIRST_AD_POSITION -> {
                    val viewHolder = holder as TeadsPlacementViewHolder
                    if (viewHolder.container.isEmpty()) {
                        createTeadsPlacement(viewHolder.container, feedAd)
                    }
                }
                SECOND_AD_POSITION -> {
                    val viewHolder = holder as TeadsPlacementViewHolder
                    if (isFirstAdReady && viewHolder.container.isEmpty()) {
                        createTeadsPlacement(viewHolder.container, feedAd2)
                    }
                }
                else -> {}
            }
        }

        private fun createTeadsPlacement(container: FrameLayout, feedAd: TeadsAdPlacementFeed) {
            val adView = feedAd.loadAd()
            container.addView(adView)
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
            private const val TEADS_PLACEMENT_VIEW_TYPE_2 = 5

            private const val FIRST_AD_POSITION = 6
            const val SECOND_AD_POSITION = 11
            private const val TOTAL_ITEM_COUNT = 12
        }
    }
}
