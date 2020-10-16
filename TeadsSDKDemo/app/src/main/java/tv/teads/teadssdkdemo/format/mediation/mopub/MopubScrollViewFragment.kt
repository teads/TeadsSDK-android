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
import tv.teads.teadssdkdemo.format.mediation.data.MoPubIdentifier.MOPUB_ID
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
        TeadsHelper.initialize()
        if (MoPub.isSdkInitialized()) {
            mMopubView.loadAd()
        } else {
            MoPub.initializeSdk(context!!, SdkConfiguration.Builder(MOPUB_ID).build()) {
                initializeSdk()

                mMopubView.loadAd()
            }
        }
    }

    private fun initializeSdk() {
        mMopubView = MoPubView(activity)

        mListener = object : TeadsBannerAdapterListener {
            override fun onRatioUpdated(adRatio: Float) {
                val params: ViewGroup.LayoutParams = mMopubView.layoutParams

                // Here the width is MATCH_PARENT
                params.height = (params.width / adRatio).roundToInt()

                mMopubView.layoutParams = params
            }
        }

        mMopubView.adUnitId = MOPUB_ID
        mMopubView.autorefreshEnabled = false
        mMopubView.adSize = MoPubView.MoPubAdSize.HEIGHT_90


        val key = TeadsHelper.attachListener(mListener)

        val extras = AdSettings.Builder()
                .enableDebug()
                .userConsent("1", "BOq832qOq832qAcABBENCxAAAAAs57_______9______9uz_Ov_v_f__33e8__9v_l_7_-___u_-33d4u_1vf99yfm1-7etr3tp_87ues2_Xur__79__3z3_9pxP78k89r7337Ew_v-_v8b7JCKN4A")
                .setUsPrivacy("1YNN")
                .addAdapterListener(key)
                .build()
        mMopubView.localExtras = extras.toHashMap()

        teadsAdView.addView(mMopubView)
    }

    override fun getTitle(): String = "MoPub ScrollView"
}
