package tv.teads.teadssdkdemo.format.mediation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import tv.teads.adapter.admob.nativead.TeadsNativeAdapter
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.component.GenericRecyclerViewAdapter
import tv.teads.teadssdkdemo.data.FeedItem.Companion.feedItems
import tv.teads.teadssdkdemo.data.RecyclerItemType

/**
 * Native RecyclerView adapter
 */
class AdMobNativeRecyclerViewAdapter(
    private val context: Context?,
    title: String,
    private val isGrid: Boolean = false,
) : GenericRecyclerViewAdapter(title) {

    init {
        TeadsHelper.initialize()
        MobileAds.initialize(context!!)
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
                    if (isGrid) R.layout.item_admob_native_ad_grid else R.layout.item_admob_native_ad,
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
                val adLoader = AdLoader.Builder(context!!, ADMOB_TEADS_TEST_ID)
                    .forNativeAd { ad: NativeAd ->
                        with(holder.itemView as NativeAdView) {
                            mediaView = findViewById(R.id.ad_mob_media)
                            headlineView = findViewById(R.id.ad_mob_headline)
                            bodyView = findViewById(R.id.ad_mob_body)
                            callToActionView = findViewById(R.id.sponsor_more)

                            // other assets could be used, in order to be aware of it,
                            // see the docs https://developers.google.com/admob/android/native/start
                            headlineView.setText(ad.headline)
                            bodyView.setText(ad.body)

                            setNativeAd(ad)
                        }
                    }
                    .withAdListener(object : AdListener() {
                        override fun onAdFailedToLoad(error: LoadAdError) {
                            Log.e("adLoader error", error.message)
                        }
                    })
                    .withNativeAdOptions(NativeAdOptions.Builder().build())
                    .build()

                val listener = object : TeadsAdapterListener {
                    override fun onRatioUpdated(adRatio: AdRatio) {}

                    override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                        (holder.itemView as NativeAdView).addView(trackerView)
                    }
                }

                val settings = TeadsMediationSettings.Builder()
                    .enableDebug()
                    .setMediationListenerKey(TeadsHelper.attachListener(listener))
                    .build()

                val adRequest = AdRequest.Builder()
                    .addCustomEventExtrasBundle(TeadsNativeAdapter::class.java, settings.toBundle())
                    .build()

                adLoader.loadAd(adRequest)
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

    private fun View?.setText(text: String?) = this.let { it as? TextView }?.also { it.text = text }

    companion object {
        private const val ADMOB_GOOGLE_TEST_ID = "ca-app-pub-3940256099942544/2247696110"
        private const val ADMOB_TEADS_TEST_ID = "ca-app-pub-3068786746829754/9820813147"
    }
}
