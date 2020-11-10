package tv.teads.teadssdkdemo.format.mediation.identifier

import tv.teads.teadssdkdemo.data.CreativeSize

object MoPubIdentifier {
    const val MOPUB_ID = "8678f92af2814e608191dbdf46efa081"

    private const val AD_UNIT_LANDSCAPE = "8678f92af2814e608191dbdf46efa081"
    private const val AD_UNIT_VERTICAL = "b10ae0be9e0948da83172e5f135f1407"
    private const val AD_UNIT_SQUARE = "2ffe21bbd7db4df09af9fe25402b2301"
    private const val AD_UNIT_CAROUSEL = "9dd53af7021c4894bda608f8afb52b42"

    fun getAdUnitFromPid(pid: Int): String {
        return when (pid) {
            CreativeSize.VERTICAL.value -> AD_UNIT_VERTICAL
            CreativeSize.SQUARE.value -> AD_UNIT_SQUARE
            CreativeSize.CAROUSEL.value -> AD_UNIT_CAROUSEL
            else -> AD_UNIT_LANDSCAPE
        }
    }
}