package tv.teads.teadssdkdemo.format.inread.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import tv.teads.sdk.*
import tv.teads.sdk.renderer.InReadAdView
import tv.teads.teadssdkdemo.component.GenericRecyclerViewAdapter
import tv.teads.teadssdkdemo.data.RecyclerItemType

/**
 * Simple RecyclerView adapter
 */
class SimpleRecyclerViewAdapter(private val context: Context?, pid: Int, title: String)
    : GenericRecyclerViewAdapter(title) {

    private var requestSettings: AdRequestSettings
    private val adPlacement: InReadAdPlacement

    init {
        // 1. Setup the settings
        val placementSettings = AdPlacementSettings.Builder()
                .enableDebug()
                .build()
        requestSettings = AdRequestSettings.Builder()
                .pageSlotUrl("http://teads.com")
                .build()

        // 2. Create the InReadAdPlacement
        adPlacement = TeadsSDK.createInReadPlacement(context!!, pid, placementSettings)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecyclerItemType.TYPE_TEADS.value -> ViewHolderDemo(FrameLayout(context!!))
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            RecyclerItemType.TYPE_TEADS.value -> {
                val adView = holder.itemView as FrameLayout
                // 3. Request the ad and register to the listener in it
                adPlacement.requestAd(requestSettings, object : InReadAdListener {
                    override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                        adView.addView(trackerView)
                    }

                    override fun onAdReceived(inReadAdView: InReadAdView, adRatio: AdRatio) {
                        adView.addView(inReadAdView, 0)
                    }

                    override fun onAdClicked() {}
                    override fun onAdClosed() {}
                    override fun onAdError(code: Int, description: String) {}
                    override fun onAdImpression() {}
                    override fun onAdExpandedToFullscreen() {}
                    override fun onAdCollapsedFromFullscreen() {}
                    override fun onAdRatioUpdate(adRatio: AdRatio) {}

                    override fun onFailToReceiveAd(failReason: String) {}
                })
            }
            else -> super.onBindViewHolder(holder, position)
        }
    }
}
