package tv.teads.teadssdkdemo.v6.ui.xml

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import tv.teads.adapter.admob.TeadsAdapter
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.inread.extensions.resizeAdContainer
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration

class MediaScrollViewAdMobFragment : Fragment() {

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

        // 1. Initialize AdMob and Teads Helper
        MobileAds.initialize(requireContext())
        TeadsHelper.initialize()

        // For testing purposes - using a test device configuration
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("BAC58D23C8C5265E2C8A56FE7FBAE2C1"))
                .build()
        )

        // 2. Create AdMob view, set up and add it to view hierarchy
        val adView = AdView(requireContext())
        adView.adUnitId = DemoSessionConfiguration.getPlacementIdOrDefault() // Your unique placement id

        // Setting the ad size as MEDIUM_RECTANGLE is mandatory but somewhat of a workaround
        // Your ad container view needs to have height as wrap_content to satisfy different ad formats that Media provides
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE)
        adContainer.addView(adView)

        // 3. Listen to lifecycle events
        adView.adListener = object : AdListener() {
            override fun onAdImpression() {
                Log.d("MediaScrollViewAdMobFragment", "onAdImpression")
            }

            override fun onAdClicked() {
                Log.d("MediaScrollViewAdMobFragment", "onAdClicked")
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.e("MediaScrollViewAdMobFragment", "onAdFailedToLoad: ${error.cause?.message}")
            }

            override fun onAdLoaded() {
                Log.d("MediaScrollViewAdMobFragment", "onAdLoaded")
            }

            override fun onAdOpened() {
                Log.d("MediaScrollViewAdMobFragment", "onAdOpened")
            }

            override fun onAdClosed() {
                Log.d("MediaScrollViewAdMobFragment", "onAdClosed")
            }
        }

        // 4. Create a TeadsAdapterListener, you need to create an instance for each AdMob view
        // It needs to be a strong reference so our helper can clean up when you don't need it anymore.
        val mListener = object : TeadsAdapterListener {
            override fun onRatioUpdated(adRatio: AdRatio) {
                adContainer.resizeAdContainer(adRatio)
            }

            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                adContainer.addView(trackerView)
            }
        }

        // 5. Attach the listener to TeadsHelper and save the key to add on TeadsMediationSettings
        val key = TeadsHelper.attachListener(mListener)

        // 6. Create the mediation settings
        val extras = TeadsMediationSettings.Builder()
            .enableDebug() // Enable more logging visibility
            .pageSlotUrl(DemoSessionConfiguration.getArticleUrlOrDefault()) // Your article url
            .setMediationListenerKey(key)
            .build()

        // 7. Create the AdRequest with your settings and TeadsAdapter as an extra bundle
        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(TeadsAdapter::class.java, extras.toBundle())
            .build()

        // 8. Load the ad
        adView.loadAd(adRequest)
    }
}
