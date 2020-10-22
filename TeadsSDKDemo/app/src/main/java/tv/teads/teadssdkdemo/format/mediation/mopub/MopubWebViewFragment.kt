package tv.teads.teadssdkdemo.format.mediation.mopub

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubView
import kotlinx.android.synthetic.main.fragment_inread_webview.*
import tv.teads.helper.TeadsBannerAdapterListener
import tv.teads.helper.TeadsHelper
import tv.teads.sdk.android.AdSettings
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.component.CustomGroupWebViewClient
import tv.teads.teadssdkdemo.format.mediation.identifier.MoPubIdentifier.MOPUB_ID
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.webviewhelper.SyncWebViewViewGroup
import tv.teads.webviewhelper.baseView.ObservableWrapperView
import kotlin.math.roundToInt

/**
 * Display inRead as Banner within a WebView using MoPub Mediation.
 */
class MopubWebViewFragment : BaseFragment(), SyncWebViewViewGroup.Listener {
    private lateinit var mMopubView: MoPubView
    private lateinit var mListener: TeadsBannerAdapterListener

    private lateinit var webviewHelperSynch: SyncWebViewViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (context == null)
            return

        // 1. Initialize AdMob & Teads Helper
        TeadsHelper.initialize()

        // 2. Create MoPub view and setup it
        mMopubView = MoPubView(context)

        mMopubView.adUnitId = MOPUB_ID
        mMopubView.autorefreshEnabled = false
        mMopubView.adSize = MoPubView.MoPubAdSize.HEIGHT_90
        mMopubView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)

        /* 3. Create ObservableWrapperView & SyncWebViewViewGroup instance with adview
        For a webview integration, we provide a example of tool to synchronise the ad view with the webview.
        You can find it in the webviewhelper module. {@see SyncWebViewViewGroup}
         */
        val observableWrapperView = ObservableWrapperView(context!!, mMopubView)
        webviewHelperSynch = SyncWebViewViewGroup(webview, observableWrapperView, this, "#teads-placement-slot")

        /* 4. Attach MoPub listener (will include Teads events)
        Call the helper in onBannerLoaded
         */
        mMopubView.bannerAdListener = object : MoPubView.BannerAdListener {
            override fun onBannerLoaded(banner: MoPubView?) {
                webviewHelperSynch.displayAd()
            }

            override fun onBannerExpanded(banner: MoPubView?) {}
            override fun onBannerCollapsed(banner: MoPubView?) {}
            override fun onBannerFailed(banner: MoPubView?, errorCode: MoPubErrorCode?) {}
            override fun onBannerClicked(banner: MoPubView?) {}
        }

        /* 5. Create a TeadsBannerAdapterListener
        You need to create an instance for each instance of AdMob view
        it needs to be a strong reference to it, so our helper can cleanup when you don't need it anymore
         */
        mListener = object : TeadsBannerAdapterListener {
            override fun onRatioUpdated(adRatio: Float) {
                val params: ViewGroup.LayoutParams = mMopubView.layoutParams

                // Here the width of parent is MATCH_PARENT
                params.height = ((mMopubView.parent as View).width / adRatio).roundToInt()

                // 6. You need to call updateSlot method from the helper
                // Some creative can resize by itself, to handle it we have to notify the webview helper
                // this ratio doesn't contains the footer and the header
                // To manage this behavior, a work around is to substract 0.2 to the media ratio
                webviewHelperSynch.updateSlot(adRatio - 0.2f)

                mMopubView.layoutParams = params
            }
        }

        // 7. Attach and Register its key in the helper
        val key = TeadsHelper.attachListener(mListener)

        // 8. Create the AdSettings to customize our Teads AdView
        val extras = AdSettings.Builder()
                .enableDebug()
                .userConsent("1", "BOq832qOq832qAcABBENCxAAAAAs57_______9______9uz_Ov_v_f__33e8__9v_l_7_-___u_-33d4u_1vf99yfm1-7etr3tp_87ues2_Xur__79__3z3_9pxP78k89r7337Ew_v-_v8b7JCKN4A")
                .setUsPrivacy("1YNN")
                .addAdapterListener(key)
                .build()
        mMopubView.localExtras = extras.toHashMap()

        // 9. Create a custom WebViewclient with helper in it
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = CustomGroupWebViewClient(webviewHelperSynch, getTitle())
        webview.loadUrl(this.webViewUrl)
    }

    override fun onHelperReady(adContainer: ViewGroup) {
        // 10. Load the ad
        mMopubView.loadAd()
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

    override fun getTitle(): String = "InRead MoPub WebView"
}
