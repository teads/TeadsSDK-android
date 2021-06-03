package tv.teads.teadssdkdemo.format.mediation.smart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.smartadserver.android.library.model.SASAdElement
import com.smartadserver.android.library.model.SASAdPlacement
import com.smartadserver.android.library.ui.SASBannerView
import com.smartadserver.android.library.util.SASConfiguration
import kotlinx.android.synthetic.main.fragment_inread_scrollview.*
import tv.teads.adapter.smart.SmartHelper
import tv.teads.helper.TeadsBannerAdapterListener
import tv.teads.helper.TeadsHelper
import tv.teads.sdk.android.AdSettings
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.mediation.identifier.SmartIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment
import java.lang.Exception
import kotlin.math.roundToInt

class SmartScrollViewFragment : BaseFragment() {
    private lateinit var mListener: TeadsBannerAdapterListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_inread_scrollview, container, false)

        v.findViewById<TextView>(R.id.integration_header).text = getTitle()

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1. Initialize SASConfiguration & Teads Helper
        TeadsHelper.initialize()
        SASConfiguration.getSharedInstance().configure(view.context, SmartIdentifier.SITE_ID)

        // Enable output to Android Logcat (optional)
        SASConfiguration.getSharedInstance().isLoggingEnabled = true

        // 2. Create Smart view, setup and add it to view hierarchy
        val adView = SASBannerView(view.context)
        teadsAdView.addView(adView)

        // 3. Attach listener (will include Teads events)
        adView.bannerListener = object : SASBannerView.BannerListener {
            override fun onBannerAdLoaded(banner: SASBannerView, p1: SASAdElement) {}

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

        /* 4. Create a TeadsBannerAdapterListener
        You need to create an instance for each instance of SAS view
        it needs to be a strong reference to it, so our helper can cleanup when you don't need it anymore
         */
        mListener = object : TeadsBannerAdapterListener {
            override fun onRatioUpdated(adRatio: Float) {
                val params: ViewGroup.LayoutParams = adView.layoutParams

                // Here the width is MATCH_PARENT
                params.height = (params.width / adRatio).roundToInt()

                adView.layoutParams = params
            }
        }

        // 5. Attach the listener to the helper and save the key
        val key = TeadsHelper.attachListener(mListener)

        // 6. Create the AdSettings to customize our Teads AdView
        val extras = AdSettings.Builder()
                // Needed by european regulation
                // See https://mobile.teads.tv/sdk/documentation/android/gdpr-consent
                .userConsent("1", "0001")
                // The article url if you are a news publisher to increase your earnings
                .pageUrl("https://page.com/article1/")
                // /!\ You need to add the key to the settings
                .addAdapterListener(key)
                .build()

        // 7. Use the helper to transform settings to a jsonEncoded
        val jsonEncoded = SmartHelper.getTargetFromTeadsAdSettings(extras)

        // 8. Create the SASAdPlacement using your filled information above
        val bannerPlacement = SASAdPlacement(SmartIdentifier.SITE_ID.toLong(),
                SmartIdentifier.PAGE_NAME, SmartIdentifier.getFormatFromPid(pid).toLong(),
                jsonEncoded, SmartIdentifier.SUPPLY_CHAIN)

        // 9. Load the ad using the SASAdPlacement
        adView.loadAd(bannerPlacement)
    }

    override fun getTitle(): String = "InRead Smart ScrollView"
}