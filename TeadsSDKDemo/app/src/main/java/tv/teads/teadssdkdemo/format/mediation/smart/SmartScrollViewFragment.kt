package tv.teads.teadssdkdemo.format.mediation.smart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smartadserver.android.library.model.SASAdPlacement
import com.smartadserver.android.library.ui.SASBannerView
import com.smartadserver.android.library.util.SASConfiguration
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.databinding.FragmentInreadScrollviewBinding
import tv.teads.teadssdkdemo.utils.BaseFragment

class SmartScrollViewFragment : BaseFragment() {
    private lateinit var binding: FragmentInreadScrollviewBinding

    private val siteID = 385317L
    private val pageName = "1399205"
    private val formatID = 96445L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInreadScrollviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.integration_header).text = getTitle()

        val smartAdView = SASBannerView(requireContext())

        // First of all, configure the SDK
        SASConfiguration.getSharedInstance().configure(requireContext(), siteID.toInt())

        // Enable output to Android Logcat (optional)
        SASConfiguration.getSharedInstance().isLoggingEnabled = true

        val settings = TeadsMediationSettings.Builder()
            .build()

        val bannerPlacement = SASAdPlacement(siteID, pageName, formatID, "teadsAdSettingsKey=${settings.toJsonEncoded()}")

        smartAdView.loadAd(bannerPlacement)

        binding.adSlotContainer.addView(smartAdView)
    }

    override fun getTitle(): String = "InRead Smart ScrollView"
}