package tv.teads.teadssdkdemo.format.mediation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.fragment_admob_banner.*
import org.greenrobot.eventbus.Subscribe
import tv.teads.adapter.admob.TeadsAdNetworkExtras
import tv.teads.adapter.admob.TeadsAdapter
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.ReloadEvent

/**
 * Display inRead as Banner within a ScrollView using AdMob Mediation.
 */
class AdMobBannerFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_admob_banner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1. Init AdMob (could be done in your Application class)
        MobileAds.initialize(context, ADMOB_TEADS_APP_ID)

        // 2. Create AdMob view and add it to hierarchy
        val adView = AdView(view.context)
        adView.adUnitId = ADMOB_TEADS_BANNER_ID
        adView.adSize = AdSize.MEDIUM_RECTANGLE
        bannerAdFrame.addView(adView)

        // 3. Attach listener (will include Teads events)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // maybe track it on GA?
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                Toast.makeText(context, "Ad loading failed: onAdFailedToLoad($errorCode)", Toast.LENGTH_SHORT).show()
            }

            override fun onAdOpened() {
                // Nothing to do for Teads
            }

            override fun onAdLeftApplication() {
                // Nothing to do for Teads
            }

            override fun onAdClosed() {
                // Nothing to do for Teads
            }
        }

        // 4. Load a new ad (this will call AdMob and Teads afterward)
        val extras = TeadsAdNetworkExtras.Builder()
                // Needed by european regulation
                // See https://mobile.teads.tv/sdk/documentation/android/gdpr-consent
                .userConsent("1", "0001")
                // The article url if you are a news publisher to increase your earnings
                .pageUrl("https://page.com/article1/")
                .adContainerId(bannerAdFrame.id)
                .build()
        val adRequest = AdRequest.Builder()
                .addCustomEventExtrasBundle(TeadsAdapter::class.java, extras.extras)
                .build()

        adView.loadAd(adRequest)
    }


    @Subscribe
    fun onReloadEvent(event: ReloadEvent) {
        // not used
    }

    companion object {
        // FIXME This ids should be replaced by your own AdMob application and ad block ids
        val ADMOB_TEADS_APP_ID = "ca-app-pub-3570580224725271~3869556230"
        val ADMOB_TEADS_BANNER_ID = "ca-app-pub-3570580224725271/1481793511"
    }

}
