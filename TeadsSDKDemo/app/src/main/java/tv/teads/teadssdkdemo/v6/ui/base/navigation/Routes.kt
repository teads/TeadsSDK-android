package tv.teads.teadssdkdemo.v6.ui.base.navigation

import androidx.fragment.app.Fragment
import tv.teads.teadssdkdemo.v6.domain.FormatType
import tv.teads.teadssdkdemo.v6.domain.IntegrationType
import tv.teads.teadssdkdemo.v6.domain.ProviderType
import tv.teads.teadssdkdemo.v6.ui.xml.FeedRecyclerViewFragment
import tv.teads.teadssdkdemo.v6.ui.xml.FeedScrollViewFragment
import tv.teads.teadssdkdemo.v6.ui.xml.MediaAdmobScrollViewFragment
import tv.teads.teadssdkdemo.v6.ui.xml.MediaNativeAdmobScrollViewFragment
import tv.teads.teadssdkdemo.v6.ui.xml.MediaNativeRecyclerViewFragment
import tv.teads.teadssdkdemo.v6.ui.xml.MediaNativeScrollViewFragment
import tv.teads.teadssdkdemo.v6.ui.xml.MediaRecyclerViewFragment
import tv.teads.teadssdkdemo.v6.ui.xml.MediaScrollViewFragment
import tv.teads.teadssdkdemo.v6.ui.xml.RecommendationsRecyclerViewFragment
import tv.teads.teadssdkdemo.v6.ui.xml.RecommendationsScrollViewFragment

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
    data object MediaAdMobScrollView : Route()
    data object MediaNativeAdMobScrollView : Route()
    data object MediaAdMobColumn : Route()
    data object MediaNativeAdMobColumn : Route()
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
            format == FormatType.FEED && provider == ProviderType.DIRECT -> {
                when (integration) {
                    IntegrationType.SCROLLVIEW -> Route.FeedScrollView
                    IntegrationType.RECYCLERVIEW -> Route.FeedRecyclerView
                    IntegrationType.COLUMN -> Route.FeedColumn
                    IntegrationType.LAZYCOLUMN -> Route.FeedLazyColumn
                }
            }
            format == FormatType.RECOMMENDATIONS && provider == ProviderType.DIRECT -> {
                when (integration) {
                    IntegrationType.SCROLLVIEW -> Route.RecommendationsScrollView
                    IntegrationType.RECYCLERVIEW -> Route.RecommendationsRecyclerView
                    IntegrationType.COLUMN -> Route.RecommendationsColumn
                    IntegrationType.LAZYCOLUMN -> Route.RecommendationsLazyColumn
                }
            }
            format == FormatType.MEDIA && provider == ProviderType.ADMOB -> {
                when (integration) {
                    IntegrationType.SCROLLVIEW -> Route.MediaAdMobScrollView
                    IntegrationType.COLUMN -> Route.MediaAdMobColumn
                    else -> throw IllegalAccessException("Impossible route")
                }
            }
            format == FormatType.MEDIANATIVE && provider == ProviderType.ADMOB -> {
                when (integration) {
                    IntegrationType.SCROLLVIEW -> Route.MediaNativeAdMobScrollView
                    IntegrationType.COLUMN -> Route.MediaNativeAdMobColumn
                    else -> throw IllegalAccessException("Impossible route")
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
        Route.MediaAdMobScrollView -> MediaAdmobScrollViewFragment::class.java
        Route.MediaNativeAdMobScrollView -> MediaNativeAdmobScrollViewFragment::class.java
        Route.FeedScrollView -> FeedScrollViewFragment::class.java
        Route.FeedRecyclerView -> FeedRecyclerViewFragment::class.java
        Route.RecommendationsScrollView -> RecommendationsScrollViewFragment::class.java
        Route.RecommendationsRecyclerView -> RecommendationsRecyclerViewFragment::class.java
        else -> throw IllegalArgumentException("No fragment defined for route: $this")
    }
}

fun Route.getFragmentTag(): String = this.getTitle().filter { !it.isWhitespace() }

fun String.getRouteFromTag(): Route {
    return when (this) {
        "MediaScrollView" -> Route.MediaScrollView
        "MediaRecyclerView" -> Route.MediaRecyclerView
        "MediaNativeScrollView" -> Route.MediaNativeScrollView
        "MediaNativeRecyclerView" -> Route.MediaNativeRecyclerView
        "MediaAdMobScrollView" -> Route.MediaAdMobScrollView
        "MediaNativeAdMobScrollView" -> Route.MediaNativeAdMobScrollView
        "MediaNativeAdMobColumn" -> Route.MediaNativeAdMobColumn
        "FeedScrollView" -> Route.FeedScrollView
        "FeedRecyclerView" -> Route.FeedRecyclerView
        "RecommendationsScrollView" -> Route.RecommendationsScrollView
        "RecommendationsRecyclerView" -> Route.RecommendationsRecyclerView
        else -> throw IllegalArgumentException("No fragment found for tag: $this")
    }
}

fun Route.getTitle(): String {
    return when (this) {
        Route.Demo -> "Teads SDK Demo"
        Route.MediaScrollView -> "Media ScrollView"
        Route.MediaRecyclerView -> "Media RecyclerView"
        Route.MediaColumn -> "Media Column"
        Route.MediaLazyColumn -> "Media LazyColumn"
        Route.MediaNativeScrollView -> "Media Native ScrollView"
        Route.MediaNativeRecyclerView -> "Media Native RecyclerView"
        Route.MediaNativeColumn -> "Media Native Column"
        Route.MediaNativeLazyColumn -> "Media Native LazyColumn"
        Route.MediaAdMobColumn -> "Media AdMob Column"
        Route.MediaNativeAdMobScrollView -> "Media Native AdMob ScrollView"
        Route.MediaNativeAdMobColumn -> "Media Native AdMob Column"
        Route.FeedScrollView -> "Feed ScrollView"
        Route.FeedRecyclerView -> "Feed RecyclerView"
        Route.FeedColumn -> "Feed Column"
        Route.FeedLazyColumn -> "Feed LazyColumn"
        Route.RecommendationsScrollView -> "Recommendations ScrollView"
        Route.RecommendationsRecyclerView -> "Recommendations RecyclerView"
        Route.RecommendationsColumn -> "Recommendations Column"
        Route.RecommendationsLazyColumn -> "Recommendations LazyColumn"
        Route.MediaAdMobScrollView -> "Media AdMob ScrollView"
    }
}