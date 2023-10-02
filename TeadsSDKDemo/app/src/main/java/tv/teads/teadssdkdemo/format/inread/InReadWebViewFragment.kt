package tv.teads.teadssdkdemo.format.inread

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import tv.teads.sdk.*
import tv.teads.sdk.renderer.InReadAdView
import tv.teads.teadssdkdemo.MainActivity
import tv.teads.teadssdkdemo.component.CustomInReadWebviewClient
import tv.teads.teadssdkdemo.databinding.FragmentInreadWebviewBinding
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.webviewhelper.SyncAdWebView

/**
 * InRead format within a WebView
 */
class InReadWebViewFragment : BaseFragment(), SyncAdWebView.Listener {
    lateinit var binding: FragmentInreadWebviewBinding
    private lateinit var webviewHelperSynch: SyncAdWebView
    private lateinit var adPlacement: InReadAdPlacement

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * Ad view listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentInreadWebviewBinding.inflate(layoutInflater)
        return binding.root
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        // 1. Setup the settings
        val placementSettings = AdPlacementSettings.Builder()
                .enableDebug()
                .build()

        // 2. Create the InReadAdPlacement
        adPlacement = TeadsSDK.createInReadPlacement(requireActivity(), pid, placementSettings)

        webviewHelperSynch = SyncAdWebView(requireContext(), binding.webview, this@InReadWebViewFragment, "#teads-placement-slot")

        if ((activity as MainActivity).isWebViewDarkTheme
                && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            WebSettingsCompat.setForceDark(binding.webview.settings, WebSettingsCompat.FORCE_DARK_ON)
        }

        with(binding.webview) {
            settings.javaScriptEnabled = true
            webViewClient = CustomInReadWebviewClient(webviewHelperSynch, title)
            loadUrl(this@InReadWebViewFragment.webViewUrl)
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
        val requestSettings = AdRequestSettings.Builder()
                .pageSlotUrl("http://teads.com")
                .build()

        // 3. Request the ad and register to the listener in it
        adPlacement.requestAd(requestSettings, object : InReadAdViewListener {
            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                webviewHelperSynch.registerTrackerView(trackerView)
            }

            override fun onAdReceived(ad: InReadAdView, adRatio: AdRatio) {
                webviewHelperSynch.registerAdView(ad)
                webviewHelperSynch.updateSlot(adRatio.getAdSlotRatio(binding.webview.measuredWidth))
            }

            override fun onAdRatioUpdate(adRatio: AdRatio) {
                webviewHelperSynch.updateSlot(adRatio.getAdSlotRatio(binding.webview.measuredWidth))
            }

            override fun onAdClosed() {
                webviewHelperSynch.closeAd()
            }

            override fun onAdError(code: Int, description: String) {
                webviewHelperSynch.clean()
            }

            override fun onFailToReceiveAd(failReason: String) {
                webviewHelperSynch.clean()
            }

            override fun onAdClicked() {}
            override fun onAdImpression() {}
        })
    }

    override fun getTitle(): String = "InRead Direct WebView"
}
