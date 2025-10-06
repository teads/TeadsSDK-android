package tv.teads.teadssdkdemo.v6.ui.base.navigation

import androidx.fragment.app.Fragment
import tv.teads.teadssdkdemo.v6.domain.FormatType
import tv.teads.teadssdkdemo.v6.domain.IntegrationType
import tv.teads.teadssdkdemo.v6.domain.ProviderType
import tv.teads.teadssdkdemo.v6.ui.xml.MediaScrollViewFragment
import tv.teads.teadssdkdemo.v6.ui.xml.MediaRecyclerViewFragment

/**
 * Sealed class representing all possible navigation routes
 */
sealed class Route {
    data object Demo : Route()
    data object MediaScrollView : Route()
    data object MediaColumn : Route()
    data object MediaLazyColumn : Route()
    data object MediaRecyclerView : Route()
    data object MediaNativeScrollView : Route()
    data object MediaNativeColumn : Route()
    data object FeedColumn : Route()
    data object FeedLazyColumn : Route()
    data object RecommendationsColumn : Route()
    data object RecommendationsRecyclerView : Route()
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
                    IntegrationType.RECYCLERVIEW -> Route.MediaRecyclerView
                    IntegrationType.COLUMN -> Route.MediaColumn
                    IntegrationType.LAZYCOLUMN -> Route.MediaLazyColumn
                }
            }
            else -> throw IllegalAccessException("Impossible route")
        }
    }
}

/**
 * Extension functions for Route utilities
 */
fun Route.getFragmentClass(): Class<out Fragment> {
    return when (this) {
        Route.MediaScrollView -> MediaScrollViewFragment::class.java
        Route.MediaRecyclerView -> MediaRecyclerViewFragment::class.java
        // TODO: Add more fragment classes as we create them
        else -> throw IllegalArgumentException("No fragment defined for route: $this")
    }
}

fun Route.getFragmentTag(): String {
    return when (this) {
        Route.MediaScrollView -> "MediaScrollViewFragment"
        Route.MediaRecyclerView -> "MediaRecyclerViewFragment"
        // TODO: Add more fragment tags as we create them
        else -> throw IllegalArgumentException("No fragment tag defined for route: $this")
    }
}
