package tv.teads.teadssdkdemo.v6.ui.base.recommendations

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
import tv.teads.teadssdkdemo.R
import com.squareup.picasso.Picasso
import tv.teads.sdk.combinedsdk.adplacement.TeadsAdPlacementRecommendations
import tv.teads.teadssdkdemo.v6.utils.BrowserNavigationHelper

/**
 * Custom view for displaying recommendations.
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
    fun bind(
        recommendations: OBRecommendationsResponse,
        articleUrl: Uri
    ) {
        removeAllViews()
        
        if (recommendations.all.isNotEmpty()) {
            // 0. Inflate your custom layout
            val inflater = LayoutInflater.from(context)
            val headerView = inflater.inflate(R.layout.recommendation_header_view, this, false)
            addView(headerView)

            // 1. Define click for ad choices icon using url returned by TeadsAdPlacementRecommendations.getAdChoicesURL()
            val adChoicesIcon = headerView.findViewById<ImageView>(R.id.rec_ad_choices_icon)
            adChoicesIcon.setOnClickListener {
                BrowserNavigationHelper.openInnerBrowser(
                    context = this.context,
                    url = TeadsAdPlacementRecommendations.getAdChoicesURL()
                )
            }
            
            // 2. Iterate each item of the ad response
            for (item in recommendations.all) {
                addClassicRecommendationView(item, articleUrl)
            }
        }
    }
    
    private fun addClassicRecommendationView(
        recommendation: OBRecommendation,
        articleUrl: Uri
    ) {
        // 3. Inflate your custom layout for ad items
        val inflater = LayoutInflater.from(context)
        val child = inflater.inflate(R.layout.recommendation_view, this, false)
        val recContainer = child.findViewById<View>(R.id.rec_container) as OBFrameLayout
        val title = child.findViewById<TextView>(R.id.rec_layouts_title_text_label)
        val desc = child.findViewById<TextView>(R.id.rec_layouts_author_text_label)
        val imageView = child.findViewById<ImageView>(R.id.rec_image_view)
        val disclosureImageView = child.findViewById<ImageView>(R.id.rec_disclosure_image_view)

        // 4. Set the RTB disclosure icon image and click handler
        if (recommendation.isPaid() && recommendation.shouldDisplayDisclosureIcon()) {
            disclosureImageView.visibility = View.VISIBLE
            Picasso.get().load(recommendation.getDisclosure()?.iconUrl).into(disclosureImageView)
            disclosureImageView.setOnClickListener {
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(context, articleUrl)
            }
        } else {
            disclosureImageView.visibility = View.GONE
        }

        // 5. Add items to container
        val recommendationContainer = child.findViewById<RelativeLayout>(R.id.recommendation_view_container)
        title.text = recommendation.getContent()
        desc.text = "By ${recommendation.getSourceName()}"
        addView(child)
        
        // 6. Load media with the image loading library of your preference
        if (imageView != null) {
            Picasso.get().load(recommendation.getThumbnail()?.url).into(imageView)
        }

        // 7. Implement ad click getting the url using TeadsAdPlacementRecommendations.getUrl(recommendation: OBRecommendation)
        recommendationContainer.setOnClickListener {
            TeadsAdPlacementRecommendations.getUrl(recommendation)?.let { url ->
                BrowserNavigationHelper.openInnerBrowser(this.context, url)
            }
        }

        // 8. Setup the viewability for each recommendation using
        // TeadsAdPlacementRecommendations.configureViewabilityPerListingFor(viewContainer: OBFrameLayout, rec: OBRecommendation)
        TeadsAdPlacementRecommendations.configureViewabilityPerListingFor(recContainer, recommendation)
    }
}
