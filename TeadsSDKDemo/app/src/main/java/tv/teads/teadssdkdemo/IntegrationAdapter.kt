package tv.teads.teadssdkdemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.data.IntegrationType

class IntegrationAdapter(private val mListener: OnIntegrationClickedListener,
                         mContext: Context)
    : RecyclerView.Adapter<IntegrationAdapter.IntegrationViewHolder>() {
    private val mList = listOf(
            IntegrationType("ScrollView", R.drawable.scrollview),
            IntegrationType("RecyclerView", R.drawable.tableview),
            IntegrationType("RecyclerView Grid", R.drawable.collectionview),
            IntegrationType("WebView", R.drawable.webview)
    )

    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntegrationViewHolder {
        val view = mInflater.inflate(R.layout.item_integration_type, parent, false)

        return IntegrationViewHolder(view)
    }

    override fun onBindViewHolder(holder: IntegrationViewHolder, position: Int) {
        val inte = mList[position]

        holder.text.text = inte.name
        holder.image.setImageResource(inte.image)
    }

    override fun getItemCount(): Int = mList.size

    inner class IntegrationViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.title_integration)
        val image: ImageView = itemView.findViewById(R.id.image_integration)

        init {
            itemView.setOnClickListener {
                mListener.onIntegrationClicked(adapterPosition)
            }
        }
    }

    interface OnIntegrationClickedListener {
        fun onIntegrationClicked(position: Int): Unit
    }
}