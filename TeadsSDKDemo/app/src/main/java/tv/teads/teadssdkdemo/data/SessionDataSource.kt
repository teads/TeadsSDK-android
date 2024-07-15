package tv.teads.teadssdkdemo.data

import android.content.Context
import android.preference.PreferenceManager
import tv.teads.teadssdkdemo.utils.toDefaultPid
import tv.teads.teadssdkdemo.utils.toStoreKey

object SessionDataSource {
    const val SHAREDPREF_INREAD_PID_KEY = "sp_inread_pid"
    const val SHAREDPREF_INFEED_PID_KEY = "sp_infeed_pid"

    const val SHAREDPREF_INREAD_DEFAULT_PID = 84242
    const val SHAREDPREF_INFEED_DEFAULT_PID = 124859

    const val SHAREDPREF_WEBVIEW_DEFAULT = "file:///android_asset/demo.html"
    const val SHAREDPREF_WEBVIEW_NIGHT = "file:///android_asset/demo_night.html"

    // This fake GDPR str is used by our infeed test pid only, since the server currently requires it to fill with an ad
    const val FAKE_GDPR_STR = "CPXVK9cPXVK9cAtABBFRCKCsAP_AAH_AAAAAIqtd_X__bX9j-_5_fft0eY1P9_r3_-QzjhfNt-8F3L_W_L0X42E7NF36pq4KuR4Eu3LBIQNlHMHUTUmwaokVrzHsak2cpyNKJ7LEknMZO2dYGH9Pn9lDuYKY7_5___bx3j-v_t_-39T378Xf3_d5_2---vCfV599jbv9f3__39nP___9v-_8_______gimASYal5AF2JY4Mm0aRQogRhWEhVAoAKKAYWiKwAcHBTsrAJ9QQsAEAqAjAiBBiCjBgEAAgEASERASAFggEQBEAgABAAiAQgAImAQWAFgYBAAKAaFiAFAAIEhBkUERymBARIlFBLZWIJQV7GmEAZZYAUCiMioAESAAAkDISFg5jgCQEuFkgSYoXyAEYAAAAA.YAAAAAAAAAAA"

    /**
     * Return current selected format type
     *
     */
//    var selectedFormat: FormatType = FormatType.INREAD
    var selectedFormat: FormatType = FormatType.INFEED

    /**
     * Return current selected provider type
     *
     */
//    var selectedProvider: ProviderType = ProviderType.DIRECT
    var selectedProvider: ProviderType = ProviderType.APPLOVIN

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