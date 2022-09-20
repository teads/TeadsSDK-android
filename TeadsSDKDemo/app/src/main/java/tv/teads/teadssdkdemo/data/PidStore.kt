package tv.teads.teadssdkdemo.data

import android.content.Context
import android.preference.PreferenceManager
import tv.teads.teadssdkdemo.utils.toDefaultPid
import tv.teads.teadssdkdemo.utils.toStoreKey

object PidStore {
    const val SHAREDPREF_INREAD_PID_KEY = "sp_inread_pid"
    const val SHAREDPREF_INFEED_PID_KEY = "sp_infeed_pid"

    const val SHAREDPREF_INREAD_DEFAULT_PID = 84242
    const val SHAREDPREF_INFEED_DEFAULT_PID = 124859

    const val SHAREDPREF_WEBVIEW_DEFAULT = "file:///android_asset/demo.html"
    const val SHAREDPREF_WEBVIEW_NIGHT = "file:///android_asset/demo_night.html"

    /**
     * Return current selected format type
     *
     */
    var selectedFormat: FormatType = FormatType.INREAD

    /**
     * Return current selected provider type
     *
     */
    var selectedProvider: ProviderType = ProviderType.DIRECT

    fun getPid(context: Context, formatType: FormatType? = null): Int {
        val format = formatType ?: selectedFormat

        return PreferenceManager
            .getDefaultSharedPreferences(context)
            .getInt(format.toStoreKey(), format.toDefaultPid())
    }

    fun setPid(context: Context, pid: Int, formatType: FormatType) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putInt(formatType.toStoreKey(), pid)
            .apply()
    }
}