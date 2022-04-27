package tv.teads.teadssdkdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.data.IntegrationType

/**
 * Simple RecyclerView adapter
 */
class IntegrationItemAdapter(
    private val items: List<IntegrationType>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<IntegrationItemAdapter.IntegrationItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntegrationItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_integration_type,
            parent,
            false
        )

        return IntegrationItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: IntegrationItemViewHolder, position: Int) {
        val integrationType = items[position]

        with (holder) {
            title.text = integrationType.name
            image.setImageResource(integrationType.image)

            itemView.setOnClickListener { onClick(position) }
        }
    }

    override fun getItemCount(): Int = items.size

    class IntegrationItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_integration)
        val image: ImageView = view.findViewById(R.id.image_integration)
    }
}
