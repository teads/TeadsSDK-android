package tv.teads.teadssdkdemo.v6.ui.xml

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smartadserver.android.library.model.SASAdPlacement
import com.smartadserver.android.library.model.SASNativeAdElement
import com.smartadserver.android.library.model.SASNativeAdManager
import com.smartadserver.android.library.util.SASConfiguration
import tv.teads.adapter.smart.nativead.TeadsSmartViewBinder
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.teadssdkdemo.R

class MediaNativeSmartScrollViewFragment : Fragment() {

    private lateinit var nativeAdManager: SASNativeAdManager

    companion object {
        private const val SITE_ID = 385317 // Your unique site id
        private const val PAGE_NAME = "1399205" // Your unique page name
        private const val FORMAT_ID = 96445L // Your unique format id
        private const val TEADS_EXTRA_KEY = "teadsAdSettingsKey"
    }

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

        // 1. Initialize Teads Helper and Smart SDK
        TeadsHelper.initialize()
        SASConfiguration.getSharedInstance().configure(requireContext(), SITE_ID)
        SASConfiguration.getSharedInstance().isLoggingEnabled = true

        // 2. Create TeadsAdapterListener
        val teadsListener = object : TeadsAdapterListener {
            override fun onRatioUpdated(adRatio: AdRatio) {
                // Not required
            }

            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                adContainer.addView(trackerView)
            }
        }

        // 3. Attach the listener to TeadsHelper and save the key to add on TeadsMediationSettings
        val key = TeadsHelper.attachListener(teadsListener)

        // 4. Create the mediation settings
        val settingsEncoded = TeadsMediationSettings.Builder()
            .enableDebug() // Enable more logging visibility
            .setMediationListenerKey(key)
            .build()
            .toJsonEncoded()

        // 5. Create the ad placement with Teads settings
        val adPlacement = SASAdPlacement(
            SITE_ID.toLong(),
            PAGE_NAME,
            FORMAT_ID,
            "$TEADS_EXTRA_KEY=${settingsEncoded}",
            ""
        )

        // 6. Create native ad manager
        nativeAdManager = SASNativeAdManager(requireContext(), adPlacement)

        // 7. Set native ad listener
        nativeAdManager.nativeAdListener = object : SASNativeAdManager.NativeAdListener {
            override fun onNativeAdLoaded(ad: SASNativeAdElement) {
                Log.d("MediaNativeSmartScrollViewFragment", "onNativeAdLoaded")
                
                // Create and add the native ad view
                val nativeAdView = createMediaView(ad)
                adContainer.addView(nativeAdView)
            }

            override fun onNativeAdFailedToLoad(e: Exception) {
                Log.e("MediaNativeSmartScrollViewFragment", "onNativeAdFailedToLoad: ${e.message}")
            }
        }

        // 8. Load the native ad
        nativeAdManager.loadNativeAd()
    }

    private fun createMediaView(ad: SASNativeAdElement): View =
        TeadsSmartViewBinder(requireContext(), R.layout.smart_native_ad_view, ad)
            .title(R.id.ad_title)
            .body(R.id.ad_body)
            .iconImage(R.id.teads_icon)
            .callToAction(R.id.teads_cta)
            .mediaLayout(R.id.teads_mediaview)
            .adChoice(R.id.ad_choice)
            .bind()

    override fun onDestroy() {
        super.onDestroy()
        // 9. Clean from memory
        if (::nativeAdManager.isInitialized) {
            nativeAdManager.onDestroy()
        }
    }
}
