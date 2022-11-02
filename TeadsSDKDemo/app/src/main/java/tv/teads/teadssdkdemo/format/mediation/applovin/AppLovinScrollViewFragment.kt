package tv.teads.teadssdkdemo.format.mediation.applovin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import kotlinx.android.synthetic.main.fragment_inread_scrollview.*
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.sdk.utils.userConsent.TCFVersion
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.mediation.identifier.AppLovinIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * Display inRead as Banner within a ScrollView using AppLovin Mediation.
 */
class AppLovinScrollViewFragment : BaseFragment() {
    private lateinit var mListener: TeadsAdapterListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_inread_scrollview, container, false)

        v.findViewById<TextView>(R.id.integration_header).text = getTitle()

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1. Initialize Teads Helper (Don't forget to initialize AppLovin aswell see SplashScreen)
        TeadsHelper.initialize()

        // 2. Create MaxAdView view and add it to view hierarchy
        val adView = MaxAdView(AppLovinIdentifier.getAdUnitFromPid(pid), MaxAdFormat.MREC, context)
        adSlotView.addView(adView, 0)

        // 3. Attach listener (will include Teads events)
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

        /* 3. Create a TeadsBannerAdapterListener
        You need to create an instance for each instance of MaxAdView view
        it needs to be a strong reference to it, so our helper can cleanup when you don't need it anymore
         */
        mListener = object : TeadsAdapterListener {
            override fun onRatioUpdated(adRatio: AdRatio) {
                val params: ViewGroup.LayoutParams = adView.layoutParams

                // Here the width is MATCH_PARENT
                params.height = adRatio.calculateHeight(adView.measuredWidth)

                adView.layoutParams = params
            }

            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                adSlotView.addView(trackerView)
            }

        }

        // 4. Attach the listener to the helper and save the key
        val key = TeadsHelper.attachListener(mListener)

        // 5. Create the AdSettings to customize our Teads AdView
        val settingsEncoded = TeadsMediationSettings.Builder()
            .enableDebug()
            // Needed by european regulation
            // See https://mobile.teads.tv/sdk/documentation/android/gdpr-consent
            .userConsent("1", "0001", TCFVersion.V1, 12)
            // The article url if you are a news publisher to increase your earnings
            .pageSlotUrl("https://page.com/article1/")
            // /!\ You need to add the key to the settings
            .setMediationListenerKey(key)
            .build()
            .toJsonEncoded()

        // 6. Add the settings encoded to the adView using this key
        adView.setLocalExtraParameter("teadsSettings", settingsEncoded)


        // 7. Load the ad after the AppLovin sdk initialized
        adView.loadAd()
    }

    override fun getTitle(): String = "InRead AppLovin ScrollView"
}
