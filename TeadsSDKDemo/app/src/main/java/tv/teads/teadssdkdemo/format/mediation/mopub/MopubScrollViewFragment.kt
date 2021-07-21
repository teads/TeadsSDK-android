package tv.teads.teadssdkdemo.format.mediation.mopub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mopub.mobileads.MoPubView
import kotlinx.android.synthetic.main.fragment_inread_scrollview.*
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.sdk.mediation.TeadsAdapterListener
import tv.teads.sdk.mediation.TeadsHelper
import tv.teads.sdk.utils.userConsent.TCFVersion
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.mediation.identifier.MoPubIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment

/**
 * Display inRead as Banner within a ScrollView using AdMob Mediation.
 */
class MopubScrollViewFragment : BaseFragment() {
    private lateinit var mMopubView: MoPubView
    private lateinit var mListener: TeadsAdapterListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_inread_scrollview, container, false)

        v.findViewById<TextView>(R.id.integration_header).text = getTitle()

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1. Initialize Teads Helper
        TeadsHelper.initialize()

        initializeSdk()
    }

    private fun initializeSdk() {
        // 2. Create MoPub view, setup and add it to view hierarchy
        mMopubView = MoPubView(activity)

        mMopubView.setAdUnitId(MoPubIdentifier.getAdUnitFromPid(pid))
        mMopubView.autorefreshEnabled = false
        mMopubView.adSize = MoPubView.MoPubAdSize.HEIGHT_90
        adSlotView.addView(mMopubView)

        /* 3. Create a TeadsBannerAdapterListener
        You need to create an instance for each instance of AdMob view
        it needs to be a strong reference to it, so our helper can cleanup when you don't need it anymore
         */
        mListener = object : TeadsAdapterListener {
            override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                mMopubView.addView(trackerView)
            }

            override fun onRatioUpdated(adRatio: AdRatio) {
                val params: ViewGroup.LayoutParams = mMopubView.layoutParams

                // Here the width is MATCH_PARENT
                params.height = adRatio.calculateHeight(mMopubView.measuredWidth)

                mMopubView.layoutParams = params
            }
        }


        // 4. Attach the listener to the helper and save the key
        val key = TeadsHelper.attachListener(mListener)

        // 5. Create the AdSettings to customize our Teads AdView
        val extras = TeadsMediationSettings.Builder()
                .enableDebug()
                .userConsent("1", "BOq832qOq832qAcABBENCxAAAAAs57_______9______9uz_Ov_v_f__33e8__9v_l_7_-___u_-33d4u_1vf99yfm1-7etr3tp_87ues2_Xur__79__3z3_9pxP78k89r7337Ew_v-_v8b7JCKN4A", TCFVersion.V1, 12)
                .setUsPrivacy("1YNN")
                .setMediationListenerKey(key)
                .build()
        mMopubView.setLocalExtras(mapOf("teads" to extras.toJsonEncoded()))

        // 8. Load the ad
        mMopubView.loadAd()
    }

    override fun getTitle(): String = "InRead MoPub ScrollView"
}
