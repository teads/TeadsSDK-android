package tv.teads.teadssdkdemo.format.inread.extensions

import android.view.View
import tv.teads.sdk.AdRatio

/**
 * Resize the ad container to the correct height
 * @param adRatio the ratio of the ad
 *
 * adRatio.calculateHeight(adSlotContainer.measuredWidth) calculates the height of the ad container
 * measuredWidth parameter must to be from the ad container/view group encapsulating the ad view
 */
fun View.resizeAdContainer(adRatio: AdRatio) {
    val adSlotContainer = this
    val adSlotContainerParams = adSlotContainer.layoutParams
    adSlotContainerParams.height = adRatio.calculateHeight(adSlotContainer.measuredWidth)
    adSlotContainer.layoutParams = adSlotContainerParams
}