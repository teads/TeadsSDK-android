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
import tv.teads.sdk.combinedsdk.TeadsAdPlacementEventName
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementMediaNative
import tv.teads.sdk.combinedsdk.adplacement.config.TeadsAdPlacementMediaNativeConfig
import tv.teads.sdk.combinedsdk.adplacement.interfaces.TeadsAdPlacementEventsDelegate
import tv.teads.sdk.combinedsdk.adplacement.interfaces.core.TeadsAdPlacement
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.teadssdkdemo.views.MediaNativeAdView

class MediaNativeRecyclerViewFragment : Fragment(), TeadsAdPlacementEventsDelegate {

    private lateinit var mediaNativeAd: TeadsAdPlacementMediaNative

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Init configuration
        val config = TeadsAdPlacementMediaNativeConfig(
            pid = DemoSessionConfiguration.getPlacementIdOrDefault().toInt(), // Your unique placement id
            articleUrl = DemoSessionConfiguration.getArticleUrlOrDefault().toUri() // Your article url
        )

        // 2. Create placement
        mediaNativeAd = TeadsAdPlacementMediaNative(
            context = requireContext(),
            config = config,
            delegate = this // events listener
        )

        setupRecyclerViewContent(view)
    }

    private fun setupRecyclerViewContent(view: View) {
        val recyclerView = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ArticleRecyclerViewAdapter(mediaNativeAd)
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
        Log.d("MediaNativeRecyclerViewFragment", "$placement - $event: $data")
    }

    override fun onDestroy() {
        super.onDestroy()
        // 4. Clean from memory
        if (::mediaNativeAd.isInitialized) {
            mediaNativeAd.clean()
        }
    }

    class ArticleRecyclerViewAdapter(
        private val mediaNativeAd: TeadsAdPlacementMediaNative,
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

        // ViewHolder for Teads Media Native placement
        class TeadsPlacementViewHolder internal constructor(var layout: View) : RecyclerView.ViewHolder(layout) {
            var container: FrameLayout = layout.findViewById(R.id.ad_container)
        }

        override fun getItemViewType(position: Int): Int {
            return when (position) {
                0 -> IMAGE_VIEW_TYPE
                1 -> TITLE_VIEW_TYPE
                5 -> TEADS_PLACEMENT_VIEW_TYPE
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
                    val viewHolder = holder as TeadsPlacementViewHolder
                    if (viewHolder.container.isEmpty()) {
                        createTeadsPlacement(viewHolder.container)
                    }
                }
                6 -> {
                    val viewHolder = holder as TextItemViewHolder
                    viewHolder.textView.text = viewHolder.textView.context.resources.getString(R.string.article_template_body_d)
                }
                else -> {}
            }
        }

        private fun createTeadsPlacement(container: FrameLayout) {
            // 5. Create your custom media native ad view
            val mediaNativeAdView = MediaNativeAdView(container.context)

            // 6. Add to container
            container.addView(mediaNativeAdView)

            // 7. Load the ad and bind it to the native ad view
            mediaNativeAd
                .loadAd()
                .let { binder ->
                    binder(mediaNativeAdView.getNativeAdView())
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
