package tv.teads.teadssdkdemo.format.mediation.smart

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.smartadserver.android.library.model.SASAdElement
import com.smartadserver.android.library.model.SASAdPlacement
import com.smartadserver.android.library.ui.SASBannerView
import com.smartadserver.android.library.util.SASConfiguration
import kotlinx.android.synthetic.main.fragment_inread_webview.*
import tv.teads.adapter.smart.SmartHelper
import tv.teads.helper.TeadsBannerAdapterListener
import tv.teads.helper.TeadsHelper
import tv.teads.sdk.android.AdSettings
import tv.teads.teadssdkdemo.MainActivity
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.component.CustomGroupWebViewClient
import tv.teads.teadssdkdemo.format.mediation.identifier.SmartIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.webviewhelper.SyncWebViewViewGroup
import tv.teads.webviewhelper.baseView.ObservableWrapperView
import kotlin.math.roundToInt

class SmartWebViewFragment : BaseFragment(), SyncWebViewViewGroup.Listener {
    private lateinit var mAdPlacement: SASAdPlacement
    private lateinit var mAdView: SASBannerView
    private lateinit var mListener: TeadsBannerAdapterListener

    private lateinit var webviewHelperSynch: SyncWebViewViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1. Initialize SASConfiguration & Teads Helper
        TeadsHelper.initialize()
        SASConfiguration.getSharedInstance().configure(view.context, SmartIdentifier.SITE_ID)

        // Enable output to Android Logcat (optional)
        SASConfiguration.getSharedInstance().isLoggingEnabled = true

        // 2. Create Smart view
        mAdView = SASBannerView(view.context)
        mAdView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)

        /* 3. Create ObservableWrapperView & SyncWebViewViewGroup instance with adview
        For a webview integration, we provide a example of tool to synchronise the ad view with the webview.
        You can find it in the webviewhelper module. {@see SyncWebViewViewGroup}
        */
        val observableWrapperView = ObservableWrapperView(context!!, mAdView)
        webviewHelperSynch = SyncWebViewViewGroup(webview, observableWrapperView, this, "#teads-placement-slot")

        // 4. Attach listener (will include Teads events) &
        // call the helper inside onBannerAdLoaded
        mAdView.bannerListener = object : SASBannerView.BannerListener {
            override fun onBannerAdLoaded(banner: SASBannerView, p1: SASAdElement) {
                webviewHelperSynch.displayAd()
            }

            override fun onBannerAdFailedToLoad(banner: SASBannerView, e: Exception) {
                activity?.runOnUiThread {
                    Toast.makeText(banner?.context, "Ad loading failed: onAdFailedToLoad(${e?.message})", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onBannerAdClicked(banner: SASBannerView) {}
            override fun onBannerAdExpanded(banner: SASBannerView) {}
            override fun onBannerAdCollapsed(banner: SASBannerView) {}
            override fun onBannerAdResized(banner: SASBannerView) {}
            override fun onBannerAdClosed(banner: SASBannerView) {}
            override fun onBannerAdVideoEvent(banner: SASBannerView, p1: Int) {}

        }

        /* 5. Create a TeadsBannerAdapterListener
        You need to create an instance for each instance of SAS view
        it needs to be a strong reference to it, so our helper can cleanup when you don't need it anymore
         */
        mListener = object : TeadsBannerAdapterListener {
            override fun onRatioUpdated(adRatio: Float) {
                val params: ViewGroup.LayoutParams = mAdView.layoutParams

                // Here the width is MATCH_PARENT
                params.height = (params.width / adRatio).roundToInt()

                // 6. You need to call updateSlot method from the helper
                // Some creative can resize by itself, to handle it we have to notify the webview helper
                // this ratio doesn't contains the footer and the header
                // To manage this behavior, a work around is to substract 0.2 to the media ratio
                webviewHelperSynch.updateSlot(adRatio - 0.2f)

                mAdView.layoutParams = params
            }
        }

        // 7. Attach the listener to the helper and save the key
        val key = TeadsHelper.attachListener(mListener)

        // 8. Create the AdSettings to customize our Teads AdView
        val extras = AdSettings.Builder()
                // Needed by european regulation
                // See https://mobile.teads.tv/sdk/documentation/android/gdpr-consent
                .userConsent("1", "0001")
                // The article url if you are a news publisher to increase your earnings
                .pageUrl("https://page.com/article1/")
                // /!\ You need to add the key to the settings
                .addAdapterListener(key)
                .build()

        // 9. Use the helper to transform settings to a jsonEncoded
        val jsonEncoded = SmartHelper.getTargetFromTeadsAdSettings(extras)

        // 10. Create the SASAdPlacement using your filled information above
        mAdPlacement = SASAdPlacement(SmartIdentifier.SITE_ID.toLong(),
                SmartIdentifier.PAGE_NAME, SmartIdentifier.getFormatFromPid(pid).toLong(),
                jsonEncoded, SmartIdentifier.SUPPLY_CHAIN)

        // 11. Create a custom WebViewclient with helper in it
        if ((activity as MainActivity).isWebViewDarkTheme
                && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            WebSettingsCompat.setForceDark(webview.settings, WebSettingsCompat.FORCE_DARK_ON)
        }
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = CustomGroupWebViewClient(webviewHelperSynch, getTitle())
        webview.loadUrl(this.webViewUrl)
    }

    override fun onHelperReady(adContainer: ViewGroup) {
        // 12. Load the ad
        mAdView.loadAd(mAdPlacement)
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

    override fun getTitle(): String = "InRead Smart WebView"
}