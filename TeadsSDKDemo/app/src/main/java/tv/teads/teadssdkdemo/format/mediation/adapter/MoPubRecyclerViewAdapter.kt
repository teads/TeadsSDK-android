package tv.teads.teadssdkdemo.format.mediation.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.mopub.mobileads.MoPubView
import tv.teads.helper.TeadsBannerAdapterListener
import tv.teads.helper.TeadsHelper
import tv.teads.sdk.android.AdSettings
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.data.RecyclerItemType
import kotlin.math.roundToInt

/**
 * Manage a repeatable ad for a Recycler view with the MoPub mediation,
 * It will display the same ad view every [MoPubRecyclerViewAdapter.AD_INTERVAL] items
 */
class MoPubRecyclerViewAdapter internal constructor(private val dataset: List<String>, moPubId: String, context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mMoPubView: MoPubView = MoPubView(context)
    private val mListener: TeadsBannerAdapterListener

    init {
        TeadsHelper.initialize()

        mMoPubView.adUnitId = moPubId

        mListener = object : TeadsBannerAdapterListener {
            override fun onRatioUpdated(adRatio: Float) {
                mMoPubView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        mMoPubView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        val params: ViewGroup.LayoutParams = mMoPubView.layoutParams

                        // Here the width is MATCH_PARENT
                        params.height = (mMoPubView.measuredWidth / adRatio).roundToInt()

                        mMoPubView.layoutParams = params
                    }
                })
            }
        }

        val key = TeadsHelper.attachListener(mListener)

        val extras = AdSettings.Builder()
                .enableDebug()
                .userConsent("1", "11001")
                .addAdapterListener(key)
                .pageUrl("https://page.com/article1/")
                .build()
        mMoPubView.localExtras = extras.toHashMap()

        mMoPubView.loadAd()
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
            RecyclerItemType.TYPE_TEADS.value -> ViewHolderTeadsAd(mMoPubView)
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