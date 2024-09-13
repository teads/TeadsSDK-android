package tv.teads.teadssdkdemo.format.inread.extensions

import android.view.View
import tv.teads.sdk.AdRatio

fun View.resizeAdContainer(adRatio: AdRatio) {
    val adSlotContainer = this
    val adSlotContainerParams = adSlotContainer.layoutParams
    adSlotContainerParams.height = adRatio.calculateHeight(adSlotContainer.measuredWidth)
    adSlotContainer.layoutParams = adSlotContainerParams
}