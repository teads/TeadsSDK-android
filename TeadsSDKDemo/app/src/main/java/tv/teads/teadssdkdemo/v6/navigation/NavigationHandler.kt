package tv.teads.teadssdkdemo.v6.navigation

import android.content.Intent
import androidx.activity.ComponentActivity
import tv.teads.teadssdkdemo.v6.ui.MediaScrollViewActivity

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
            Route.MediaScrollView -> navigateToScrollViewActivity(fromActivity)
            Route.MediaNativeScrollView -> navigateToScrollViewActivity(fromActivity)
            Route.Default -> navigateToScrollViewActivity(fromActivity)
            else -> throw IllegalStateException("$route should be handled by MainActivityV6 as composable")
        }
    }

    /**
     * Navigate to ScrollView Activity
     */
    private fun navigateToScrollViewActivity(fromActivity: ComponentActivity) {
        val intent = Intent(fromActivity, MediaScrollViewActivity::class.java)
        fromActivity.startActivity(intent)
        // Add exit animation
        fromActivity.overridePendingTransition(
            android.R.anim.fade_in, 
            android.R.anim.fade_out
        )
    }

    /**
     * Navigate back to Demo from any activity
     */
    fun navigateBackToDemo(fromActivity: ComponentActivity) {
        val intent = Intent(fromActivity, tv.teads.teadssdkdemo.v6.ui.MainActivityV6::class.java)
        fromActivity.startActivity(intent)
        fromActivity.finish()
        fromActivity.overridePendingTransition(
            android.R.anim.fade_in, 
            android.R.anim.fade_out
        )
    }
}
