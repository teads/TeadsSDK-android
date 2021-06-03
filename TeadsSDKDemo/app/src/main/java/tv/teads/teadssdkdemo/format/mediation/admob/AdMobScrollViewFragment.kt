package tv.teads.teadssdkdemo.format.mediation.admob

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.fragment_inread_scrollview.*
import tv.teads.adapter.admob.TeadsAdapter
import tv.teads.helper.TeadsBannerAdapterListener
import tv.teads.helper.TeadsHelper
import tv.teads.sdk.android.AdSettings
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.mediation.identifier.AdMobIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment
import kotlin.math.roundToInt

/**
 * Display inRead as Banner within a ScrollView using AdMob Mediation.
 */
class AdMobScrollViewFragment : BaseFragment() {
    private lateinit var mListener: TeadsBannerAdapterListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_inread_scrollview, container, false)

        v.findViewById<TextView>(R.id.integration_header).text = getTitle()

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1. Initialize AdMob & Teads Helper
        MobileAds.initialize(requireContext())
        TeadsHelper.initialize()

        // 2. Create AdMob view, setup and add it to view hierarchy
        val adView = AdView(view.context)
        adView.adUnitId = AdMobIdentifier.getAdUnitFromPid(pid)
        adView.adSize = AdSize.MEDIUM_RECTANGLE
        teadsAdView.addView(adView)

        // 3. Attach listener (will include Teads events)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // maybe track it on GA?
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                Toast.makeText(context, "Ad loading failed: onAdFailedToLoad(${error.cause?.message})", Toast.LENGTH_SHORT).show()
            }

            override fun onAdOpened() {
                // Nothing to do for Teads
            }

            override fun onAdClosed() {
                // Nothing to do for Teads
            }
        }

        /* 3. Create a TeadsBannerAdapterListener
        You need to create an instance for each instance of AdMob view
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

        // 4. Attach the listener to the helper and save the key
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

        // 7. Create the AdRequest with the previous settings
        val adRequest = AdRequest.Builder()
                .addCustomEventExtrasBundle(TeadsAdapter::class.java, extras.toBundle())
                .build()

        // 8. Load the ad with the AdRequest
        adView.loadAd(adRequest)
    }

    override fun getTitle(): String = "InRead AdMob ScrollView"
}
