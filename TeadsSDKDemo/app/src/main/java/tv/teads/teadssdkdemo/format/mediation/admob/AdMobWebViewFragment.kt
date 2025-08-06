package tv.teads.teadssdkdemo.format.mediation.admob

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import tv.teads.adapter.admob.TeadsAdapter
import tv.teads.sdk.*
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.sdk.utils.userConsent.TCFVersion
import tv.teads.teadssdkdemo.MainActivity
import tv.teads.teadssdkdemo.component.CustomInReadWebviewClient
import tv.teads.teadssdkdemo.data.SessionDataSource
import tv.teads.teadssdkdemo.databinding.FragmentInreadWebviewBinding
import tv.teads.teadssdkdemo.format.mediation.identifier.AdMobIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.webviewhelper.SyncAdWebView

/**
 * InRead format within a WebView
 */
class AdMobWebViewFragment : BaseFragment(), SyncAdWebView.Listener {
    private lateinit var binding: FragmentInreadWebviewBinding

    private lateinit var adView: AdView
    private lateinit var mListener: TeadsAdapterListener
    private lateinit var webviewHelperSynch: SyncAdWebView

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * Ad view listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentInreadWebviewBinding.inflate(layoutInflater)
        return binding.root
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        // 1. Initialize AdMob & Teads Helper
        MobileAds.initialize(requireContext())
        TeadsHelper.initialize()

        // For testing purposes
        MobileAds.setRequestConfiguration(AdMobIdentifier.TEST_DEVICE_ID_CONFIG)

        // 2. Create WebViewHelper
        webviewHelperSynch = SyncAdWebView(requireContext(), binding.webview, this@AdMobWebViewFragment, "#teads-placement-slot")

        // 3. Create AdMob view, setup and register it
        adView = AdView(requireContext())

        webviewHelperSynch.registerAdView(adView)
        adView.adUnitId = AdMobIdentifier.getAdUnitFromPid(pid)
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE)

        // 4. Create the TeadsAdapterListener
        mListener = object : TeadsAdapterListener {
            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                webviewHelperSynch.registerTrackerView(trackerView)
            }

            override fun onRatioUpdated(adRatio: AdRatio) {
                val params: ViewGroup.LayoutParams = adView.layoutParams

                // Here the width of parent is MATCH_PARENT
                params.height = adRatio.calculateHeight(adView.measuredWidth)

                /* You need to call updateSlot method from the helper
                Some creative can resize by itself, to handle it we have to notify the webview helper
                this ratio doesn't contains the footer and the header
                To manage this behavior, a work around is to substract 0.2 to the media ratio
                 */
                webviewHelperSynch.updateSlot(adRatio.getAdSlotRatio(adView.measuredWidth))

                adView.layoutParams = params
            }
        }

        if ((activity as MainActivity).isWebViewDarkTheme
                && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            WebSettingsCompat.setForceDark(binding.webview.settings, WebSettingsCompat.FORCE_DARK_ON)
        }

        with(binding.webview) {
            settings.javaScriptEnabled = true
            webViewClient = CustomInReadWebviewClient(webviewHelperSynch, title)
            loadUrl(this@AdMobWebViewFragment.webViewUrl)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        webviewHelperSynch.onConfigurationChanged()
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * WebView helper listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onHelperReady(adContainer: ViewGroup) {
        // 5. Attach and Register its key in the helper
        val key = TeadsHelper.attachListener(mListener)

        // 6. Create the AdSettings to customize our Teads AdView
        val extras = TeadsMediationSettings.Builder()
                // Needed by european regulation
                // See https://mobile.teads.tv/sdk/documentation/android/gdpr-consent
                .userConsent("1", SessionDataSource.FAKE_GDPR_STR, TCFVersion.V2, 12)
                .enableDebug()
                // Add the helper in the adsettings
                .setMediationListenerKey(key)
                // The article url if you are a news publisher to increase your
                // earnings
                .pageSlotUrl("https://page.com/article1/")
                .build()

        // 7. Create the AdRequest with the previous settings
        val adRequest = AdRequest.Builder()
                .addNetworkExtrasBundle(TeadsAdapter::class.java, extras.toBundle())
                .build()

        // 8. Load the ad with the AdRequest
        adView.loadAd(adRequest)
    }

    override fun getTitle(): String = "InRead AdMob WebView"
}
