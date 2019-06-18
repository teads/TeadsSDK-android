package tv.teads.teadssdkdemo.format.mediation

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.mopub.mobileads.MoPubView

import tv.teads.adapter.mopub.TeadsLocalExtras
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.mediation.MoPubRepeatableRecyclerViewAdapter.ViewHolderDemo
import tv.teads.teadssdkdemo.format.mediation.MoPubRepeatableRecyclerViewAdapter.ViewHolderTeadsAd

/**
 * Manage a repeatable ad for a Recycler view with the MoPub mediation,
 * It will display the same ad view every [MoPubRepeatableRecyclerViewAdapter.AD_INTERVAL] items
 * Created by Benjamin Volland on 15/03/2019.
 */
class MoPubRepeatableRecyclerViewAdapter internal constructor(context: Context?, private val mDataset: List<String>, moPubId: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val moPubView: MoPubView = MoPubView(context)

    init {
        moPubView.adUnitId = moPubId
        val teadsLocalExtras = TeadsLocalExtras.Builder()
                .enableDebug()
                .userConsent("1", "11001")
                .adContainerId(moPubView.id)
                .build()
        moPubView.localExtras = teadsLocalExtras.extras
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % AD_INTERVAL == 9) TYPE_MOPUB_AD else TYPE_TEXT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MOPUB_AD -> ViewHolderTeadsAd(moPubView)
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
            TYPE_MOPUB_AD -> {
            }
            TYPE_TEXT -> (holder as ViewHolderDemo).textView.text = (position - position / 10).toString()
        }// loading is already done before hand
    }

    override fun getItemCount(): Int {
        return mDataset.size + mDataset.size / AD_INTERVAL
    }

    private inner class ViewHolderTeadsAd internal constructor(view: View) : RecyclerView.ViewHolder(view)

    private inner class ViewHolderDemo internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal val textView: TextView = view.findViewById(R.id.listViewText)

    }

    internal fun loadBanner() {
        moPubView.loadAd()
    }

    companion object {
        private val TYPE_MOPUB_AD = 0
        private val TYPE_TEXT = 1
        private val AD_INTERVAL = 10
    }
}