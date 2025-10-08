package tv.teads.teadssdkdemo.v6.ui.xml

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.sdk.AppLovinSdk
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.inread.extensions.resizeAdContainer
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration

class MediaAppLovinScrollViewFragment : Fragment() {

    private lateinit var maxAdView: MaxAdView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.article_scroll_view_template_middle_ad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupContent()
    }

    private fun setupContent() {
        val adContainer = requireView().findViewById<ViewGroup>(R.id.ad_container)

        // 1. Initialize Teads Helper and AppLovin SDK (can be init once on the start of the app)
        AppLovinSdk.getInstance(requireContext().applicationContext).mediationProvider = "max"
        AppLovinSdk.getInstance(requireContext()).initializeSdk { }
        TeadsHelper.initialize()

        // 2. Create MaxAdView view and add it to view hierarchy
        maxAdView = MaxAdView(DemoSessionConfiguration.getPlacementIdOrDefault(), MaxAdFormat.MREC, requireContext())
        adContainer.addView(maxAdView)

        // 3. Listen to lifecycle events
        maxAdView.setListener(object : MaxAdViewAdListener {
            override fun onAdLoaded(ad: MaxAd?) {
                Log.d("MediaAppLovinScrollViewFragment", "onAdLoaded")
            }

            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                Log.e("MediaAppLovinScrollViewFragment", "onAdLoadFailed: ${error?.message}")
            }

            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                Log.e("MediaAppLovinScrollViewFragment", "onAdDisplayFailed: ${error?.message}")
            }

            override fun onAdExpanded(ad: MaxAd?) {
                Log.d("MediaAppLovinScrollViewFragment", "onAdExpanded")
            }

            override fun onAdCollapsed(ad: MaxAd?) {
                Log.d("MediaAppLovinScrollViewFragment", "onAdCollapsed")
            }

            override fun onAdDisplayed(ad: MaxAd?) {
                Log.d("MediaAppLovinScrollViewFragment", "onAdDisplayed")
            }

            override fun onAdHidden(ad: MaxAd?) {
                Log.d("MediaAppLovinScrollViewFragment", "onAdHidden")
            }

            override fun onAdClicked(ad: MaxAd?) {
                Log.d("MediaAppLovinScrollViewFragment", "onAdClicked")
            }
        })

        // 4. Create a TeadsAdapterListener
        // You need to create an instance for each instance of MaxAdView view
        // It needs to be a strong reference to it, so our helper can clean up when you don't need it anymore
        val teadsListener = object : TeadsAdapterListener {
            override fun onRatioUpdated(adRatio: AdRatio) {
                adContainer.resizeAdContainer(adRatio)
            }

            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                adContainer.addView(trackerView)
            }
        }

        // 5. Attach the listener to TeadsHelper and save the key to add on TeadsMediationSettings
        val key = TeadsHelper.attachListener(teadsListener)

        // 6. Create the mediation settings to customize our Teads AdView
        val settingsEncoded = TeadsMediationSettings.Builder()
            .enableDebug() // Enable more logging visibility
            .pageSlotUrl(DemoSessionConfiguration.getArticleUrlOrDefault()) // Your article url
            .setMediationListenerKey(key)
            .build()
            .toJsonEncoded()

        // 7. Add the settings encoded to the adView using this key
        maxAdView.setLocalExtraParameter("teadsSettings", settingsEncoded)

        // 8. Disable ad auto refresh, if you don't disable it, use it carefully since
        // if you receive ads that are not displayed too often it will reduce your fill rate
        maxAdView.setExtraParameter("allow_pause_auto_refresh_immediately", "true")
        maxAdView.stopAutoRefresh()

        // 9. Load the ad
        maxAdView.loadAd()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 10. Clean from memory
        if (::maxAdView.isInitialized) {
            maxAdView.destroy()
        }
    }
}
