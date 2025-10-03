package tv.teads.teadssdkdemo.v6.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tv.teads.teadssdkdemo.R

class MediaScrollViewFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Use modular article scroll view template
        return inflater.inflate(R.layout.article_scroll_view_template, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupContent()
    }

    private fun setupContent() {
        // Content is already set up from article_template.xml
        // Customize ad container for ScrollView integration
        val adContainer = requireView().findViewById<ViewGroup>(R.id.ad_container)
        adContainer?.let { container ->
            // The container is ready for Teads SDK ScrollView integration
            // This is where you would add your Teads ScrollView
        }
    }
}
