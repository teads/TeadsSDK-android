package tv.teads.teadssdkdemo.utils

import tv.teads.teadssdkdemo.data.FormatType
import tv.teads.teadssdkdemo.data.SessionDataSource

fun FormatType.toDefaultPid(): Int {
    return when (this) {
        FormatType.INREAD -> SessionDataSource.SHAREDPREF_INREAD_DEFAULT_PID
        FormatType.INFEED -> SessionDataSource.SHAREDPREF_INFEED_DEFAULT_PID
    }
}

fun FormatType.toStoreKey(): String {
    return when (this) {
        FormatType.INREAD -> SessionDataSource.SHAREDPREF_INREAD_PID_KEY
        FormatType.INFEED -> SessionDataSource.SHAREDPREF_INFEED_PID_KEY
    }
}