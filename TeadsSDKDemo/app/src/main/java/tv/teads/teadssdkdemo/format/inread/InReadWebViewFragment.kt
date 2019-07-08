package tv.teads.teadssdkdemo.format.inread

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_inread_webview.*

import org.greenrobot.eventbus.Subscribe

import tv.teads.sdk.android.AdFailedReason
import tv.teads.sdk.android.InReadAdView
import tv.teads.sdk.android.TeadsListener
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.ReloadEvent
import tv.teads.webviewhelper.SyncWebViewTeadsAdView

/**
 * InRead format within a WebView
 */
class InReadWebViewFragment : BaseFragment(), SyncWebViewTeadsAdView.Listener {

    private lateinit var mWebviewHelperSynch: SyncWebViewTeadsAdView

    private lateinit var adView: InReadAdView

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * Ad view listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    private val mTeadsListener = object : TeadsListener() {

        override fun onAdFailedToLoad(adFailedReason: AdFailedReason?) {
            Toast.makeText(this@InReadWebViewFragment.activity, getString(R.string.didfail), Toast.LENGTH_SHORT).show()
        }

        override fun onError(s: String?) {
            Toast.makeText(this@InReadWebViewFragment.activity, getString(R.string.didfail_playback), Toast.LENGTH_SHORT).show()
        }

        override fun onAdLoaded(adRatio: Float) {
            mWebviewHelperSynch.updateSlot(adRatio)
            mWebviewHelperSynch.displayAd()
        }

        override fun closeAd() {
            mWebviewHelperSynch.closeAd()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_webview, container, false)
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        adView = InReadAdView(context)

        /*
        For a webview integration, we provide a example of tool to synchronise the ad view with the webview.
        You can find it in the webviewhelper module. {@see SyncWebViewTeadsAdView}
         */
        mWebviewHelperSynch = SyncWebViewTeadsAdView(webview, adView, this, "p:nth-child(7)")

        adView.setPid(pid)
        adView.listener = mTeadsListener

        webview.settings.javaScriptEnabled = true
        webview.webViewClient = CustomWebviewClient(mWebviewHelperSynch)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        webview.loadUrl(this.webViewUrl)
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.clean()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mWebviewHelperSynch.onConfigurationChanged()
    }

    @Subscribe
    fun onReloadEvent(event: ReloadEvent) {
        // Not used
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * WebView helper listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onHelperReady(adContainer: ViewGroup) {
            //The helper is ready we can now load the ad
            // Current Android SDK version set the adContainer automatically, it was not suppoed to.
            // adView.setAdContainerView(adContainer);
            adView.load()
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
