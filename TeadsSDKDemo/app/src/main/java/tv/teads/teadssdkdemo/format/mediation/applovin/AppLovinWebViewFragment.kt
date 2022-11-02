package tv.teads.teadssdkdemo.format.mediation.applovin

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.ads.MaxAdView
import kotlinx.android.synthetic.main.fragment_inread_webview.*
import tv.teads.sdk.*
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.sdk.utils.userConsent.TCFVersion
import tv.teads.teadssdkdemo.MainActivity
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.component.CustomInReadWebviewClient
import tv.teads.teadssdkdemo.format.mediation.identifier.AppLovinIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.webviewhelper.SyncAdWebView

/**
 * InRead format within a WebView
 */
class AppLovinWebViewFragment : BaseFragment(), SyncAdWebView.Listener {

    private lateinit var adView: MaxAdView
    private lateinit var mListener: TeadsAdapterListener
    private lateinit var webviewHelperSynch: SyncAdWebView

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * Ad view listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_webview, container, false)
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        // 1. Initialize Teads Helper (Don't forget to initialize AppLovin aswell see SplashScreen)
        TeadsHelper.initialize()

        // 2. Create WebViewHelper
        webviewHelperSynch = SyncAdWebView(requireContext(), webview, this@AppLovinWebViewFragment, "#teads-placement-slot")

        // 3. Create MaxAdView view
        adView = MaxAdView(AppLovinIdentifier.getAdUnitFromPid(pid), MaxAdFormat.MREC, context)

        webviewHelperSynch.registerAdView(adView)

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
            && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)
        ) {
            WebSettingsCompat.setForceDark(webview.settings, WebSettingsCompat.FORCE_DARK_ON)
        }
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = CustomInReadWebviewClient(webviewHelperSynch, getTitle())
        webview.loadUrl(this.webViewUrl)
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
        val settingsEncoded = TeadsMediationSettings.Builder()
            // Needed by european regulation
            // See https://mobile.teads.tv/sdk/documentation/android/gdpr-consent
            .userConsent("1", "0001", TCFVersion.V1, 12)
            .enableDebug()
            // Add the helper in the adsettings
            .setMediationListenerKey(key)
            // The article url if you are a news publisher to increase your
            // earnings
            .pageSlotUrl("https://page.com/article1/")
            .build()
            .toJsonEncoded()

        // 7. Add the settings encoded to the adView using this key
        adView.setLocalExtraParameter("teadsSettings", settingsEncoded)

        // 8. Load the ad with the AdRequest
        adView.loadAd()
    }

    override fun getTitle(): String = "InRead AppLovin WebView"
}
