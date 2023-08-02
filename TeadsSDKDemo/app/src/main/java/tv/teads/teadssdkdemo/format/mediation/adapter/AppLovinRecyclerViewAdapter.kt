package tv.teads.teadssdkdemo.format.mediation.adapter

import android.content.Context
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.sdk.utils.userConsent.TCFVersion
import tv.teads.teadssdkdemo.component.GenericRecyclerViewAdapter
import tv.teads.teadssdkdemo.data.RecyclerItemType

/**
 * Simple RecyclerView adapter
 */
class AppLovinRecyclerViewAdapter(appLovinUnitId: String, context: Context?, title: String)
    : GenericRecyclerViewAdapter(title) {

    private val adView: MaxAdView = MaxAdView(appLovinUnitId, MaxAdFormat.MREC, context!!)
    private val mListener: TeadsAdapterListener

    init {
        // 1. Initialize Teads Helper (Don't forget to initialize AppLovin aswell see SplashScreen)
        TeadsHelper.initialize()

        // 2. Subsribe to the listener if needed
        adView.setListener(object : MaxAdViewAdListener {
            override fun onAdLoaded(ad: MaxAd?) {}

            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                Toast.makeText(context, "Ad loading failed: onAdFailedToLoad(${error?.message})", Toast.LENGTH_SHORT).show()
            }

            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                Toast.makeText(context, "Ad display failed: onAdFailedToLoad(${error?.message})", Toast.LENGTH_SHORT).show()
            }

            override fun onAdExpanded(ad: MaxAd?) {}
            override fun onAdCollapsed(ad: MaxAd?) {}
            override fun onAdDisplayed(ad: MaxAd?) {}
            override fun onAdHidden(ad: MaxAd?) {}
            override fun onAdClicked(ad: MaxAd?) {}
        })

        /* 3. Create a TeadsAdapterListener
        You need to create an instance for each instance of MaxAdView view
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

        // 4. Attach the listener to the helper and save the key
        val key = TeadsHelper.attachListener(mListener)

        // 5. Create the TeadsMediationSettings to customize our Teads AdView
        val settingsEncoded = TeadsMediationSettings.Builder()
            .enableDebug()
            // Needed by european regulation
            // See https://mobile.teads.tv/sdk/documentation/android/gdpr-consent
            .userConsent("1", "0001", TCFVersion.V2, 12)
            // The article url if you are a news publisher to increase your earnings
            .pageSlotUrl("https://page.com/article1/")
            // /!\ You need to add the key to the settings
            .setMediationListenerKey(key)
            .build()
            .toJsonEncoded()

        // 6. Add the settings encoded to the adView using this key
        adView.setLocalExtraParameter("teadsSettings", settingsEncoded)

        // 7. Load the ad with the AdRequest
        adView.loadAd()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecyclerItemType.TYPE_TEADS.value -> ViewHolderDemo(adView)
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }
}
