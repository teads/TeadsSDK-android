package tv.teads.featurefoo

import android.content.Context
import tv.teads.sdk.TeadsSDK

object FooTeadsCaller {
    fun init(context: Context, appKey: String) {
        TeadsSDK.configure(context, appKey)
    }
}
