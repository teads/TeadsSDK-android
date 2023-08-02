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
import com.applovin.mediation.nativeAds.*
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.utils.userConsent.TCFVersion
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
    private val nativeAdMap = mutableMapOf<Int, MaxNativeAdView?>()

    init {
        val settingsEncoded = TeadsMediationSettings.Builder()
            .enableDebug()
            .setUsPrivacy("1YNN")
            .userConsent(
                "1",
                "CPXVK9cPXVK9cAtABBFRCKCsAP_AAH_AAAAAIqtd_X__bX9j-_5_fft0eY1P9_r3_-QzjhfNt-8F3L_W_L0X42E7NF36pq4KuR4Eu3LBIQNlHMHUTUmwaokVrzHsak2cpyNKJ7LEknMZO2dYGH9Pn9lDuYKY7_5___bx3j-v_t_-39T378Xf3_d5_2---vCfV599jbv9f3__39nP___9v-_8_______gimASYal5AF2JY4Mm0aRQogRhWEhVAoAKKAYWiKwAcHBTsrAJ9QQsAEAqAjAiBBiCjBgEAAgEASERASAFggEQBEAgABAAiAQgAImAQWAFgYBAAKAaFiAFAAIEhBkUERymBARIlFBLZWIJQV7GmEAZZYAUCiMioAESAAAkDISFg5jgCQEuFkgSYoXyAEYAAAAA.YAAAAAAAAAAA",
                TCFVersion.V2,
                12
            )
            .build()
            .toJsonEncoded()

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
                val maxNativeAdView = nativeAdMap[holder.adapterPosition]

                if (maxNativeAdView != null) appendAdToParent(maxNativeAdView, holder.itemView as ViewGroup)
                else {
                    nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {
                        override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
                            nativeAdMap[holder.adapterPosition] = nativeAdView

                            appendAdToParent(nativeAdView, holder.itemView as ViewGroup)
                        }

                        override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
                            Log.e("onNativeAdLoadFailed", error.message)
                        }

                        override fun onNativeAdClicked(ad: MaxAd) {}
                    })
                    nativeAdLoader.loadAd(createNativeAdView())
                }
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

    private fun appendAdToParent(adView: MaxNativeAdView?, parentView: ViewGroup) {
        parentView.apply {
            removeAllViews()
            addView(adView)
        }
    }

    private fun createNativeAdView(): MaxNativeAdView {
        val binder: MaxNativeAdViewBinder = MaxNativeAdViewBinder
            .Builder(if (isGrid) R.layout.item_applovin_native_ad_grid else R.layout.item_applovin_native_ad)
            .setTitleTextViewId(R.id.ad_title)
            .setBodyTextViewId(R.id.ad_body)
            .setMediaContentViewGroupId(R.id.teads_mediaview)
            .setOptionsContentViewGroupId(R.id.ad_options_view)
            .build()
        return MaxNativeAdView(binder, context)
    }

    override fun getItemCount(): Int = feedItems.size + 1
}
