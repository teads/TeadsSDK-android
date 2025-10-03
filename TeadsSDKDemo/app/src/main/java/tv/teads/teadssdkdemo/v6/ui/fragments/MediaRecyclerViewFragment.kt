package tv.teads.teadssdkdemo.v6.ui.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.R

class MediaRecyclerViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViewContent(view)
    }

    private fun setupRecyclerViewContent(view: View) {
        // Get the content container
        val contentContainer = view.findViewById<LinearLayout>(R.id.content_container)

        val titleText = TextView(requireContext()).apply {
            text = "Media RecyclerView Integration"
            textSize = 24f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            setPadding(64, 32, 64, 32)
            setTextColor(requireContext().getColor(R.color.primaryDef))
        }

        val descriptionText = TextView(requireContext()).apply {
            text = "This would contain media content in a recyclerview layout with sample items"
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(64, 0, 64, 32)
            setTextColor(requireContext().getColor(R.color.accent))
        }

        // Create RecyclerView
        val recyclerView = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SampleRecyclerViewAdapter(getSampleData())
            setPadding(32, 0, 32, 64)
        }

        // Add sample items to demonstrate RecyclerView
        contentContainer.addView(titleText)
        contentContainer.addView(descriptionText)
        contentContainer.addView(recyclerView)
    }

    private fun getSampleData(): List<String> {
        return listOf(
            "Sample Item 1",
            "Sample Item 2", 
            "Sample Item 3",
            "Sample Item 4",
            "Sample Item 5"
        )
    }

    /**
     * Simple RecyclerView adapter for demonstration
     */
    class SampleRecyclerViewAdapter(private val items: List<String>) : RecyclerView.Adapter<SampleViewHolder>() {
        
        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): SampleViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return SampleViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: SampleViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size
    }

    /**
     * Simple ViewHolder for demonstration
     */
    class SampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView as TextView

        fun bind(text: String) {
            textView.text = text
        }
    }
}
