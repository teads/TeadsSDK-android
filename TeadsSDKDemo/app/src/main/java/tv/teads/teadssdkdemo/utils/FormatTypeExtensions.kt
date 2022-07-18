package tv.teads.teadssdkdemo.utils

import tv.teads.teadssdkdemo.data.FormatType
import tv.teads.teadssdkdemo.data.PidStore

fun FormatType.toDefaultPid(): Int {
    return when (this) {
        FormatType.INREAD -> PidStore.SHAREDPREF_INREAD_DEFAULT_PID
        FormatType.INFEED -> PidStore.SHAREDPREF_INFEED_DEFAULT_PID
    }
}

fun FormatType.toStoreKey(): String {
    return when (this) {
        FormatType.INREAD -> PidStore.SHAREDPREF_INREAD_PID_KEY
        FormatType.INFEED -> PidStore.SHAREDPREF_INFEED_PID_KEY
    }
}