package tv.teads.teadssdkdemo.format.mediation.admob

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.fragment_inread_webview.*
import tv.teads.adapter.admob.TeadsAdapter
import tv.teads.helper.TeadsBannerAdapterListener
import tv.teads.helper.TeadsHelper
import tv.teads.sdk.android.AdSettings
import tv.teads.teadssdkdemo.MainActivity
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.component.CustomGroupWebViewClient
import tv.teads.teadssdkdemo.format.mediation.identifier.AdMobIdentifier.ADMOB_TEADS_APP_ID
import tv.teads.teadssdkdemo.format.mediation.identifier.AdMobIdentifier.ADMOB_TEADS_BANNER_ID
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.webviewhelper.SyncWebViewViewGroup
import tv.teads.webviewhelper.baseView.ObservableWrapperView
import kotlin.math.roundToInt

/**
 * Display inRead as Banner within a WebView using AdMob Mediation.
 * This is an exemple using Teads WebView Helper classes to display ads within a WebView content.
 */
class AdMobWebViewFragment : BaseFragment(), SyncWebViewViewGroup.Listener {
    private lateinit var mListener: TeadsBannerAdapterListener

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
        // 1. Initialize AdMob & Teads Helper
        MobileAds.initialize(context, ADMOB_TEADS_APP_ID)
        TeadsHelper.initialize()

        // 2. Create AdMob view, setup and add it to view hierarchy
        adView = AdView(context)
        adView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
        adView.adUnitId = ADMOB_TEADS_BANNER_ID
        adView.adSize = AdSize.MEDIUM_RECTANGLE

        /* 3. Create ObservableWrapperView & SyncWebViewViewGroup instance
        For a webview integration, we provide a example of tool to synchronise the ad view with the webview.
        You can find it in the webviewhelper module. {@see SyncWebViewViewGroup}
         */
        val observableWrapperView = ObservableWrapperView(context!!, adView)
        webviewHelperSynch = SyncWebViewViewGroup(webview, observableWrapperView, this, "#teads-placement-slot")

        // 4. Attach listener (will include Teads events)
        adView.adListener = adListener

        // 5.Create a custom WebViewclient with helper in it
        if ((activity as MainActivity).isWebViewDarkTheme
                && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            WebSettingsCompat.setForceDark(webview.settings, WebSettingsCompat.FORCE_DARK_ON)
        }
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = CustomGroupWebViewClient(webviewHelperSynch, getTitle())
        webview.loadUrl(this.webViewUrl)
    }


    override fun onDestroy() {
        super.onDestroy()

        // Don't forget to call the helper here
        webviewHelperSynch.clean()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Don't forget to call the helper here
        webviewHelperSynch.onConfigurationChanged()
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * WebView helper listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onHelperReady(adContainer: ViewGroup) {
        /* 6. Create a TeadsBannerAdapterListener
        You need to create an instance for each instance of AdMob view
        it needs to be a strong reference to it, so our helper can cleanup when you don't need it anymore
         */
        mListener = object : TeadsBannerAdapterListener {
            override fun onRatioUpdated(adRatio: Float) {
                val params: ViewGroup.LayoutParams = adView.layoutParams

                // Here the width of parent is MATCH_PARENT
                params.height = ((adView.parent as View).width / adRatio).roundToInt()

                /* 7. You need to call updateSlot method from the helper
                Some creative can resize by itself, to handle it we have to notify the webview helper
                this ratio doesn't contains the footer and the header
                To manage this behavior, a work around is to substract 0.2 to the media ratio
                 */
                webviewHelperSynch.updateSlot(adRatio - 0.2f)

                adView.layoutParams = params
            }
        }

        // 8. Attach and Register its key in the helper
        val key = TeadsHelper.attachListener(mListener)

        // 9. Create the AdSettings to customize our Teads AdView
        val extras = AdSettings.Builder()
                // Needed by european regulation
                // See https://mobile.teads.tv/sdk/documentation/android/gdpr-consent
                .userConsent("1", "0001")
                .enableDebug()
                // Add the helper in the adsettings
                .addAdapterListener(key)
                // The article url if you are a news publisher to increase your
                // earnings
                .pageUrl("https://page.com/article1/")
                .build()

        // 10. Create the AdRequest with the previous settings
        val adRequest = AdRequest.Builder()
                .addCustomEventExtrasBundle(TeadsAdapter::class.java, extras.toBundle())
                .build()

        // 11. Load the ad with the AdRequest
        adView.loadAd(adRequest)
    }

    override fun getTitle(): String = "InRead AdMob WebView"
}
