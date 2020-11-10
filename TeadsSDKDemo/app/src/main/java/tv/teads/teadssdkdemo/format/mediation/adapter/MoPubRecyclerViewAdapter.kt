package tv.teads.teadssdkdemo.format.mediation.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import com.mopub.mobileads.MoPubView
import tv.teads.helper.TeadsBannerAdapterListener
import tv.teads.helper.TeadsHelper
import tv.teads.sdk.android.AdSettings
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.component.GenericRecyclerViewAdapter
import tv.teads.teadssdkdemo.data.RecyclerItemType
import kotlin.math.roundToInt

/**
 * Manage a repeatable ad for a Recycler view with the MoPub mediation,
 * It will display the same ad view every [MoPubRecyclerViewAdapter.AD_INTERVAL] items
 */
class MoPubRecyclerViewAdapter internal constructor(moPubId: String, context: Context?, title: String)
    : GenericRecyclerViewAdapter(title) {

    private val mMoPubView: MoPubView = MoPubView(context)
    private val mListener: TeadsBannerAdapterListener

    init {
        // 1. Initialize Teads Helper
        TeadsHelper.initialize()

        // 2. Setup the MoPub view
        mMoPubView.adUnitId = moPubId
        // Don't forget to put autorefreshEnabled to false
        mMoPubView.autorefreshEnabled = false

        /* 3. Create a TeadsBannerAdapterListener
        You need to create an instance for each instance of AdMob view
        it needs to be a strong reference to it, so our helper can cleanup when you don't need it anymore
         */
        mListener = object : TeadsBannerAdapterListener {
            override fun onRatioUpdated(adRatio: Float) {
                mMoPubView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        mMoPubView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        val params: ViewGroup.LayoutParams = mMoPubView.layoutParams

                        // Here the width is MATCH_PARENT
                        params.height = (mMoPubView.measuredWidth / adRatio).roundToInt()

                        mMoPubView.layoutParams = params
                    }
                })
            }
        }

        // 5. Attach the listener to the helper and save the key
        val key = TeadsHelper.attachListener(mListener)

        // 6. Create the AdSettings to customize our Teads AdView
        val extras = AdSettings.Builder()
                .enableDebug()
                .userConsent("1", "11001")
                .addAdapterListener(key)
                .pageUrl("https://page.com/article1/")
                .build()

        // 6. Add the AdSettings to MoPub view
        mMoPubView.localExtras = extras.toHashMap()

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