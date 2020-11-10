package tv.teads.teadssdkdemo.format.inread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_inread_scrollview.*
import tv.teads.sdk.android.AdFailedReason
import tv.teads.sdk.android.AdSettings
import tv.teads.sdk.android.InReadAdView
import tv.teads.sdk.android.TeadsListener
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.utils.BaseFragment
import kotlin.math.roundToInt

/**
 * InRead format within a ScrollView
 */
class InReadScrollViewFragment : BaseFragment() {

    private lateinit var adView: InReadAdView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_inread_scrollview, container, false)

        v.findViewById<TextView>(R.id.integration_header).text = getTitle()

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 1. Create the InReadAdView
        adView = InReadAdView(activity)

        // 2. Setup the AdView
        val settings = AdSettings.Builder()
                .enableDebug()
                .build()

        adView.setPid(pid)

        // 4. Subscribe to our listener
        adView.listener = object : TeadsListener() {
            override fun onAdFailedToLoad(reason: AdFailedReason?) {
                Toast.makeText(this@InReadScrollViewFragment.activity, getString(R.string.didfail), Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: String?) {
                Toast.makeText(this@InReadScrollViewFragment.activity, getString(R.string.didfail_playback), Toast.LENGTH_SHORT).show()
            }

            override fun onRatioUpdated(adRatio: Float) {
                val params = adView.layoutParams

                params.height = (adView.measuredWidth / adRatio).roundToInt()

                adView.layoutParams = params
            }
        }

        // 5. Load the ad with the created settings
        //    You can still load without settings.
        teadsAdView.addView(adView)
        adView.load(settings)
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.clean()
    }

    override fun getTitle(): String = "InRead Direct ScrollView"
}
