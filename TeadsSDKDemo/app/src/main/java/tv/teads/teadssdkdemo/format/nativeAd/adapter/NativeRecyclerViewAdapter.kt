package tv.teads.teadssdkdemo.format.nativeAd.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tv.teads.sdk.*
import tv.teads.sdk.renderer.NativeAdView
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.component.GenericRecyclerViewAdapter
import tv.teads.teadssdkdemo.data.FeedItem
import tv.teads.teadssdkdemo.data.RecyclerItemType

/**
 * Native RecyclerView adapter
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
            0 -> if (isGrid) RecyclerItemType.TYPE_FAKE_FEED.value else RecyclerItemType.TYPE_SCROLL_DOWN.value
            4 -> RecyclerItemType.TYPE_TEADS.value
            else -> RecyclerItemType.TYPE_FAKE_FEED.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecyclerItemType.TYPE_TEADS.value -> {
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
            RecyclerItemType.TYPE_TEADS.value -> {
                val nativeAdView = holder.itemView.findViewById<NativeAdView>(R.id.nativeAdView)

                adPlacement.requestAd(requestSettings, object : NativeAdListener {
                    override fun onAdReceived(nativeAd: NativeAd) {
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

    private val feedItems = listOf(
        FeedItem(
            title = "'Six-Word Sci-Fi: Stories Written by You'",
            body =  "'Here's this month's prompt, how to submit, and an illustrated archive of past favorites.'",
            source = "@wired",
            time = "3min",
            image = R.drawable.feed_wired
        ),
        FeedItem(
            title = "'Finally, a Down-to-Earth Space Odyssey'",
            body =  "'Rebecca Scherm’s A House Between Earth and the Moon is set amongst the stars, but is psychologically grounded.'",
            source = "@kate_knibbs",
            time = "3min",
            image = R.drawable.feed_knibbs
        ),
        FeedItem(
            title = "'Ghostwire: Tokyo Brings Japanese Folklore to the Masses'",
            body = "The new action-thriller from Tango Gameworks uncovers universal themes by embracing cultural specificity.",
            source = "@reid_mccarter",
            time = "5min",
            image = R.drawable.feed_mccater
        ),
        FeedItem(
            title = "'Even the Pandemic Couldn’t Stop Button Soccer'",
            body = "Traditionally played with buttons and bottle caps, this Brazilian game, usually played offline, exploded on the internet when people were forced inside",
            source = "@garcia",
            time = "6min",
            image = R.drawable.feed_garcia
        ),
        FeedItem(
            title = "'Not Everyone Wants NFTs to Be the Future of Gaming'",
            body = "Tim Morten, cofounder of Frost Giant Studios and formerly production director on StarCraft II, doesn't see the same dollar signs some studios do.",
            source = "@matthew_smith",
            time = "11min",
            image = R.drawable.feed_smith
        ),
        FeedItem(
            title = "'Elden Ring Isn’t Made for All Gamers. I Wish It Were'",
            body = "The celebrated game has no difficulty modes, guaranteeing it will alienate some players.",
            source = "@krishna",
            time = "12min",
            image = R.drawable.feed_knibbs
        ),
        FeedItem(
            title = "'TV Struggles to Put Silicon Valley on the Screen'",
            body = "From WeCrashed to Super Pumped, Hollywood is still too infatuated with founders.",
            source = "@pardes",
            time = "13min",
            image = R.drawable.feed_pardes
        ),
        FeedItem(
            title = "'Why The Andy Warhol Diaries Recreated the Artist’s Voice With AI'",
            body = "The filmmakers had under four minutes of audio to work with. And yes, they considered the ethical concerns.",
            source = "@watercutter",
            time = "17min",
            image = R.drawable.feed_watercutter
        ),
        FeedItem(
            title = "'After Yang Will Make You Grieve For a Robot'",
            body = "The beautiful and strange new movie from South Korean filmmaker Kogonada presents a very different view of an AI-enhanced future.",
            source = "@knight",
            time = "18min",
            image = R.drawable.feed_knigth
        )
    )
}
