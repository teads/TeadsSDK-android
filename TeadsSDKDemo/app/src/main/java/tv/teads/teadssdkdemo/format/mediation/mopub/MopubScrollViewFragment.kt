package tv.teads.teadssdkdemo.format.mediation.mopub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import com.mopub.mobileads.MoPubView
import kotlinx.android.synthetic.main.fragment_inread_scrollview.*
import tv.teads.helper.TeadsBannerAdapterListener
import tv.teads.helper.TeadsHelper
import tv.teads.sdk.android.AdSettings
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.mediation.identifier.MoPubIdentifier.MOPUB_ID
import tv.teads.teadssdkdemo.utils.BaseFragment
import kotlin.math.roundToInt

/**
 * Display inRead as Banner within a ScrollView using AdMob Mediation.
 */
class MopubScrollViewFragment : BaseFragment() {
    private lateinit var mMopubView: MoPubView
    private lateinit var mListener: TeadsBannerAdapterListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_scrollview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1. Initialize Teads Helper
        TeadsHelper.initialize()

        initializeSdk()
    }

    private fun initializeSdk() {
        // 2. Create MoPub view, setup and add it to view hierarchy
        mMopubView = MoPubView(activity)

        mMopubView.adUnitId = MOPUB_ID
        mMopubView.autorefreshEnabled = false
        mMopubView.adSize = MoPubView.MoPubAdSize.HEIGHT_90
        teadsAdView.addView(mMopubView)

        /* 3. Create a TeadsBannerAdapterListener
        You need to create an instance for each instance of AdMob view
        it needs to be a strong reference to it, so our helper can cleanup when you don't need it anymore
         */
        mListener = object : TeadsBannerAdapterListener {
            override fun onRatioUpdated(adRatio: Float) {
                val params: ViewGroup.LayoutParams = mMopubView.layoutParams

                // Here the width is MATCH_PARENT
                params.height = (params.width / adRatio).roundToInt()

                mMopubView.layoutParams = params
            }
        }


        // 4. Attach the listener to the helper and save the key
        val key = TeadsHelper.attachListener(mListener)

        // 5. Create the AdSettings to customize our Teads AdView
        val extras = AdSettings.Builder()
                .enableDebug()
                .userConsent("1", "BOq832qOq832qAcABBENCxAAAAAs57_______9______9uz_Ov_v_f__33e8__9v_l_7_-___u_-33d4u_1vf99yfm1-7etr3tp_87ues2_Xur__79__3z3_9pxP78k89r7337Ew_v-_v8b7JCKN4A")
                .setUsPrivacy("1YNN")
                .addAdapterListener(key)
                .build()
        mMopubView.localExtras = extras.toHashMap()

        // 8. Load the ad
        mMopubView.loadAd()
    }

    override fun getTitle(): String = "MoPub ScrollView"
}
