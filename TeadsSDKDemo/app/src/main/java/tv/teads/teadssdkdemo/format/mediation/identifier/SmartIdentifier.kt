package tv.teads.teadssdkdemo.format.mediation.identifier

import tv.teads.teadssdkdemo.data.CreativeSize

object SmartIdentifier {
    const val SUPPLY_CHAIN: String = "" // "1.0,1!exchange1.com,1234,1,publisher,publisher.com";
    const val SITE_ID = 385317
    const val PAGE_NAME = "1331330"

    private const val FORMAT_CAROUSEL = 96470
    private const val FORMAT_SQUARE = 96468
    private const val FORMAT_VERTICAL = 96469
    private const val FORMAT_LANDSCAPE = 96445

    fun getFormatFromPid(pid: Int): Int {
        return when (pid) {
            CreativeSize.CAROUSEL.value -> FORMAT_CAROUSEL
            CreativeSize.LANDSCAPE.value -> FORMAT_LANDSCAPE
            CreativeSize.VERTICAL.value -> FORMAT_VERTICAL
            CreativeSize.SQUARE.value -> FORMAT_SQUARE
            else -> FORMAT_LANDSCAPE
        }
    }
}