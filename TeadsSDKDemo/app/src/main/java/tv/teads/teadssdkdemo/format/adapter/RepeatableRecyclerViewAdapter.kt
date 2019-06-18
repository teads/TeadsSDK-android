package tv.teads.teadssdkdemo.format.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import tv.teads.sdk.android.InReadAdView
import tv.teads.teadssdkdemo.R

/**
 * Manage a repeatable ad for a Recycler view,
 * It will display the same ad view every [RepeatableRecyclerViewAdapter.AD_INTERVAL] items
 * Created by Benjamin Volland on 22/11/2018.
 */
class RepeatableRecyclerViewAdapter(context: Context?, private val dataset: List<String>, pid: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val adView: InReadAdView  = InReadAdView(context)

    init {
        adView.setPid(pid)
        adView.enableDebug()
        adView.load()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % AD_INTERVAL == 9) TYPE_TEADS else TYPE_TEXT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TEADS -> ViewHolderTeadsAd(adView)
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
            TYPE_TEXT -> (holder as ViewHolderDemo).textView.text = (position - position / 10).toString()
        }// loading is already done before hand
    }

    override fun getItemCount(): Int {
        return dataset.size + dataset.size / AD_INTERVAL
    }

    fun reloadAd() {
        adView.load()
    }

    private inner class ViewHolderTeadsAd(view: View) : RecyclerView.ViewHolder(view) {
        val adView: InReadAdView = view as InReadAdView
    }

    private inner class ViewHolderDemo(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.listViewText)

    }

    companion object {
        private val TYPE_TEADS = 0
        private val TYPE_TEXT = 1
        private val AD_INTERVAL = 10
    }
}