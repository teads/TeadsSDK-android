package tv.teads.teadssdkdemo.v6.ui.base.navigation

import androidx.fragment.app.Fragment
import tv.teads.teadssdkdemo.v6.domain.FormatType
import tv.teads.teadssdkdemo.v6.domain.IntegrationType
import tv.teads.teadssdkdemo.v6.domain.ProviderType
import tv.teads.teadssdkdemo.v6.ui.xml.MediaNativeScrollViewFragment
import tv.teads.teadssdkdemo.v6.ui.xml.MediaNativeRecyclerViewFragment
import tv.teads.teadssdkdemo.v6.ui.xml.MediaScrollViewFragment
import tv.teads.teadssdkdemo.v6.ui.xml.MediaRecyclerViewFragment

/**
 * Sealed class representing all possible navigation routes
 */
sealed class Route {
    data object Demo : Route()
    data object MediaScrollView : Route()
    data object MediaRecyclerView : Route()
    data object MediaColumn : Route()
    data object MediaLazyColumn : Route()
    data object MediaNativeScrollView : Route()
    data object MediaNativeRecyclerView : Route()
    data object MediaNativeColumn : Route()
    data object MediaNativeLazyColumn : Route()
    data object FeedScrollView : Route()
    data object FeedRecyclerView : Route()
    data object FeedColumn : Route()
    data object FeedLazyColumn : Route()
    data object RecommendationsScrollView : Route()
    data object RecommendationsRecyclerView : Route()
    data object RecommendationsColumn : Route()
    data object RecommendationsLazyColumn : Route()
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
            format == FormatType.MEDIANATIVE && provider == ProviderType.DIRECT -> {
                when (integration) {
                    IntegrationType.SCROLLVIEW -> Route.MediaNativeScrollView
                    IntegrationType.RECYCLERVIEW -> Route.MediaNativeRecyclerView
                    IntegrationType.COLUMN -> Route.MediaNativeColumn
                    IntegrationType.LAZYCOLUMN -> Route.MediaNativeLazyColumn
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
        Route.MediaNativeScrollView -> MediaNativeScrollViewFragment::class.java
        Route.MediaNativeRecyclerView -> MediaNativeRecyclerViewFragment::class.java
        else -> throw IllegalArgumentException("No fragment defined for route: $this")
    }
}

fun Route.getFragmentTag(): String = this.getTitle().filter { !it.isWhitespace() }

fun String.getRouteFromTag(): Route {
    return when (this) {
        "MediaScrollViewFragment" -> Route.MediaScrollView
        "MediaRecyclerViewFragment" -> Route.MediaRecyclerView
        "MediaNativeScrollViewFragment" -> Route.MediaNativeScrollView
        "MediaNativeRecyclerViewFragment" -> Route.MediaNativeRecyclerView
        else -> throw IllegalArgumentException("No fragment found for tag: $this")
    }
}

fun Route.getTitle(): String {
    return when (this) {
        Route.Demo -> "Teads SDK Demo"
        Route.MediaScrollView -> "Media ScrollView Fragment"
        Route.MediaRecyclerView -> "Media RecyclerView Fragment"
        Route.MediaColumn -> "Media Column Screen"
        Route.MediaLazyColumn -> "Media LazyColumn Screen"
        Route.MediaNativeScrollView -> "Media Native ScrollView Fragment"
        Route.MediaNativeRecyclerView -> "Media Native RecyclerView Fragment"
        Route.MediaNativeColumn -> "Media Native Column Screen"
        Route.MediaNativeLazyColumn -> "Media Native LazyColumn Screen"
        Route.FeedScrollView -> "Feed ScrollView Fragment"
        Route.FeedRecyclerView -> "Feed RecyclerView Fragment"
        Route.FeedColumn -> "Feed Column Screen"
        Route.FeedLazyColumn -> "Feed LazyColumn Screen"
        Route.RecommendationsScrollView -> "Recommendations ScrollView Fragment"
        Route.RecommendationsRecyclerView -> "Recommendations RecyclerView Fragment"
        Route.RecommendationsColumn -> "Recommendations Column Screen"
        Route.RecommendationsLazyColumn -> "Recommendations LazyColumn Screen"
    }
}