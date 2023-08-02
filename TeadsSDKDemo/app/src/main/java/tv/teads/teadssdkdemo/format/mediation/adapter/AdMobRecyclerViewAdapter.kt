package tv.teads.teadssdkdemo.format.mediation.adapter

import android.content.Context
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*
import tv.teads.adapter.admob.TeadsAdapter
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.sdk.utils.userConsent.TCFVersion
import tv.teads.teadssdkdemo.component.GenericRecyclerViewAdapter
import tv.teads.teadssdkdemo.data.RecyclerItemType
import tv.teads.teadssdkdemo.data.SessionDataSource

/**
 * Simple RecyclerView adapter
 */
class AdMobRecyclerViewAdapter(admobBannerId: String, context: Context?, title: String)
    : GenericRecyclerViewAdapter(title) {

    private val adView: AdView = AdView(context!!)
    private val mListener: TeadsAdapterListener

    init {
        // 1. Initialize AdMob & Teads Helper
        MobileAds.initialize(context!!)
        TeadsHelper.initialize()

        // 2. Setup the AdMob view
        adView.adUnitId = admobBannerId
        adView.adSize = AdSize.MEDIUM_RECTANGLE

        // 3. Subsribe to the listener if needed
        adView.adListener = object : AdListener() {

            override fun onAdFailedToLoad(error: LoadAdError) {
                Toast.makeText(context, "Ad loading failed: onAdFailedToLoad(${error.cause?.message})", Toast.LENGTH_SHORT).show()
            }

            override fun onAdLoaded() {
            }

            override fun onAdOpened() {
                // Nothing to do for Teads
            }

            override fun onAdClosed() {
                // Nothing to do for Teads
            }
        }

        /* 4. Create a TeadsAdapterListener
        You need to create an instance for each instance of AdMob view
        it needs to be a strong reference to it, so our helper can cleanup when you don't need it anymore
         */
        mListener = object : TeadsAdapterListener {
            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                adView.addView(trackerView)
            }

            override fun onRatioUpdated(adRatio: AdRatio) {
                adView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        adView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        val params: ViewGroup.LayoutParams = adView.layoutParams

                        // Here the width is MATCH_PARENT
                        params.height = adRatio.calculateHeight(adView.measuredWidth)

                        adView.layoutParams = params
                    }
                })
            }
        }

        // 5. Attach the listener to the helper and save the key
        val key = TeadsHelper.attachListener(mListener)

        // 6. Create the TeadsMediationSettings to customize our Teads AdView
        val extras = TeadsMediationSettings.Builder()
                // Needed by european regulation
                // See https://mobile.teads.tv/sdk/documentation/android/gdpr-consent
                .userConsent("1", SessionDataSource.FAKE_GDPR_STR, TCFVersion.V2, 12)
                // The article url if you are a news publisher to increase your earnings
                .pageSlotUrl("https://page.com/article1/")
                // /!\ You need to add the key to the settings
                .setMediationListenerKey(key)
                .build()

        // 7. Create the AdRequest with the previous settings
        val adRequest = AdRequest.Builder()
                .addNetworkExtrasBundle(TeadsAdapter::class.java, extras.toBundle())
                .build()

        // 8. Load the ad with the AdRequest
        adView.loadAd(adRequest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecyclerItemType.TYPE_TEADS.value -> ViewHolderDemo(adView)
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }
}
