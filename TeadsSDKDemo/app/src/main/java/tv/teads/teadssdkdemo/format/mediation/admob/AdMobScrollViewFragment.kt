package tv.teads.teadssdkdemo.format.mediation.admob

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.*
import tv.teads.adapter.admob.TeadsAdapter
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.sdk.utils.userConsent.TCFVersion
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.data.SessionDataSource
import tv.teads.teadssdkdemo.databinding.FragmentInreadScrollviewBinding
import tv.teads.teadssdkdemo.format.mediation.identifier.AdMobIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * Display inRead as Banner within a ScrollView using AdMob Mediation.
 */
class AdMobScrollViewFragment : BaseFragment() {
    private lateinit var binding: FragmentInreadScrollviewBinding
    private lateinit var mListener: TeadsAdapterListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentInreadScrollviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.integration_header).text = getTitle()

        // 1. Initialize AdMob & Teads Helper
        MobileAds.initialize(requireContext())
        TeadsHelper.initialize()

        // 2. Create AdMob view, setup and add it to view hierarchy
        val adView = AdView(view.context)
        adView.adUnitId = AdMobIdentifier.getAdUnitFromPid(pid)
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE)
        binding.adSlotView.addView(adView, 0)

        // 3. Attach listener (will include Teads events)
        adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError) {
                Toast.makeText(context, "Ad loading failed: onAdFailedToLoad(${error.cause?.message})", Toast.LENGTH_SHORT).show()
            }

            override fun onAdLoaded() { }
            override fun onAdOpened() { }
            override fun onAdClosed() { }
        }

        /* 4. Create a TeadsBannerAdapterListener
        You need to create an instance for each instance of AdMob view
        it needs to be a strong reference to it, so our helper can cleanup when you don't need it anymore
         */
        mListener = object : TeadsAdapterListener {
            override fun onRatioUpdated(adRatio: AdRatio) {
                val params: ViewGroup.LayoutParams = adView.layoutParams

                // Here the width is MATCH_PARENT
                params.height = adRatio.calculateHeight(adView.measuredWidth)

                adView.layoutParams = params
            }

            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                binding.adSlotView.addView(trackerView)
            }

        }

        // 5. Attach the listener to the helper and save the key
        val key = TeadsHelper.attachListener(mListener)

        // 6. Create the AdSettings to customize our Teads AdView
        val extras = TeadsMediationSettings.Builder()
                .enableDebug()
                // Needed by european regulation
                // See https://mobile.teads.tv/sdk/documentation/android/gdpr-consent
                .userConsent("1", SessionDataSource.FAKE_GDPR_STR, TCFVersion.V2, 12)
                // The article url if you are a news publisher to increase your earnings
                .pageSlotUrl("https://page.com/article1/")
                // /!\ You need to add the key to the settings
                .setMediationListenerKey(key)
                .build()

        // 7. Create the AdRequest with the previous settings
        val adRequest = AdRequest.Builder()
                .addNetworkExtrasBundle(TeadsAdapter::class.java, extras.toBundle())
                .build()

        // 8. Load the ad with the AdRequest
        adView.loadAd(adRequest)
    }

    override fun getTitle(): String = "InRead AdMob ScrollView"
}
