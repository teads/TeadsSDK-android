package tv.teads.teadssdkdemo.format.mediation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartadserver.android.library.model.SASAdPlacement
import com.smartadserver.android.library.model.SASNativeAdElement
import com.smartadserver.android.library.model.SASNativeAdManager
import com.smartadserver.android.library.util.SASConfiguration.getSharedInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tv.teads.adapter.smart.nativead.TeadsSmartViewBinder
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.renderer.AdScale
import tv.teads.sdk.utils.userConsent.TCFVersion
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.component.GenericRecyclerViewAdapter
import tv.teads.teadssdkdemo.data.FeedItem.Companion.feedItems
import tv.teads.teadssdkdemo.data.RecyclerItemType
import tv.teads.teadssdkdemo.data.SessionDataSource

/**
 * Smart Native RecyclerView adapter
 */
class SmartNativeRecyclerViewAdapter(
    private val context: Context?,
    title: String,
    private val isGrid: Boolean = false,
) : GenericRecyclerViewAdapter(title) {

    private val siteID = 385317
    private val pageName = "1399205"
    private val formatID = 102803

    private val nativeAdMap = mutableMapOf<Int, SASNativeAdElement>()
    private val nativeAdManager: SASNativeAdManager

    init {
        getSharedInstance().configure(context!!, siteID)

        // Enable output to Android Logcat (optional)
        getSharedInstance().isLoggingEnabled = true

        val settingsEncoded = TeadsMediationSettings.Builder()
            .enableDebug()
            .setUsPrivacy("1YNN")
            .userConsent(
                "1",
                SessionDataSource.FAKE_GDPR_STR,
                TCFVersion.V2,
                12
            )
            .build()
            .toJsonEncoded()

        val adPlacement =
            SASAdPlacement(siteID.toLong(), pageName, formatID.toLong(), "teadsAdSettingsKey=${settingsEncoded}", "")
        nativeAdManager = SASNativeAdManager(context!!, adPlacement)
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
                val sasNativeAdElement = nativeAdMap[holder.adapterPosition]

                if (sasNativeAdElement != null) appendAdToParent(sasNativeAdElement, holder.itemView as ViewGroup)
                else {
                    nativeAdManager.nativeAdListener = object : SASNativeAdManager.NativeAdListener {
                        override fun onNativeAdLoaded(ad: SASNativeAdElement) {
                            CoroutineScope(Dispatchers.Main).launch {
                                nativeAdMap[position] = ad

                                appendAdToParent(ad, holder.itemView as ViewGroup)
                            }
                        }

                        override fun onNativeAdFailedToLoad(e: Exception) {
                            // the native ad loading failed
                        }
                    }
                    nativeAdManager.loadNativeAd()
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

    private fun appendAdToParent(ad: SASNativeAdElement, parentView: ViewGroup) {
        parentView.apply {
            removeAllViews()
            addView(createMediaView(ad, if (isGrid) R.layout.item_smart_native_ad_grid else R.layout.item_smart_native_ad))
        }
    }

    private fun createMediaView(ad: SASNativeAdElement, layout: Int): View =
        TeadsSmartViewBinder(context!!, layout, ad)
            .title(R.id.ad_title)
            .body(R.id.ad_body)
            .iconImage(R.id.teads_icon)
            .callToAction(R.id.teads_cta)
            .mediaLayout(R.id.teads_mediaview)
            .adChoice(R.id.ad_choice)
            .bind()

    override fun getItemCount(): Int = feedItems.size + 1
}
