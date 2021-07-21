package tv.teads.teadssdkdemo.format.mediation.mopub

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.mopub.mobileads.MoPubView
import kotlinx.android.synthetic.main.fragment_inread_scrollview.*
import kotlinx.android.synthetic.main.fragment_inread_webview.*
import tv.teads.adapter.admob.TeadsAdapter
import tv.teads.sdk.*
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.sdk.renderer.InReadAdView
import tv.teads.sdk.utils.userConsent.TCFVersion
import tv.teads.teadssdkdemo.MainActivity
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.component.CustomInReadWebviewClient
import tv.teads.teadssdkdemo.format.mediation.identifier.AdMobIdentifier
import tv.teads.teadssdkdemo.format.mediation.identifier.MoPubIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.webviewhelper.SyncAdWebView
import kotlin.math.roundToInt

/**
 * InRead format within a WebView
 */
class MoPubWebViewFragment : BaseFragment(), SyncAdWebView.Listener {

    private lateinit var adView: MoPubView
    private lateinit var mListener: TeadsAdapterListener
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
        // 1. Teads Helper
        TeadsHelper.initialize()

        // 2. Create WebViewHelper
        webviewHelperSynch = SyncAdWebView(requireContext(), webview, this@MoPubWebViewFragment, "#teads-placement-slot")

        // 3. Create MoPub view, setup and register it
        adView = MoPubView(activity)

        webviewHelperSynch.registerAdView(adView)
        adView.layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        adView.setAdUnitId(MoPubIdentifier.getAdUnitFromPid(pid))
        adView.autorefreshEnabled = false
        adView.adSize = MoPubView.MoPubAdSize.HEIGHT_90

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
        // 5. Attach and Register its key in the helper
        val key = TeadsHelper.attachListener(mListener)

        // 6. Create the TeadsMediationSettings to customize our Teads AdView
        val extras = TeadsMediationSettings.Builder()
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

        // 7. Set the settings inside MoPub AdView
        adView.setLocalExtras(mapOf("teads" to extras.toJsonEncoded()))

        // 8. Load the ad
        adView.loadAd()
    }

    override fun getTitle(): String = "InRead MoPub WebView"
}
