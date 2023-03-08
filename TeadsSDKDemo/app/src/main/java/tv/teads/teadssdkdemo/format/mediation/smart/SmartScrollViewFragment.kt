package tv.teads.teadssdkdemo.format.mediation.smart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smartadserver.android.library.model.SASAdPlacement
import com.smartadserver.android.library.ui.SASBannerView
import com.smartadserver.android.library.util.SASConfiguration
import kotlinx.android.synthetic.main.fragment_inread_scrollview.*
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.utils.BaseFragment

class SmartScrollViewFragment : BaseFragment() {
    private val supplyChainObjectString: String = "" // "1.0,1!exchange1.com,1234,1,publisher,publisher.com";
    private val siteID = 385317L
    private val pageName = "1399205"
    private val formatID = 96445L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_inread_scrollview, container, false)

        v.findViewById<TextView>(R.id.integration_header).text = getTitle()

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val smartAdView = SASBannerView(requireContext())

        // First of all, configure the SDK
        SASConfiguration.getSharedInstance().configure(requireContext(), siteID.toInt())

        // Enable output to Android Logcat (optional)
        SASConfiguration.getSharedInstance().isLoggingEnabled = true

        val settings = TeadsMediationSettings.Builder()
            .build()

        val bannerPlacement = SASAdPlacement(siteID, pageName, formatID, "teadsAdSettingsKey=${settings.toJsonEncoded()}", supplyChainObjectString)

        smartAdView.loadAd(bannerPlacement)

        adSlotView.addView(smartAdView)
    }

    override fun getTitle(): String = "InRead Smart ScrollView"
}