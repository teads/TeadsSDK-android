package tv.teads.teadssdkdemo.v6.ui.base.utils

import android.app.Activity

/**
 * Helper class for handling activity transition animations
 * Provides backward compatibility with deprecated overridePendingTransition
 */
object AnimationHelper {
    
    /**
     * Apply fade transition animation for activity open
     * @param activity the activity to apply transition to
     */
    fun applyFadeOpenTransition(activity: Activity) {
        applyTransition(activity, Activity.OVERRIDE_TRANSITION_OPEN)
    }
    
    /**
     * Apply fade transition animation for activity close
     * @param activity the activity to apply transition to
     */
    fun applyFadeCloseTransition(activity: Activity) {
        applyTransition(activity, Activity.OVERRIDE_TRANSITION_CLOSE)
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
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                0 // synchronized transition
            )
        } else {
            @Suppress("DEPRECATION")
            activity.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }
    }
}
