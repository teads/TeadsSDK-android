package tv.teads.teadssdkdemo.v6.ui.base.navigation

import android.content.Intent
import androidx.activity.ComponentActivity
import tv.teads.teadssdkdemo.v6.ui.base.IntegrationActivity
import tv.teads.teadssdkdemo.v6.ui.base.MainActivityV6
import tv.teads.teadssdkdemo.v6.ui.base.utils.AnimationHelper

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
            Route.MediaScrollView -> navigateToIntegrationActivity(fromActivity, route)
            Route.MediaRecyclerView -> navigateToIntegrationActivity(fromActivity, route)
            Route.MediaNativeScrollView -> navigateToIntegrationActivity(fromActivity, route)
            Route.MediaColumn -> navigateToIntegrationActivity(fromActivity, route)
            else -> throw IllegalAccessException("Impossible route")
        }
    }

    /**
     * Navigate to Integration Activity with specific route
     */
    private fun navigateToIntegrationActivity(fromActivity: ComponentActivity, route: Route) {
        IntegrationActivity.launch(fromActivity, route)
        AnimationHelper.applySlideOpenTransition(fromActivity)
    }

    /**
     * Navigate back to Demo from any activity
     */
    fun navigateBackToDemo(fromActivity: ComponentActivity) {
        fromActivity.finish()
        AnimationHelper.applySlideCloseTransition(fromActivity)
    }
}
