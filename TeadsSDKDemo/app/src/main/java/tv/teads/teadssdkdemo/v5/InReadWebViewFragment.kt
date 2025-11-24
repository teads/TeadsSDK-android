package tv.teads.teadssdkdemo.v5

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdPlacementSettings
import tv.teads.sdk.AdRatio
import tv.teads.sdk.AdRequestSettings
import tv.teads.sdk.InReadAdPlacement
import tv.teads.sdk.InReadAdViewListener
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.renderer.InReadAdView
import tv.teads.teadssdkdemo.BuildConfig
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.v5.utils.CustomInReadWebviewClient
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.webviewhelper.SyncAdWebView
import tv.teads.webviewhelper.baseView.ObservableWebView

/**
 * InRead format within a WebView with synchronized scrolling
 * Uses v5.x SDK API (InReadAdPlacement) with WebView helper for ad/content synchronization
 */
class InReadWebViewFragment : Fragment(), SyncAdWebView.Listener {

    private lateinit var webviewHelperSync: SyncAdWebView
    private lateinit var adPlacement: InReadAdPlacement
    private lateinit var webView: ObservableWebView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inread_webview, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupContent()
    }

    private fun setupContent() {
        webView = requireView().findViewById(R.id.webview)

        // 0. Enable more logging visibility for testing purposes
        TeadsSDK.testMode = BuildConfig.DEBUG

        // 1. Setup the placement settings
        val placementSettings = AdPlacementSettings.Builder()
            .enableDebug()
            .build()

        // 2. Create the InReadAdPlacement
        adPlacement = TeadsSDK.createInReadPlacement(
            requireActivity(),
            DemoSessionConfiguration.getPlacementIdOrDefault().toInt(),
            placementSettings
        )

        // 3. Initialize WebView helper for ad/content synchronization
        webviewHelperSync = SyncAdWebView(
            requireContext(),
            webView,
            this@InReadWebViewFragment,
            "#teads-placement-slot"
        )

        // 4. Configure WebView
        with(webView) {
            settings.javaScriptEnabled = true
            webViewClient = CustomInReadWebviewClient(webviewHelperSync, "InRead Direct WebView")
            loadUrl("file:///android_asset/demo.html")
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        webviewHelperSync.onConfigurationChanged()
    }

    // 5. WebView helper listener - called when WebView is ready for ad insertion
    override fun onHelperReady(adContainer: ViewGroup) {
        val requestSettings = AdRequestSettings.Builder()
            .pageSlotUrl(DemoSessionConfiguration.getArticleUrlOrDefault())
            .build()

        // 6. Request the ad with lifecycle callbacks
        adPlacement.requestAd(requestSettings, object : InReadAdViewListener {
            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                webviewHelperSync.registerTrackerView(trackerView)
                Log.d("InReadWebView", "Tracker view registered")
            }

            override fun onAdReceived(ad: InReadAdView, adRatio: AdRatio) {
                webviewHelperSync.registerAdView(ad)
                webviewHelperSync.updateSlot(adRatio.getAdSlotRatio(webView.measuredWidth))
                Log.d("InReadWebView", "Ad received with ratio: $adRatio")
            }

            override fun onAdRatioUpdate(adRatio: AdRatio) {
                webviewHelperSync.updateSlot(adRatio.getAdSlotRatio(webView.measuredWidth))
                Log.d("InReadWebView", "Ad ratio updated: $adRatio")
            }

            override fun onAdClosed() {
                webviewHelperSync.closeAd()
                Log.d("InReadWebView", "Ad closed")
            }

            override fun onAdError(code: Int, description: String) {
                webviewHelperSync.clean()
                Log.e("InReadWebView", "Ad error: $code - $description")
            }

            override fun onFailToReceiveAd(failReason: String) {
                webviewHelperSync.clean()
                Log.e("InReadWebView", "Failed to receive ad: $failReason")
            }

            override fun onAdClicked() {
                Log.d("InReadWebView", "Ad clicked")
            }

            override fun onAdImpression() {
                Log.d("InReadWebView", "Ad impression")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // 7. Clean from memory
        webviewHelperSync.clean()
    }
}
