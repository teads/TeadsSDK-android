package tv.teads.teadssdkdemo.format.mediation.smart

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.smartadserver.android.library.model.SASAdPlacement
import com.smartadserver.android.library.ui.SASBannerView
import com.smartadserver.android.library.util.SASConfiguration
import tv.teads.sdk.*
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.sdk.utils.userConsent.TCFVersion
import tv.teads.teadssdkdemo.MainActivity
import tv.teads.teadssdkdemo.component.CustomInReadWebviewClient
import tv.teads.teadssdkdemo.data.SessionDataSource
import tv.teads.teadssdkdemo.databinding.FragmentInreadWebviewBinding
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.webviewhelper.SyncAdWebView

/**
 * InRead format within a WebView
 */
class SmartWebViewFragment : BaseFragment(), SyncAdWebView.Listener {
    private lateinit var binding: FragmentInreadWebviewBinding

    private val siteID = 385317L
    private val pageName = "1399205"
    private val formatID = 96445L

    private lateinit var adView: SASBannerView
    private lateinit var mListener: TeadsAdapterListener
    private lateinit var webviewHelperSynch: SyncAdWebView
    private lateinit var adPlacement: InReadAdPlacement

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * Ad view listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInreadWebviewBinding.inflate(layoutInflater)
        return binding.root
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        TeadsHelper.initialize()

        adView = SASBannerView(requireContext())

        webviewHelperSynch = SyncAdWebView(requireContext(), binding.webview, this@SmartWebViewFragment, "#teads-placement-slot")

        SASConfiguration.getSharedInstance().configure(requireContext(), siteID.toInt())
        SASConfiguration.getSharedInstance().isLoggingEnabled = true

        webviewHelperSynch.registerAdView(adView)

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
            webViewClient = CustomInReadWebviewClient(webviewHelperSynch, getTitle())
            loadUrl(this@SmartWebViewFragment.webViewUrl)
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
        val key = TeadsHelper.attachListener(mListener)

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

        val bannerPlacement = SASAdPlacement(siteID, pageName, formatID, "teadsAdSettingsKey=${extras.toJsonEncoded()}", "")

        adView.loadAd(bannerPlacement)
    }

    override fun getTitle(): String = "InRead Smart WebView"
}
