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
        val target = normalizeWebUrl(url)
        if (target == null) {
            Log.w(TAG, "Ignoring browser open, unusable URL: $url")
            return
        }
        try {
            val intent = Intent(Intent.ACTION_VIEW, target.toUri())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            Log.d(TAG, "Opened URL in browser: $target")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open URL: $target", e)
        }
    }

    /**
     * Returns a URL that ACTION_VIEW can handle, or null when there is nothing usable.
     * A schemeless value like "example.com" has no ACTION_VIEW handler, so assume https://.
     */
    fun normalizeWebUrl(url: String?): String? {
        val raw = url?.trim().orEmpty()
        if (raw.isEmpty()) return null

        val scheme = SCHEME_REGEX.find(raw)?.groupValues?.get(1)?.lowercase()
        return when (scheme) {
            null -> "https://${raw.trimStart('/')}".takeIf { raw.trimStart('/').isNotEmpty() }
            "http", "https" -> raw.takeIf { raw.substringAfter(':').trimStart('/').isNotEmpty() }
            else -> null // mailto:, intent:, javascript:, ... not ours to open
        }
    }

    private val SCHEME_REGEX = Regex("^([a-zA-Z][a-zA-Z0-9+.-]*):")
}
