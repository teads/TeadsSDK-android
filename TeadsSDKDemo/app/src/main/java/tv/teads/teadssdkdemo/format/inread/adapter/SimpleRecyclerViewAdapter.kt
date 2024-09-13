package tv.teads.teadssdkdemo.format.inread.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import tv.teads.sdk.*
import tv.teads.sdk.renderer.InReadAdView
import tv.teads.teadssdkdemo.component.GenericRecyclerViewAdapter
import tv.teads.teadssdkdemo.data.RecyclerItemType
import tv.teads.teadssdkdemo.format.inread.extensions.resizeAdContainer

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
                val adSlotContainer = holder.itemView as FrameLayout
                adSlotContainer.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                var inReadAdView: InReadAdView? = null

                // 3. Request the ad and register to the listener in it
                adPlacement.requestAd(requestSettings, object : InReadAdViewListener {
                    override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                        adSlotContainer.addView(trackerView)
                    }

                    override fun onAdReceived(ad: InReadAdView, adRatio: AdRatio) {
                        // Clean and init inReadAdView
                        inReadAdView = ad
                        // Add ad to the container and resize
                        adSlotContainer.resizeAdContainer(adRatio)
                        adSlotContainer.addView(ad, 0)
                    }

                    override fun onAdRatioUpdate(adRatio: AdRatio) {
                        // Resize
                        adSlotContainer.resizeAdContainer(adRatio)
                    }

                    override fun onAdClicked() {}
                    override fun onAdClosed() {}
                    override fun onAdError(code: Int, description: String) {}
                    override fun onAdImpression() {}
                    override fun onAdExpandedToFullscreen() {}
                    override fun onAdCollapsedFromFullscreen() {}
                    override fun onFailToReceiveAd(failReason: String) { }
                })
            }
            else -> super.onBindViewHolder(holder, position)
        }
    }
}
