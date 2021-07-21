package tv.teads.teadssdkdemo.format.inread

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import kotlinx.android.synthetic.main.fragment_inread_scrollview.*
import kotlinx.android.synthetic.main.fragment_inread_webview.*
import tv.teads.sdk.*
import tv.teads.sdk.renderer.InReadAdView
import tv.teads.teadssdkdemo.MainActivity
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.component.CustomInReadWebviewClient
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.webviewhelper.SyncAdWebView

/**
 * InRead format within a WebView
 */
class InReadWebViewFragment : BaseFragment(), SyncAdWebView.Listener {

    private lateinit var webviewHelperSynch: SyncAdWebView
    private lateinit var adPlacement: InReadAdPlacement

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * Ad view listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_webview, container, false)
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        // 1. Setup the settings
        val placementSettings = AdPlacementSettings.Builder()
                .enableDebug()
                .build()

        // 2. Create the InReadAdPlacement
        adPlacement = TeadsSDK.createInReadPlacement(requireActivity(), pid, placementSettings)

        webviewHelperSynch = SyncAdWebView(requireContext(), webview, this@InReadWebViewFragment, "#teads-placement-slot")

        if ((activity as MainActivity).isWebViewDarkTheme
                && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
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
        val requestSettings = AdRequestSettings.Builder()
                .pageSlotUrl("http://teads.com")
                .build()

        // 3. Request the ad and register to the listener in it
        adPlacement.requestAd(requestSettings, object : InReadAdListener {
            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                webviewHelperSynch.registerTrackerView(trackerView)
            }

            override fun onAdReceived(inReadAdView: InReadAdView, adRatio: AdRatio) {
                webviewHelperSynch.registerAdView(inReadAdView)
                webviewHelperSynch.updateSlot(adRatio.getAdSlotRatio(webview.measuredWidth))
            }

            override fun onAdRatioUpdate(adRatio: AdRatio) {
                webviewHelperSynch.updateSlot(adRatio.getAdSlotRatio(webview.measuredWidth))
            }

            override fun onAdClicked() {}
            override fun onAdClosed() {
                webviewHelperSynch.closeAd()
            }
            override fun onAdError(code: Int, description: String) {
                webviewHelperSynch.clean()
            }
            override fun onAdImpression() {}
            override fun onFailToReceiveAd(failReason: String) {
                webviewHelperSynch.clean()
            }
        })
    }

    override fun getTitle(): String = "InRead Direct WebView"
}
