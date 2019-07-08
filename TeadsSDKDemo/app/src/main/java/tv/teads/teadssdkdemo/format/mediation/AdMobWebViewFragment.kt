package tv.teads.teadssdkdemo.format.mediation

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.fragment_inread_webview.*
import org.greenrobot.eventbus.Subscribe
import tv.teads.adapter.admob.TeadsAdNetworkExtras
import tv.teads.adapter.admob.TeadsAdapter
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.ReloadEvent
import tv.teads.webviewhelper.SyncWebViewViewGroup
import tv.teads.webviewhelper.baseView.ObservableWrapperView

/**
 * Display inRead as Banner within a WebView using AdMob Mediation.
 * This is an exemple using Teads WebView Helper classes to display ads within a WebView content.
 */
class AdMobWebViewFragment : BaseFragment(), SyncWebViewViewGroup.Listener {

    private lateinit var webviewHelperSynch: SyncWebViewViewGroup
    private lateinit var adView: AdView

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * Ad view listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    private val adListener = object : AdListener() {

        override fun onAdLoaded() {
            webviewHelperSynch.displayAd()
        }

        override fun onAdFailedToLoad(errorCode: Int) {
            Toast.makeText(context, "Ad loading failed: onAdFailedToLoad($errorCode)",
                           Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_webview, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        if(context == null) {
            return
        }

        // 1. Init AdMob (could be done in your Application class)
        MobileAds.initialize(context, ADMOB_TEADS_APP_ID)

        // 2. Create AdMob view and add it to view hierarchy
        adView = AdView(context)
        adView.adUnitId = ADMOB_TEADS_BANNER_ID
        adView.adSize = AdSize.MEDIUM_RECTANGLE

        /*
        For a webview integration, we provide a example of tool to synchronise the ad view with the webview.
        You can find it in the webviewhelper module. {@see SyncWebViewTeadsAdView}
         */
        val observableWrapperView = ObservableWrapperView(context, adView)
        webviewHelperSynch = SyncWebViewViewGroup(webview, observableWrapperView, this, "p:nth-child(7)")

        // 3. Attach ad listener
        adView.adListener = adListener

        // 4. Load WebView page
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = CustomWebviewClient(webviewHelperSynch)
        webview.loadUrl(this.webViewUrl)
    }

    override fun onDestroy() {
        super.onDestroy()
        webviewHelperSynch.clean()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        webviewHelperSynch.onConfigurationChanged()
    }

    @Subscribe
    fun onReloadEvent(event: ReloadEvent) {
        // not used
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * WebView helper listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onHelperReady(adContainer: ViewGroup) {
        // The helper is ready, we can now load the ad
        // 4. Load a new ad (this will call AdMob and Teads afterward)
        val extras = TeadsAdNetworkExtras.Builder()
                // Needed by european regulation
                // See https://mobile.teads.tv/sdk/documentation/android/gdpr-consent
                .userConsent("1", "0001")
                .enableDebug()
                // The article url if you are a news publisher to increase your
                // earnings
                .pageUrl("https://page.com/article1/")
                .build()
        val adRequest = AdRequest.Builder()
                .addCustomEventExtrasBundle(TeadsAdapter::class.java, extras.extras)
                .build()

        adView.loadAd(adRequest)
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * WebViewClient
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    private inner class CustomWebviewClient constructor(private val webviewHelperSynch: SyncWebViewViewGroup) : WebViewClient() {

        override fun onPageFinished(view: WebView, url: String) {
            webviewHelperSynch.injectJS()

            super.onPageFinished(view, url)
        }
    }

    companion object {
        // FIXME This ids should be replaced by your own AdMob application and ad block ids
        const val ADMOB_TEADS_APP_ID = "ca-app-pub-3570580224725271~3869556230"
        const val ADMOB_TEADS_BANNER_ID = "ca-app-pub-3570580224725271/1481793511"
    }
}
