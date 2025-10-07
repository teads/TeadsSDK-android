package tv.teads.teadssdkdemo.v6.utils

import android.content.Context
import android.content.res.Configuration

/**
 * Utility class for theme-related operations
 */
object ThemeUtils {

    /**
     * Check if dark mode is currently enabled
     * @param context The context to check the current theme
     * @return true if dark mode is enabled, false otherwise
     */
    fun isDarkModeEnabled(context: Context): Boolean {
        return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }
}
