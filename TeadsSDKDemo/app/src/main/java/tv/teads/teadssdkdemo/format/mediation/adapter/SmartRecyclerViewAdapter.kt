package tv.teads.teadssdkdemo.format.mediation.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartadserver.android.library.model.SASAdPlacement
import com.smartadserver.android.library.ui.SASBannerView
import com.smartadserver.android.library.util.SASConfiguration
import tv.teads.sdk.TeadsMediationSettings
import tv.teads.teadssdkdemo.component.GenericRecyclerViewAdapter
import tv.teads.teadssdkdemo.data.RecyclerItemType

/**
 * Simple RecyclerView adapter
 */
class SmartRecyclerViewAdapter(context: Context?, title: String)
    : GenericRecyclerViewAdapter(title) {
    private val supplyChainObjectString: String = "" // "1.0,1!exchange1.com,1234,1,publisher,publisher.com";
    private val siteID = 385317L
    private val pageName = "1399205"
    private val formatID = 96445L

    private val smartAdView: SASBannerView

    init {
        smartAdView = SASBannerView(context!!)

        // First of all, configure the SDK
        SASConfiguration.getSharedInstance().configure(context, siteID.toInt())

        // Enable output to Android Logcat (optional)
        SASConfiguration.getSharedInstance().isLoggingEnabled = true

        val settings = TeadsMediationSettings.Builder()
            .build()

        val bannerPlacement = SASAdPlacement(siteID, pageName, formatID, "teadsAdSettingsKey=${settings.toJsonEncoded()}", supplyChainObjectString)

        smartAdView.loadAd(bannerPlacement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecyclerItemType.TYPE_TEADS.value -> ViewHolderDemo(smartAdView)
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }
}
