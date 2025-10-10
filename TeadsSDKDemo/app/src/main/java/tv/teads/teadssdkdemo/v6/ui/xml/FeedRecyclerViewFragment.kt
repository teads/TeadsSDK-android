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

class FeedRecyclerViewFragment : Fragment(), TeadsAdPlacementEventsDelegate {

    private lateinit var feedAd: TeadsAdPlacementFeed

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
        val recyclerView = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ArticleRecyclerViewAdapter(feedAd)
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
        Log.d("FeedRecyclerViewFragment", "$placement - $event: $data")
        
        if (event == TeadsAdPlacementEventName.CLICKED_ORGANIC) {
            val url = data?.get("url") as? String
            url?.let {
                BrowserNavigationHelper.openInnerBrowser(requireContext(), it)
            }
        }
    }

    class ArticleRecyclerViewAdapter(
        private val feedAd: TeadsAdPlacementFeed,
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

        // ViewHolder for Teads Feed placement
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
                    viewHolder.textView.setTypeface(viewHolder.textView.typeface, android.graphics.Typeface.BOLD)
                }
                3 -> {
                    val viewHolder = holder as TextItemViewHolder
                    viewHolder.textView.text = viewHolder.textView.context.resources.getString(R.string.article_template_body_b)
                    viewHolder.textView.setTypeface(viewHolder.textView.typeface, android.graphics.Typeface.NORMAL)
                }
                4 -> {
                    val viewHolder = holder as TextItemViewHolder
                    viewHolder.textView.text = viewHolder.textView.context.resources.getString(R.string.article_template_body_c)
                    viewHolder.textView.setTypeface(viewHolder.textView.typeface, android.graphics.Typeface.NORMAL)
                }
                5 -> {
                    val viewHolder = holder as TextItemViewHolder
                    viewHolder.textView.text = viewHolder.textView.context.resources.getString(R.string.article_template_body_d)
                    viewHolder.textView.setTypeface(viewHolder.textView.typeface, android.graphics.Typeface.NORMAL)
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
            // 4. Request ad
            val adView = feedAd.loadAd()

            // 5. Add in the ad container
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

            private const val TOTAL_ITEM_COUNT = 7
        }
    }
}
