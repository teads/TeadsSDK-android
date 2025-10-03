package tv.teads.teadssdkdemo.v6.navigation

import tv.teads.teadssdkdemo.v6.domain.FormatType
import tv.teads.teadssdkdemo.v6.domain.IntegrationType
import tv.teads.teadssdkdemo.v6.domain.ProviderType

/**
 * Sealed class representing all possible navigation routes
 */
sealed class Route {
    /**
     * Demo configuration screen
     */
    data object Demo : Route()
    
    /**
     * Media content in ScrollView integration
     */
    data object MediaScrollView : Route()
    
    /**
     * Media content in Column integration  
     */
    data object MediaColumn : Route()
    
    /**
     * Media Native content in ScrollView integration
     */
    data object MediaNativeScrollView : Route()
    
    /**
     * Media Native content in Column integration
     */
    data object MediaNativeColumn : Route()
    
    /**
     * Feed content in Column integration
     */
    data object FeedColumn : Route()
    
    /**
     * Feed content in LazyColumn integration
     */
    data object FeedLazyColumn : Route()
    
    /**
     * Recommendations content in Column integration
     */
    data object RecommendationsColumn : Route()
    
    /**
     * Recommendations content in RecyclerView integration
     */
    data object RecommendationsRecyclerView : Route()
    
    /**
     * Default fallback route
     */
    data object Default : Route()
}

/**
 * Route factory that creates routes based on configuration
 */
object RouteFactory {
    
    /**
     * Create route based on format, provider and integration configuration
     */
    fun createRoute(
        format: FormatType,
        provider: ProviderType,
        integration: IntegrationType
    ): Route {
        return when {
            format == FormatType.MEDIA && provider == ProviderType.DIRECT -> {
                when (integration) {
                    IntegrationType.SCROLLVIEW -> Route.MediaScrollView
                    IntegrationType.COLUMN -> Route.MediaColumn
                    else -> throw IllegalAccessException("Impossible route")
                }
            }
//            format == FormatType.MEDIANATIVE && provider == ProviderType.DIRECT -> {
//                when (integration) {
//                    IntegrationType.SCROLLVIEW -> Route.MediaNativeScrollView
//                    IntegrationType.COLUMN -> Route.MediaNativeColumn
//                    IntegrationType.LAZYCOLUMN -> Route.MediaNativeColumn
//                    IntegrationType.RECYCLERVIEW -> Route.MediaNativeScrollView
//                }
//            }
//            format == FormatType.FEED && provider == ProviderType.DIRECT -> {
//                when (integration) {
//                    IntegrationType.COLUMN -> Route.FeedColumn
//                    IntegrationType.LAZYCOLUMN -> Route.FeedLazyColumn
//                    else -> Route.FeedColumn  // Fallback for SCROLLVIEW/RECYCLERVIEW
//                }
//            }
//            format == FormatType.RECOMMENDATIONS && provider == ProviderType.DIRECT -> {
//                when (integration) {
//                    IntegrationType.COLUMN -> Route.RecommendationsColumn
//                    IntegrationType.LAZYCOLUMN -> Route.RecommendationsColumn
//                    IntegrationType.RECYCLERVIEW -> Route.RecommendationsRecyclerView
//                    else -> Route.RecommendationsColumn  // Fallback for SCROLLVIEW
//                }
//            }
            else -> Route.Default
        }
    }
}
