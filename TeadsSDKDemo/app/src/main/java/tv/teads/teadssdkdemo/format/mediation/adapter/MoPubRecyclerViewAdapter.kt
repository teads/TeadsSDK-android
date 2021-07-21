package tv.teads.teadssdkdemo.format.mediation.adapter

import android.content.Context
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.mopub.mobileads.MoPubView
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.sdk.utils.userConsent.TCFVersion
import tv.teads.teadssdkdemo.component.GenericRecyclerViewAdapter
import tv.teads.teadssdkdemo.data.RecyclerItemType

/**
 * Manage a repeatable ad for a Recycler view with the MoPub mediation,
 * It will display the same ad view every [MoPubRecyclerViewAdapter.AD_INTERVAL] items
 */
class MoPubRecyclerViewAdapter internal constructor(moPubId: String, context: Context?, title: String)
    : GenericRecyclerViewAdapter(title) {

    private val mMoPubView: MoPubView = MoPubView(context)
    private val mListener: TeadsAdapterListener

    init {
        // 1. Initialize Teads Helper
        TeadsHelper.initialize()

        // 2. Setup the MoPub view
        mMoPubView.setAdUnitId(moPubId)
        // Don't forget to put autorefreshEnabled to false
        mMoPubView.autorefreshEnabled = false

        /* 3. Create a TeadsBannerAdapterListener
        You need to create an instance for each instance of AdMob view
        it needs to be a strong reference to it, so our helper can cleanup when you don't need it anymore
         */
        mListener = object : TeadsAdapterListener {
            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                mMoPubView.addView(trackerView)
            }

            override fun onRatioUpdated(adRatio: AdRatio) {
                mMoPubView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        mMoPubView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        val params: ViewGroup.LayoutParams = mMoPubView.layoutParams

                        // Here the width is MATCH_PARENT
                        params.height = adRatio.calculateHeight(mMoPubView.measuredWidth)

                        mMoPubView.layoutParams = params
                    }
                })
            }
        }

        // 5. Attach the listener to the helper and save the key
        val key = TeadsHelper.attachListener(mListener)

        // 6. Create the AdSettings to customize our Teads AdView
        val extras = TeadsMediationSettings.Builder()
                .enableDebug()
                .userConsent("1", "11001", TCFVersion.V1, 12)
                .setMediationListenerKey(key)
                .pageSlotUrl("https://page.com/article1/")
                .build()

        // 6. Add the AdSettings to MoPub view
        mMoPubView.setLocalExtras(mapOf("teads" to extras.toJsonEncoded()))

        // 8. Load the ad
        mMoPubView.loadAd()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecyclerItemType.TYPE_TEADS.value -> ViewHolderDemo(mMoPubView)
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }
}