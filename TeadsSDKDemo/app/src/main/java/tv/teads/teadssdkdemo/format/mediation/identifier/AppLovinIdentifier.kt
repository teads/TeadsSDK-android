package tv.teads.teadssdkdemo.format.mediation.identifier

import tv.teads.teadssdkdemo.data.CreativeSize

object AppLovinIdentifier {

    private const val AD_UNIT_LANDSCAPE = "d4fe50739a8b55c3"
    private const val AD_UNIT_VERTICAL = "be7da42a7dea91e3"
    private const val AD_UNIT_SQUARE = "50eeb978b06eb4ef"
    private const val AD_UNIT_CAROUSEL = "5a5ea03f1285b548"
    private const val AD_UNIT_NATIVE = "8c137f7d9047e302"

    fun getAdUnitFromPid(pid: Int): String {
        return when (pid) {
            CreativeSize.VERTICAL.value -> AD_UNIT_VERTICAL
            CreativeSize.SQUARE.value -> AD_UNIT_SQUARE
            CreativeSize.CAROUSEL.value -> AD_UNIT_CAROUSEL
            CreativeSize.NATIVE.value -> AD_UNIT_NATIVE
            else -> AD_UNIT_LANDSCAPE
        }
    }
}