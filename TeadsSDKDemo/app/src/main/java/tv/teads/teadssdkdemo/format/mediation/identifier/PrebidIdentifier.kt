package tv.teads.teadssdkdemo.format.mediation.identifier

import tv.teads.teadssdkdemo.data.CreativeSize

object PrebidIdentifier {
    private const val AD_UNIT_LANDSCAPE = "imp-video-300x250" // Currently fake prebid test server provides only this one
    private const val AD_UNIT_VERTICAL = "imp-banner-vertical"
    private const val AD_UNIT_CAROUSEL = "imp-banner-carousel"
    private const val AD_UNIT_SQUARE = "imp-banner-square"

    fun getAdUnitFromPid(creativeSize: CreativeSize): String {
        return when (creativeSize) {
            CreativeSize.VERTICAL -> AD_UNIT_VERTICAL
            CreativeSize.SQUARE -> AD_UNIT_SQUARE
            CreativeSize.CAROUSEL -> AD_UNIT_CAROUSEL
            else -> AD_UNIT_LANDSCAPE
        }
    }
}