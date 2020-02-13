package tv.teads.teadssdkdemo.format.example

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_inread_webview_wrap.*
import org.greenrobot.eventbus.Subscribe
import tv.teads.sdk.android.AdFailedReason
import tv.teads.sdk.android.AdSettings
import tv.teads.sdk.android.InReadAdView
import tv.teads.sdk.android.TeadsListener
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.ReloadEvent
import tv.teads.webviewhelper.SyncWebViewTeadsAdView

/**
 * InRead format within a WebView where the WebView scroll is managed by a ScrollView and not the WebView it self,
 * equal to have the WebView height set to wrap_content.
 */
class InReadWebViewWrapFragment : BaseFragment(), SyncWebViewTeadsAdView.Listener {

    private lateinit var adView: InReadAdView

    private lateinit var webviewHelperSynch: SyncWebViewTeadsAdView

    private val teadsListener = object : TeadsListener() {

        override fun onAdFailedToLoad(adFailedReason: AdFailedReason?) {
            Toast.makeText(this@InReadWebViewWrapFragment.activity, getString(R.string.didfail), Toast.LENGTH_SHORT).show()
        }

        override fun onError(s: String?) {
            Toast.makeText(this@InReadWebViewWrapFragment.activity, getString(R.string.didfail_playback), Toast.LENGTH_SHORT).show()
        }

        override fun onAdLoaded(adRatio: Float) {
            webviewHelperSynch.updateSlot(adRatio)
            webviewHelperSynch.displayAd()
        }

        override fun onRatioUpdated(adRatio: Float) {
            // Some creative can resize by itself, to handle it we have to notify the webview helper
            // But unlike the ratio in onAdLoaded method, this ratio doesn't contains the footer and the header
            // To manage this behavior, a work around is to substract 0.2 to the media ratio
            webviewHelperSynch.updateSlot(adRatio - 0.2f)
        }

        override fun closeAd() {
            webviewHelperSynch.closeAd()
        }
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * Ad view listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_webview_wrap, container, false)
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        adView = InReadAdView(context)
        webviewHelperSynch = SyncWebViewTeadsAdView(webview, adView, this, "h2")
        adView.setPid(pid)
        adView.enableDebug()
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = CustomWebviewClient(webviewHelperSynch)
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

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onReloadEvent(event: ReloadEvent) {
        // Not used
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * WebView helper listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onHelperReady(adContainer: ViewGroup) {
        adView.listener = teadsListener
        adView.setAdContainerView(adContainer)
        adView.load(AdSettings.Builder().pageUrl("https://example.com/article1").build())
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * WebViewClient
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    private inner class CustomWebviewClient internal constructor(private val webviewHelperSynch: SyncWebViewTeadsAdView) : WebViewClient() {

        override fun onPageFinished(view: WebView, url: String) {
            webviewHelperSynch.injectJS()

            super.onPageFinished(view, url)
        }
    }
}
