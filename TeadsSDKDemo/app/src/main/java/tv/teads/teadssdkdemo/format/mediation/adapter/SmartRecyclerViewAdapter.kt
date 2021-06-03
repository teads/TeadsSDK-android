package tv.teads.teadssdkdemo.format.mediation.adapter

import android.content.Context
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.smartadserver.android.library.model.SASAdElement
import com.smartadserver.android.library.model.SASAdPlacement
import com.smartadserver.android.library.ui.SASBannerView
import com.smartadserver.android.library.util.SASConfiguration
import tv.teads.adapter.smart.SmartHelper
import tv.teads.helper.TeadsBannerAdapterListener
import tv.teads.helper.TeadsHelper
import tv.teads.sdk.android.AdSettings
import tv.teads.teadssdkdemo.component.GenericRecyclerViewAdapter
import tv.teads.teadssdkdemo.data.RecyclerItemType
import kotlin.math.roundToInt

class SmartRecyclerViewAdapter(context: Context, siteID: Int, pageName: String,
                               supplyChainObjectString: String, formatID: Int, title: String)
    : GenericRecyclerViewAdapter(title) {

    private val adView: SASBannerView
    private val mListener: TeadsBannerAdapterListener

    init {
        // 1. Initialize SASConfiguration & Teads Helper
        TeadsHelper.initialize()
        SASConfiguration.getSharedInstance().configure(context, siteID)

        // Enable output to Android Logcat (optional)
        SASConfiguration.getSharedInstance().isLoggingEnabled = true

        // 2. Create AdMob view, setup and add it to view hierarchy
        adView = SASBannerView(context)

        // 3. Attach listener (will include Teads events)
        adView.bannerListener = object : SASBannerView.BannerListener {
            override fun onBannerAdLoaded(p0: SASBannerView, p1: SASAdElement) {}

            override fun onBannerAdFailedToLoad(p0: SASBannerView, p1: java.lang.Exception) {}

            override fun onBannerAdClicked(p0: SASBannerView) {}

            override fun onBannerAdExpanded(p0: SASBannerView) {}

            override fun onBannerAdCollapsed(p0: SASBannerView) {}

            override fun onBannerAdResized(p0: SASBannerView) {}

            override fun onBannerAdClosed(p0: SASBannerView) {}

            override fun onBannerAdVideoEvent(p0: SASBannerView, p1: Int) {}
        }

        /* 4. Create a TeadsBannerAdapterListener
       You need to create an instance for each instance of Smart view
       it needs to be a strong reference to it, so our helper can cleanup when you don't need it anymore
        */
        mListener = object : TeadsBannerAdapterListener {
            override fun onRatioUpdated(adRatio: Float) {
                adView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        adView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        val params: ViewGroup.LayoutParams = adView.layoutParams

                        // Here the width is MATCH_PARENT
                        params.height = (adView.measuredWidth / adRatio).roundToInt()

                        adView.layoutParams = params
                    }
                })
            }
        }

        // 5. Attach the listener to the helper and save the key
        val key = TeadsHelper.attachListener(mListener)

        // 6. Create the AdSettings to customize our Teads AdView
        val extras = AdSettings.Builder()
                .enableDebug()
                .userConsent("1", "11001")
                .addAdapterListener(key)
                .pageUrl("https://page.com/article1/")
                .build()

        // 7. Use the helper to transform settings to a jsonEncoded
        val jsonEncoded = SmartHelper.getTargetFromTeadsAdSettings(extras)

        // 8. Create the SASAdPlacement using your filled information above
        val bannerPlacement = SASAdPlacement(siteID.toLong(), pageName, formatID.toLong(), jsonEncoded, supplyChainObjectString)

        // 9. Load the ad using the SASAdPlacement
        adView.loadAd(bannerPlacement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecyclerItemType.TYPE_TEADS.value -> ViewHolderDemo(adView)
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }
}