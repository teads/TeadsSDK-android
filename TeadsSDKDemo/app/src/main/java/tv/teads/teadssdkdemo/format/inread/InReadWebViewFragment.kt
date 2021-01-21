package tv.teads.teadssdkdemo.format.inread

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import kotlinx.android.synthetic.main.fragment_inread_webview.*
import tv.teads.sdk.android.AdFailedReason
import tv.teads.sdk.android.AdSettings
import tv.teads.sdk.android.InReadAdView
import tv.teads.sdk.android.TeadsListener
import tv.teads.teadssdkdemo.MainActivity
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.component.CustomInReadWebviewClient
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.webviewhelper.SyncWebViewTeadsAdView

/**
 * InRead format within a WebView
 */
class InReadWebViewFragment : BaseFragment(), SyncWebViewTeadsAdView.Listener {

    private lateinit var webviewHelperSynch: SyncWebViewTeadsAdView

    private lateinit var adView: InReadAdView

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * Ad view listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_webview, container, false)
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        // 1. Create the InReadAdView
        adView = InReadAdView(context)

        /*
        2. Create a SyncWebViewTeadsAdView
        For a webview integration, we provide a example of tool to synchronise the ad view with the webview.
        You can find it in the webviewhelper module. {@see SyncWebViewTeadsAdView}
         */
        webviewHelperSynch = SyncWebViewTeadsAdView(webview, adView, this, "#teads-placement-slot")

        // 2. Setup the AdView
        adView.setPid(pid)

        /* 3. Subscribe to our listener
        You need to implement at least onAdLoaded & onRatioUpdated to synchronize the AdView with the
        previous helper you created.
         */
        adView.listener = object : TeadsListener() {

            override fun onAdFailedToLoad(adFailedReason: AdFailedReason?) {
                Toast.makeText(this@InReadWebViewFragment.activity, getString(R.string.didfail), Toast.LENGTH_SHORT).show()
            }

            override fun onError(s: String?) {
                Toast.makeText(this@InReadWebViewFragment.activity, getString(R.string.didfail_playback), Toast.LENGTH_SHORT).show()
            }

            override fun onAdLoaded(adRatio: Float) {
                webviewHelperSynch.updateSlot(adRatio)
                webviewHelperSynch.displayAd()
            }

            override fun onRatioUpdated(adRatio: Float) {
                // Some creative can resize by itself, to handle it we have to notify the webview helper
                // But unlike the ratio in onAdLoaded method, this ratio doesn't contains the footer and the header
                // To manage this behavior, a work around is to add an estimated header height to the media ratio
                val screenDensity: Float = context?.resources?.displayMetrics?.density ?: 1f
                val estimatedHeaderHeightPX = 40 * screenDensity
                val estimatedTotalHeightPX = adView.width.toFloat() / adRatio + estimatedHeaderHeightPX
                val ratioWithHeaderIncluded = adView.width.toFloat() / estimatedTotalHeightPX
                webviewHelperSynch.updateSlot(ratioWithHeaderIncluded)
            }

            override fun closeAd() {
                webviewHelperSynch.closeAd()
            }
        }

        if ((activity as MainActivity).isWebViewDarkTheme
                && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            WebSettingsCompat.setForceDark(webview.settings, WebSettingsCompat.FORCE_DARK_ON)
        }
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = CustomInReadWebviewClient(webviewHelperSynch, getTitle())
        webview.loadUrl(this.webViewUrl)
    }

    override fun onDestroy() {
        super.onDestroy()

        adView.clean()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        webviewHelperSynch.onConfigurationChanged()
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * WebView helper listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onHelperReady(adContainer: ViewGroup) {
        // 2. Customize the AdView with your settings
        val settings = AdSettings.Builder()
                .enableDebug()
                .build()

        // 5. Load the ad with the created settings
        //    You can still load without settings.
        adView.load(settings)
    }

    override fun getTitle(): String = "InRead Direct WebView"
}
