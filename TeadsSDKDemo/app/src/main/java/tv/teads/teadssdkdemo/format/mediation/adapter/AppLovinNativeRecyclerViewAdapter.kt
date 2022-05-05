package tv.teads.teadssdkdemo.format.mediation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.component.GenericRecyclerViewAdapter
import tv.teads.teadssdkdemo.data.FeedItem.Companion.feedItems
import tv.teads.teadssdkdemo.data.RecyclerItemType

/**
 * AppLovin Native RecyclerView adapter
 */
class AppLovinNativeRecyclerViewAdapter(
    private val context: Context?,
    title: String,
    private val isGrid: Boolean = false,
) : GenericRecyclerViewAdapter(title) {

    private val nativeAdLoader: MaxNativeAdLoader = MaxNativeAdLoader("a416d5d67e65ddcd", context)
    private val nativeAdMap = mutableMapOf<Int, MaxAd>()

    init {
        val settingsEncoded = TeadsMediationSettings.Builder()
            .enableDebug()
            .setUsPrivacy("1YNN")
            .build().toJsonEncoded()

        nativeAdLoader.setLocalExtraParameter("teadsSettings", settingsEncoded)
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
                    if (isGrid) R.layout.item_applovin_native_ad_placeholder_grid else R.layout.item_applovin_native_ad_placeholder,
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
                nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {
                    override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
                        nativeAdMap[position]?.let { nativeAdLoader.destroy(it) }
                        nativeAdMap[position] = ad

                        (holder.itemView as ViewGroup).apply {
                            removeAllViews()
                            addView(nativeAdView)
                        }
                    }

                    override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
                        Log.e("onNativeAdLoadFailed", error.message)
                    }

                    override fun onNativeAdClicked(ad: MaxAd) {}
                })

                nativeAdLoader.loadAd(createNativeAdView())
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

    private fun createNativeAdView(): MaxNativeAdView {
        val binder: MaxNativeAdViewBinder = MaxNativeAdViewBinder
            .Builder(if (isGrid) R.layout.item_applovin_native_ad_grid else R.layout.item_applovin_native_ad)
            .setTitleTextViewId(R.id.ad_title)
            .setBodyTextViewId(R.id.ad_body)
            .setMediaContentViewGroupId(R.id.media_view_container)
            .setOptionsContentViewGroupId(R.id.ad_options_view)
            .build()
        return MaxNativeAdView(binder, context)
    }

    override fun getItemCount(): Int = feedItems.size + 1
}
