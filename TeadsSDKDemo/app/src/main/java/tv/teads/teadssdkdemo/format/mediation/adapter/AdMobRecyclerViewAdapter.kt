package tv.teads.teadssdkdemo.format.mediation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*
import tv.teads.adapter.admob.TeadsAdapter
import tv.teads.helper.TeadsBannerAdapterListener
import tv.teads.helper.TeadsHelper
import tv.teads.sdk.android.AdSettings
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.data.RecyclerItemType
import tv.teads.teadssdkdemo.format.mediation.admob.AdMobRecyclerViewFragment
import tv.teads.teadssdkdemo.format.mediation.data.AdMobIdentifier.ADMOB_TEADS_APP_ID
import tv.teads.teadssdkdemo.format.mediation.data.AdMobIdentifier.ADMOB_TEADS_BANNER_ID
import kotlin.math.roundToInt

/**
 * Simple RecyclerView adapter
 */
class AdMobRecyclerViewAdapter(private val dataset: List<String>, context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val adView: AdView = AdView(context)
    private var mAdRatio: Float = 1F
    private val mListener: TeadsBannerAdapterListener

    init {
        MobileAds.initialize(context, ADMOB_TEADS_APP_ID)
        TeadsHelper.initialize()

        adView.adUnitId = ADMOB_TEADS_BANNER_ID
        adView.adSize = AdSize.MEDIUM_RECTANGLE
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                Toast.makeText(context, "Ad loading failed: onAdFailedToLoad($errorCode)", Toast.LENGTH_SHORT).show()
            }

            override fun onAdOpened() {
                // Nothing to do for Teads
            }

            override fun onAdLeftApplication() {
                // Nothing to do for Teads
            }

            override fun onAdClosed() {
                // Nothing to do for Teads
            }
        }

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

        val key = TeadsHelper.attachListener(mListener)
        val extras = AdSettings.Builder()
                // Needed by european regulation
                // See https://mobile.teads.tv/sdk/documentation/android/gdpr-consent
                .userConsent("1", "0001")
                // The article url if you are a news publisher to increase your earnings
                .pageUrl("https://page.com/article1/")
                .addAdapterListener(key)
                .build()
        val adRequest = AdRequest.Builder()
                .addCustomEventExtrasBundle(TeadsAdapter::class.java, extras.toBundle())
                .build()

        adView.loadAd(adRequest)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> RecyclerItemType.TYPE_SCROLL_DOWN.value
            1 -> RecyclerItemType.TYPE_ARTICLE_TITLE.value
            2 -> RecyclerItemType.TYPE_ARTICLE_REAL_LINES.value
            4 -> RecyclerItemType.TYPE_TEADS.value
            else -> RecyclerItemType.TYPE_ARTICLE_FAKE_LINES.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecyclerItemType.TYPE_SCROLL_DOWN.value -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.article_header_row, parent, false)
                ViewHolderDemo(v)
            }
            RecyclerItemType.TYPE_ARTICLE_TITLE.value -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.article_title_row, parent, false)
                ViewHolderDemo(v)
            }
            RecyclerItemType.TYPE_ARTICLE_REAL_LINES.value -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.article_real_lines, parent, false)
                ViewHolderDemo(v)
            }
            RecyclerItemType.TYPE_TEADS.value -> ViewHolderTeadsAd(adView)
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.article_fake_lines, parent, false)
                ViewHolderDemo(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

    override fun getItemCount(): Int = dataset.size

    private inner class ViewHolderTeadsAd internal constructor(view: View) : RecyclerView.ViewHolder(view)

    private inner class ViewHolderDemo internal constructor(view: View) : RecyclerView.ViewHolder(view)
}
