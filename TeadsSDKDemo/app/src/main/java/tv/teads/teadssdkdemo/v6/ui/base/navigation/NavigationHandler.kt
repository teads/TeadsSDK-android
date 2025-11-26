package tv.teads.teadssdkdemo.v6.ui.base.navigation

import androidx.activity.ComponentActivity
import tv.teads.teadssdkdemo.v6.ui.base.IntegrationActivity

/**
 * Navigation handler responsible for navigating to the specified route
 */
object NavigationHandler {

    /**
     * Navigate to the specified route
     * @param fromActivity The current activity calling this navigation
     * @param route The route to navigate to
     */
    fun navigateToRoute(
        fromActivity: ComponentActivity,
        route: Route
    ) {
        when (route) {
            Route.MediaScrollView,
            Route.MediaRecyclerView,
            Route.MediaNativeScrollView,
            Route.MediaNativeRecyclerView,
            Route.MediaAdMobScrollView,
            Route.MediaNativeAdMobScrollView,
            Route.MediaAppLovinScrollView,
            Route.MediaNativeAppLovinScrollView,
            Route.MediaSmartScrollView,
            Route.MediaNativeSmartScrollView,
            Route.MediaPrebidStandardScrollView,
            Route.MediaPrebidStandaloneScrollView,
            Route.FeedScrollView,
            Route.FeedRecyclerView,
            Route.RecommendationsScrollView,
            Route.RecommendationsRecyclerView,
            Route.InReadWebView -> navigateToIntegrationActivity(fromActivity, route)
            else -> throw IllegalAccessException("Impossible route")
        }
    }

    /**
     * Navigate to Integration Activity with specific route
     */
    private fun navigateToIntegrationActivity(fromActivity: ComponentActivity, route: Route) {
        IntegrationActivity.launch(fromActivity, route)
    }

    /**
     * Navigate back to Demo from any activity
     */
    fun navigateBackToDemo(fromActivity: ComponentActivity) {
        fromActivity.finish()
    }
}
