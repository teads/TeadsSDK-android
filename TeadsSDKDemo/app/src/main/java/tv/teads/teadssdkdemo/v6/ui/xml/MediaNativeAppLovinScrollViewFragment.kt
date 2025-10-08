package tv.teads.teadssdkdemo.v6.ui.xml

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.applovin.sdk.AppLovinSdk
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration

class MediaNativeAppLovinScrollViewFragment : Fragment() {

    private lateinit var nativeAdLoader: MaxNativeAdLoader

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

        // 2. Create native ad view binder
        val binder: MaxNativeAdViewBinder = MaxNativeAdViewBinder
            .Builder(R.layout.applovin_native_ad_view)
            .setTitleTextViewId(R.id.ad_title)
            .setBodyTextViewId(R.id.ad_body)
            .setMediaContentViewGroupId(R.id.teads_mediaview)
            .setOptionsContentViewGroupId(R.id.ad_options_view)
            .build()

        // 3. Create your AppLovin Native AdView and add it to container
        val maxNativeAdView = MaxNativeAdView(binder, requireContext())
        adContainer.addView(maxNativeAdView)

        // 4. Create MaxNativeAdLoader for native ads
        nativeAdLoader = MaxNativeAdLoader(DemoSessionConfiguration.getPlacementIdOrDefault(), requireContext())

        // 5. Create TeadsAdapterListener
        val teadsListener = object : TeadsAdapterListener {
            override fun onRatioUpdated(adRatio: AdRatio) {
                // For native ads, ratio updates are typically handled by the ad itself
                Log.d("MediaNativeAppLovinScrollViewFragment", "onRatioUpdated: $adRatio")
            }

            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                maxNativeAdView.addView(trackerView)
            }
        }

        // 6. Attach the listener to TeadsHelper and save the key to add on TeadsMediationSettings
        val key = TeadsHelper.attachListener(teadsListener)

        // 7. Create the mediation settings
        val settingsEncoded = TeadsMediationSettings.Builder()
            .enableDebug() // Enable more logging visibility
            .pageSlotUrl(DemoSessionConfiguration.getArticleUrlOrDefault()) // Your article url
            .setMediationListenerKey(key)
            .build()
            .toJsonEncoded()

        // 8. Add the settings encoded to the nativeAdLoader using this key
        nativeAdLoader.setLocalExtraParameter("teadsSettings", settingsEncoded)

        // 9. Set native ad listener
        nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {
            override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
                Log.d("MediaNativeAppLovinScrollViewFragment", "onNativeAdLoaded")
            }

            override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
                Log.e("MediaNativeAppLovinScrollViewFragment", "onNativeAdLoadFailed: ${error.message}")
            }

            override fun onNativeAdClicked(ad: MaxAd) {
                Log.d("MediaNativeAppLovinScrollViewFragment", "onNativeAdClicked")
            }
        })

        // 10. Load the native ad
        nativeAdLoader.loadAd(maxNativeAdView)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 11. Clean from memory
        if (::nativeAdLoader.isInitialized) {
            nativeAdLoader.destroy()
        }
    }
}
