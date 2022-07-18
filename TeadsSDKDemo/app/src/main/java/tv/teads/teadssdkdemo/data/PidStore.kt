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

    fun getPid(context: Context, formatType: FormatType): Int {
        return PreferenceManager
            .getDefaultSharedPreferences(context)
            .getInt(formatType.toStoreKey(), formatType.toDefaultPid())
    }

    fun setPid(context: Context, pid: Int, formatType: FormatType) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putInt(formatType.toStoreKey(), pid)
            .apply()
    }
}