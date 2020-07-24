package tv.teads.teadssdkdemo.format.mediation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.fragment_admob_banner.*
import org.greenrobot.eventbus.Subscribe
import tv.teads.adapter.admob.TeadsAdapter
import tv.teads.helper.TeadsBannerAdapterListener
import tv.teads.helper.TeadsHelper
import tv.teads.sdk.android.AdSettings
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.utils.BaseFragment
import tv.teads.teadssdkdemo.utils.ReloadEvent
import kotlin.math.roundToInt

/**
 * Display inRead as Banner within a ScrollView using AdMob Mediation.
 */
class AdMobBannerFragment : BaseFragment() {
    private lateinit var mListener: TeadsBannerAdapterListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_admob_banner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1. Init AdMob (could be done in your Application class)
        MobileAds.initialize(context, ADMOB_TEADS_APP_ID)
        TeadsHelper.initialize()

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

        mListener = object : TeadsBannerAdapterListener {
            override fun onRatioUpdated(adRatio: Float) {
                val params: ViewGroup.LayoutParams = adView.layoutParams

                // Here the width is MATCH_PARENT
                params.height = (params.width / adRatio).roundToInt()

                adView.layoutParams = params
            }

        }

        // 4. Attach listener to the helper and save the key
        val key = TeadsHelper.attachListener(mListener)

        // 5. Load a new ad (this will call AdMob and Teads afterward)
        val extras = AdSettings.Builder()
                // Needed by european regulation
                // See https://mobile.teads.tv/sdk/documentation/android/gdpr-consent
                .userConsent("1", "0001")
                // The article url if you are a news publisher to increase your earnings
                .pageUrl("https://page.com/article1/")
                .addAdapterListener(key)
                .build()
        val adRequest = AdRequest.Builder()
                .addCustomEventExtrasBundle(TeadsAdapter::class.java, extras.toBundle())
                .build()

        adView.loadAd(adRequest)
    }


    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onReloadEvent(event: ReloadEvent) {
        // not used
    }

    companion object {
        // FIXME This ids should be replaced by your own AdMob application and ad block ids
        val ADMOB_TEADS_APP_ID = "ca-app-pub-3068786746829754~3613028870"
        val ADMOB_TEADS_BANNER_ID = "ca-app-pub-3068786746829754/3486435166"
    }

}
