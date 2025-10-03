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
import tv.teads.teadssdkdemo.R

class MediaScrollViewFragment : Fragment() {
    
    private lateinit var contentContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_base, container, false)
        
        // Get the content container
        contentContainer = rootView.findViewById(R.id.content_container)
        
        setupContent()
        
        return rootView
    }

    private fun setupContent() {
        val linearLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(64, 64, 64, 64)
        }

        val titleText = TextView(requireContext()).apply {
            text = "Media ScrollView Integration"
            textSize = 24f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            setTextColor(context.getColor(R.color.primaryDef)) // Use theme-aware primary color
        }

        val descriptionText = TextView(requireContext()).apply {
            text = "This would contain media content in a scrollview layout"
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 32, 0, 64)
            setTextColor(context.getColor(R.color.titleTextColor)) // Use theme-aware text color
        }

        linearLayout.addView(titleText)
        linearLayout.addView(descriptionText)
        
        // Add content to the container
        contentContainer.addView(linearLayout)
    }
}
