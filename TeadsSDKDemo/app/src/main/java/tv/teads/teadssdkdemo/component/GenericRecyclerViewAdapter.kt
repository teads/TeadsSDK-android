package tv.teads.teadssdkdemo.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.data.RecyclerItemType

open class GenericRecyclerViewAdapter(private val mTitle: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> RecyclerItemType.TYPE_SCROLL_DOWN.value
            1 -> RecyclerItemType.TYPE_ARTICLE_TITLE.value
            2 -> RecyclerItemType.TYPE_ARTICLE_REAL_LINES.value
            6 -> RecyclerItemType.TYPE_TEADS.value
            else -> RecyclerItemType.TYPE_ARTICLE_FAKE_LINES.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecyclerItemType.TYPE_SCROLL_DOWN.value -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.article_header_row, parent, false)

                v.findViewById<TextView>(R.id.integration_header).text = mTitle
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
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.article_fake_lines, parent, false)
                ViewHolderDemo(v)
            }
        }
    }

    override fun getItemCount(): Int = 8

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

    protected inner class ViewHolderDemo internal constructor(view: View) : RecyclerView.ViewHolder(view)
}