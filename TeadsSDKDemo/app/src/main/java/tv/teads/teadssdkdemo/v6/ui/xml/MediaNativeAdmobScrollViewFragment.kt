package tv.teads.teadssdkdemo.v6.ui.xml

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import tv.teads.adapter.admob.nativead.TeadsNativeAdapter
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration

class MediaNativeAdmobScrollViewFragment : Fragment() {

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

        // 2. Create your Admob Native AdView and add it to container
        val nativeAdView = LayoutInflater.from(requireContext())
            .inflate(R.layout.admob_native_ad_view, adContainer, false) as NativeAdView
        
        adContainer.addView(nativeAdView)

        // 3. Create AdLoader for native ads
        val adLoader = AdLoader.Builder(requireContext(), DemoSessionConfiguration.getPlacementIdOrDefault())
            .forNativeAd { ad: NativeAd ->
                // Configure the native ad view
                with(nativeAdView) {
                    mediaView = findViewById(R.id.ad_mob_media)
                    headlineView = findViewById(R.id.ad_mob_headline)
                    bodyView = findViewById(R.id.ad_mob_body)
                    callToActionView = findViewById(R.id.sponsor_more)

                    // Set the native ad content
                    headlineView?.let { it as TextView }?.text = ad.headline
                    bodyView?.let { it as TextView }?.text = ad.body

                    setNativeAd(ad)
                }
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("MediaNativeAdmobScrollViewFragment", "onAdFailedToLoad: ${error.message}")
                }

                override fun onAdLoaded() {
                    Log.d("MediaNativeAdmobScrollViewFragment", "onAdLoaded")
                }

                override fun onAdImpression() {
                    Log.d("MediaNativeAdmobScrollViewFragment", "onAdImpression")
                }

                override fun onAdClicked() {
                    Log.d("MediaNativeAdmobScrollViewFragment", "onAdClicked")
                }

                override fun onAdOpened() {
                    Log.d("MediaNativeAdmobScrollViewFragment", "onAdOpened")
                }

                override fun onAdClosed() {
                    Log.d("MediaNativeAdmobScrollViewFragment", "onAdClosed")
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        // 4. Create TeadsAdapterListener
        val teadsListener = object : TeadsAdapterListener {
            override fun onRatioUpdated(adRatio: AdRatio) {
                // For native ads, ratio updates are typically handled by the ad itself
                Log.d("MediaNativeAdmobScrollViewFragment", "onRatioUpdated: $adRatio")
            }

            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                nativeAdView.addView(trackerView)
            }
        }

        // 5. Attach the listener to TeadsHelper and save the key
        val key = TeadsHelper.attachListener(teadsListener)

        // 6. Create the mediation settings
        val extras = TeadsMediationSettings.Builder()
            .enableDebug() // Enable more logging visibility
            .pageSlotUrl(DemoSessionConfiguration.getArticleUrlOrDefault()) // Your article url
            .setMediationListenerKey(key)
            .build()

        // 7. Create the AdRequest with your settings and TeadsNativeAdapter as an extra bundle
        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(TeadsNativeAdapter::class.java, extras.toBundle())
            .build()

        // 8. Load the native ad
        adLoader.loadAd(adRequest)
    }
}
