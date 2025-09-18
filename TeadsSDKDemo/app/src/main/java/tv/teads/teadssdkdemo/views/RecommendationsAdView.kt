package tv.teads.teadssdkdemo.views

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import com.outbrain.OBSDK.Entities.OBRecommendation
import com.outbrain.OBSDK.Entities.OBRecommendationsResponse
import com.outbrain.OBSDK.Viewability.OBFrameLayout
import com.squareup.picasso.Picasso
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementRecommendations
import tv.teads.teadssdkdemo.R

/**
 * Custom view for displaying Outbrain recommendations.
 * Extends LinearLayout and provides a bind method to populate with recommendations data.
 */
class RecommendationsAdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
    }

    /**
     * Binds the view with recommendations data and displays them.
     * 
     * @param recommendations The OBRecommendationsResponse containing the recommendations to display
     * @param articleUrl The article URL for disclosure icon click handling
     */
    fun bind(recommendations: OBRecommendationsResponse, articleUrl: Uri) {
        removeAllViews()
        
        if (recommendations.all.isNotEmpty()) {
            // Add header view
            val inflater = LayoutInflater.from(context)
            val headerView = inflater.inflate(R.layout.outbrain_classic_recommendation_header_view, this, false)
            addView(headerView)
            
            val whatIsOutbrainIV = headerView.findViewById<ImageView>(R.id.recommended_by_image)
            whatIsOutbrainIV.setOnClickListener {
                openURLInBrowser(TeadsAdPlacementRecommendations.getOutbrainAboutURL(), context)
            }
            
            // Add recommendation views
            for (item in recommendations.all) {
                addClassicRecommendationView(item, articleUrl)
            }
        }
    }
    
    private fun addClassicRecommendationView(
        recommendation: OBRecommendation,
        articleUrl: Uri
    ) {
        val inflater = LayoutInflater.from(context)
        val child = inflater.inflate(R.layout.outbrain_classic_recommendation_view, this, false)
        val recContainer = child.findViewById<View>(R.id.outbrain_rec_container) as OBFrameLayout
        val title = child.findViewById<TextView>(R.id.outbrain_layouts_title_text_label)
        val desc = child.findViewById<TextView>(R.id.outbrain_layouts_author_text_label)
        val imageView = child.findViewById<ImageView>(R.id.outbrain_rec_image_view)
        val disclosureImageView = child.findViewById<ImageView>(R.id.outbrain_rec_disclosure_image_view)
        
        if (recommendation.isPaid && recommendation.shouldDisplayDisclosureIcon()) {
            // Set the RTB disclosure icon image and click handler
            disclosureImageView.visibility = View.VISIBLE
            // Load disclosure icon using Picasso
            Picasso.get().load(recommendation.disclosure.iconUrl).into(disclosureImageView)
            disclosureImageView.setOnClickListener {
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(context, articleUrl)
            }
        } else {
            disclosureImageView.visibility = View.GONE
        }
        
        val recommendationContainer = child.findViewById<RelativeLayout>(R.id.outbrain_classic_recommendation_view_container)
        
        title.text = recommendation.content
        desc.text = "By ${recommendation.sourceName}"
        
        addView(child)
        
        // Load recommendation thumbnail using Picasso
        if (imageView != null) {
            Picasso.get().load(recommendation.thumbnail.url).into(imageView)
        }
        
        recommendationContainer.setOnClickListener {
            openURLInBrowser(TeadsAdPlacementRecommendations.getUrl(recommendation), context)
        }

        // Viewability setup
        TeadsAdPlacementRecommendations.configureViewabilityPerListingFor(recContainer, recommendation)
    }

    private fun openURLInBrowser(url: String?, context: Context) {
        url?.let {
            try {
                val customTabsIntent = CustomTabsIntent.Builder().build()
                customTabsIntent.launchUrl(context, Uri.parse(it))
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }
}

