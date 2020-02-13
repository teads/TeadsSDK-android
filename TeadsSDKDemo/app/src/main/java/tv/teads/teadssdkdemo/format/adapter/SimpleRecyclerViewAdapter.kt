package tv.teads.teadssdkdemo.format.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import tv.teads.sdk.android.InReadAdView
import tv.teads.teadssdkdemo.R

/**
 * Simple RecyclerView adapter
 */
class SimpleRecyclerViewAdapter(private val dataset: List<String>, private val adPosition: Int, context: Context?, pid: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val adView: InReadAdView = InReadAdView(context)

    /**
     * Your ad container
     */
    private lateinit var adContainer: ViewGroup

    init {
        adView.setPid(pid)
        adView.enableDebug()
        adView.load()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == adPosition) TYPE_TEADS else TYPE_TEXT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TEADS -> {
                adContainer = parent
                adView.setAdContainerView(adContainer)
                ViewHolderTeadsAd(adView)
            }
            TYPE_TEXT -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
                ViewHolderDemo(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
                ViewHolderDemo(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_TEADS -> {
            }
            TYPE_TEXT -> (holder as ViewHolderDemo).textView.text =
                    dataset[if (adPosition in 1 until position) position - 1 else position]
        }// loading is already done before hand
    }

    override fun getItemCount(): Int {
        return dataset.size + if (adPosition >= 0) 1 else 0
    }

    fun reloadAd() {
        adView.setAdContainerView(adContainer)
        adView.load()
    }

    private inner class ViewHolderTeadsAd internal constructor(view: View) : RecyclerView.ViewHolder(view)

    private inner class ViewHolderDemo internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.listViewText)
    }

    companion object {
        private val TYPE_TEADS = 0
        private val TYPE_TEXT = 1
    }
}
