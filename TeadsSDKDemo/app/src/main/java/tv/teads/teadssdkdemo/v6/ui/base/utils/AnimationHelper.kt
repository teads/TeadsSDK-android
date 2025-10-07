package tv.teads.teadssdkdemo.v6.ui.base.utils

import android.app.Activity
import tv.teads.teadssdkdemo.R

/**
 * Helper class for handling activity transition animations
 * Provides slide animations that enter from the right and exit to the right
 * Provides backward compatibility with deprecated overridePendingTransition
 */
object AnimationHelper {
    
    /**
     * Apply slide transition animation for activity open (enter from right)
     * @param activity the activity to apply transition to
     */
    fun applySlideOpenTransition(activity: Activity) {
        applyTransition(activity, 0)
    }
    
    /**
     * Apply slide transition animation for activity close (exit to right)
     * @param activity the activity to apply transition to
     */
    fun applySlideCloseTransition(activity: Activity) {
        applyTransition(activity, 1)
    }
    
    /**
     * Internal method to handle transition animation with backward compatibility
     * @param activity the activity to apply transition to
     * @param transitionType Activity.OVERRIDE_TRANSITION_OPEN or Activity.OVERRIDE_TRANSITION_CLOSE
     */
    private fun applyTransition(activity: Activity, transitionType: Int) {
        if (android.os.Build.VERSION.SDK_INT >= 34) { // Android 14+
            activity.overrideActivityTransition(
                transitionType,
                R.anim.slide_in_right,
                R.anim.slide_out_right,
                0 // synchronized transition
            )
        } else {
            @Suppress("DEPRECATION")
            activity.overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_right
            )
        }
    }
}
