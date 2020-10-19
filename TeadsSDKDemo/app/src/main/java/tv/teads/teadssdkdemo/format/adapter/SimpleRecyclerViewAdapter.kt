package tv.teads.teadssdkdemo.format.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tv.teads.sdk.android.AdSettings
import tv.teads.sdk.android.InReadAdView
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.data.RecyclerItemType

/**
 * Simple RecyclerView adapter
 */
class SimpleRecyclerViewAdapter(context: Context?, pid: Int)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 1. Create the InReadAdView
    private val adView: InReadAdView = InReadAdView(context)

    init {
        // 2. Setup the AdView
        adView.setPid(pid)

        // 3. Create an AdSettings and setup your AdView if needed
        val settings = AdSettings.Builder()
                .enableDebug()
                .build()

        // 4. Load the ad with the created settings
        //    You can still load without settings.
        adView.load(settings)
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

    override fun getItemCount(): Int = 6

    private inner class ViewHolderTeadsAd internal constructor(view: View) : RecyclerView.ViewHolder(view)

    private inner class ViewHolderDemo internal constructor(view: View) : RecyclerView.ViewHolder(view) {}
}
