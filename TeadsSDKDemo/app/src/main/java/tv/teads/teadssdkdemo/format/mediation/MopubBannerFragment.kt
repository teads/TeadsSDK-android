package tv.teads.teadssdkdemo.format.mediation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.ads.*
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import com.mopub.common.SdkInitializationListener
import com.mopub.mobileads.MoPubView
import kotlinx.android.synthetic.main.fragment_mopub_banner.*
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
class MopubBannerFragment : BaseFragment() {
    private lateinit var mListener: TeadsBannerAdapterListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mopub_banner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        TeadsHelper.initialize()
        MoPub.initializeSdk(context!!, SdkConfiguration.Builder(MOPUB_ID).build()) {
            mMopubView.loadAd()
        }

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

        val key = TeadsHelper.attachListener(mListener)

        val extras = AdSettings.Builder()
                .userConsent("1", "0001")
                .pageUrl("https://page.com/article1/")
                .addAdapterListener(key)
                .build()
        mMopubView.localExtras = extras.toHashMap()
    }


    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onReloadEvent(event: ReloadEvent) {
        // not used
    }

    companion object {
        val MOPUB_ID = "8678f92af2814e608191dbdf46efa081"
    }

}
