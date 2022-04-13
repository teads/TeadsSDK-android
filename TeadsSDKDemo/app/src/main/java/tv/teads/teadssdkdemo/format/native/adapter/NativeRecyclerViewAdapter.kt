package tv.teads.teadssdkdemo.format.native.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import tv.teads.sdk.*
import tv.teads.sdk.renderer.NativeAdView
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.component.GenericRecyclerViewAdapter
import tv.teads.teadssdkdemo.data.RecyclerItemType

/**
 * Simple RecyclerView adapter
 */
class NativeRecyclerViewAdapter(
    private val context: Context?,
    pid: Int,
    title: String,
    private val isGrid: Boolean = false,
) : GenericRecyclerViewAdapter(title) {

    private var requestSettings: AdRequestSettings
    private val adPlacement: NativeAdPlacement

    init {
        // 1. Setup the settings
        val placementSettings = AdPlacementSettings.Builder().build()
        requestSettings = AdRequestSettings.Builder().build()

        // 2. Create the NativeAdPlacement
        adPlacement = TeadsSDK.createNativePlacement(context!!, pid, placementSettings)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> RecyclerItemType.TYPE_SCROLL_DOWN.value
            1 -> if (isGrid) RecyclerItemType.TYPE_EMPTY.value else RecyclerItemType.TYPE_FAKE_FEED.value
            4 -> RecyclerItemType.TYPE_TEADS.value
            else -> RecyclerItemType.TYPE_FAKE_FEED.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecyclerItemType.TYPE_TEADS.value -> {
                val nativeView = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_native_ad,
                    parent,
                    false
                )

                ViewHolderDemo(nativeView)
            }
            RecyclerItemType.TYPE_ARTICLE_FAKE_LINES.value -> {
                val fakeFeed = LayoutInflater.from(parent.context).inflate(
                    if (isGrid) R.layout.item_fake_feed_grid else R.layout.item_fake_feed,
                    parent,
                    false
                )

                ViewHolderDemo(fakeFeed)
            }
            RecyclerItemType.TYPE_EMPTY.value -> {
                ViewHolderDemo(FrameLayout(context))
            }
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            RecyclerItemType.TYPE_TEADS.value -> {
                val nativeAdView = holder.itemView.findViewById<NativeAdView>(R.id.nativeAdView)

                adPlacement.requestAd(requestSettings, object : NativeAdListener {
                    override fun onAdReceived(nativeAd: NativeAd) {
                        nativeAdView.bind(nativeAd)
                    }
                })
            }
            else -> super.onBindViewHolder(holder, position)
        }
    }

    override fun getItemCount(): Int {
        return 12
    }
}
