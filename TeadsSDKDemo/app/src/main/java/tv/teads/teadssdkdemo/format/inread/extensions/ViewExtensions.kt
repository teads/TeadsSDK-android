package tv.teads.teadssdkdemo.format.inread.extensions

import android.view.View
import tv.teads.sdk.AdRatio

fun View.resizeAdContainer(onCalculateHeight: () -> Int) {
    val adSlotContainer = this
    val adSlotContainerParams = adSlotContainer.layoutParams
    adSlotContainerParams.height = onCalculateHeight()
    adSlotContainer.layoutParams = adSlotContainerParams
}