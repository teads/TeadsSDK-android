package tv.teads.teadssdkdemo.v6.ui.xml

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smartadserver.android.library.model.SASAdPlacement
import com.smartadserver.android.library.ui.SASBannerView
import com.smartadserver.android.library.util.SASConfiguration
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v6.ui.base.extensions.resizeAdContainer

class MediaSmartScrollViewFragment : Fragment() {

    private lateinit var smartAdView: SASBannerView

    companion object {
        private const val SITE_ID = 385317L // Your unique site id
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
        SASConfiguration.getSharedInstance().configure(requireContext(), SITE_ID.toInt())
        SASConfiguration.getSharedInstance().isLoggingEnabled = true // Enable more logging visibility

        // 2. Create Smart AdView and add it to view hierarchy
        smartAdView = SASBannerView(requireContext())
        adContainer.addView(smartAdView)

        // 3. Create TeadsAdapterListener
        val teadsListener = object : TeadsAdapterListener {
            override fun onRatioUpdated(adRatio: AdRatio) {
                adContainer.resizeAdContainer(adRatio)
            }

            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                adContainer.addView(trackerView)
            }
        }

        // 4. Attach the listener to TeadsHelper and save the key to add on TeadsMediationSettings
        val key = TeadsHelper.attachListener(teadsListener)

        // 5. Create the mediation settings
        val settings = TeadsMediationSettings.Builder()
            .enableDebug() // Enable more logging visibility
            .setMediationListenerKey(key)
            .build()

        // 6. Create the ad placement with Teads settings
        val bannerPlacement = SASAdPlacement(SITE_ID, PAGE_NAME, FORMAT_ID, "$TEADS_EXTRA_KEY=${settings.toJsonEncoded()}")

        // 7. Load the ad
        smartAdView.loadAd(bannerPlacement)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 8. Clean from memory
        if (::smartAdView.isInitialized) {
            smartAdView.onDestroy()
        }
    }
}
