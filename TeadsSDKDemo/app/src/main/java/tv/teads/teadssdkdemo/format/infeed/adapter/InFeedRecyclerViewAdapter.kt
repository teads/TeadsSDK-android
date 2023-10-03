package tv.teads.teadssdkdemo.format.infeed.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tv.teads.sdk.*
import tv.teads.sdk.renderer.MediaScale
import tv.teads.sdk.renderer.MediaView
import tv.teads.sdk.renderer.NativeAdView
import tv.teads.sdk.utils.userConsent.TCFVersion
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.component.GenericRecyclerViewAdapter
import tv.teads.teadssdkdemo.data.FeedItem.Companion.feedItems
import tv.teads.teadssdkdemo.data.SessionDataSource.FAKE_GDPR_STR
import tv.teads.teadssdkdemo.data.RecyclerItemType

/**
 * Native RecyclerView adapter
 */
class InFeedRecyclerViewAdapter(
    private val context: Context,
    pid: Int,
    title: String,
    private val isGrid: Boolean = false,
) : GenericRecyclerViewAdapter(title) {

    private var requestSettings: AdRequestSettings
    private val adPlacement: NativeAdPlacement

    init {
        // 1. Setup the settings, don't use fake values on production
        val placementSettings = AdPlacementSettings
            .Builder()
            .userConsent("1", FAKE_GDPR_STR, TCFVersion.V2, 7)
            .setMediaScale(MediaScale.CENTER_CROP)
            .build()

        requestSettings = AdRequestSettings.Builder().build()

        // 2. Create the NativeAdPlacement
        adPlacement = TeadsSDK.createNativePlacement(context, pid, placementSettings)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> if (isGrid) RecyclerItemType.TYPE_FAKE_FEED.value else RecyclerItemType.TYPE_SCROLL_DOWN.value
            4 -> RecyclerItemType.TYPE_NATIVE_AD.value
            else -> RecyclerItemType.TYPE_FAKE_FEED.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecyclerItemType.TYPE_NATIVE_AD.value -> {
                val nativeView = LayoutInflater.from(parent.context).inflate(
                    if (isGrid) R.layout.item_native_ad_grid else R.layout.item_native_ad,
                    parent,
                    false
                )

                ViewHolderDemo(nativeView)
            }
            RecyclerItemType.TYPE_FAKE_FEED.value -> {
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
            RecyclerItemType.TYPE_NATIVE_AD.value -> {
                val nativeAdView = holder.itemView.findViewById<NativeAdView>(R.id.nativeAdView)

                adPlacement.requestAd(requestSettings, object : NativeAdListener {
                    override fun onAdReceived(nativeAd: NativeAd) {
                        nativeAdView.findViewById<MediaView>(R.id.teads_native_media).mediaScale = MediaScale.CENTER_INSIDE
                        nativeAdView.bind(nativeAd)
                    }
                })
            }
            RecyclerItemType.TYPE_FAKE_FEED.value -> {
                val positionInList = position % 9

                with(holder.itemView) {
                    findViewById<TextView>(R.id.feed_title).text = feedItems[positionInList].title
                    findViewById<TextView>(R.id.feed_body).text = feedItems[positionInList].body
                    findViewById<TextView>(R.id.feed_source).text = feedItems[positionInList].source
                    findViewById<TextView>(R.id.feed_time).text = feedItems[positionInList].time
                    findViewById<ImageView>(R.id.feed_image).setImageResource(feedItems[positionInList].image)
                }
            }
            else -> super.onBindViewHolder(holder, position)
        }
    }

    override fun getItemCount(): Int = feedItems.size + 1
}
