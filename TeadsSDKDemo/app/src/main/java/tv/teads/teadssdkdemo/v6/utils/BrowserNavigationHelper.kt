package tv.teads.teadssdkdemo.v6.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri

/**
 * Utility class for handling browser navigation
 */
object BrowserNavigationHelper {
    const val TAG = "BrowserNavigationHelper"

    /**
     * Open URL in external browser
     * @param context The context to start the intent
     * @param url The URL to open
     */
    fun openInnerBrowser(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            Log.d(TAG, "Opened URL in browser: $url")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open URL: $url", e)
        }
    }
}
