package tv.teads.teadssdkdemo.v6.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tv.teads.teadssdkdemo.R

class MediaRecyclerViewFragment : Fragment() {

    private enum class ArticleLayoutType(val layoutRes: Int) {
        IMAGE(R.layout.item_article_image),
        TITLE(R.layout.item_article_title),
        BODY(R.layout.item_article_body),
        AD_CONTAINER(R.layout.item_ad_container)
    }

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
        val recyclerView = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ArticleRecyclerViewAdapter()
        }

        view.findViewById<LinearLayout>(R.id.content_container)?.apply {
            addView(recyclerView)
        }
    }

    class ArticleRecyclerViewAdapter : RecyclerView.Adapter<ArticleViewHolder>() {

        private val articleLayoutOrder = listOf(
            ArticleLayoutType.IMAGE,
            ArticleLayoutType.TITLE,
            ArticleLayoutType.BODY,
            ArticleLayoutType.BODY,
            ArticleLayoutType.BODY,
            ArticleLayoutType.AD_CONTAINER,
            ArticleLayoutType.BODY
        )

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ArticleViewHolder {
            val linearLayout = LinearLayout(parent.context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            
            val inflater = LayoutInflater.from(parent.context)
            
            articleLayoutOrder.forEach { layoutType ->
                inflater.inflate(layoutType.layoutRes, linearLayout, true)
            }
            
            return ArticleViewHolder(linearLayout)
        }

        override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
            // No binding needed - all text comes from layouts
        }

        override fun getItemCount(): Int = 1
    }

    class ArticleViewHolder(private val containerView: LinearLayout) : RecyclerView.ViewHolder(containerView)
}