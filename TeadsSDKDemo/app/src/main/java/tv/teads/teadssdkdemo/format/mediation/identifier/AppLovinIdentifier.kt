package tv.teads.teadssdkdemo.format.mediation.identifier

import tv.teads.teadssdkdemo.data.CreativeSize

object AppLovinIdentifier {

    private const val AD_UNIT_LANDSCAPE = "3359d5bcb0cf612b"
    private const val AD_UNIT_VERTICAL = "74481c0cee5c73b1"
    private const val AD_UNIT_SQUARE = "accecf03a9e0a672"
    private const val AD_UNIT_CAROUSEL = "d1fb90cd8eeb350e"
    private const val AD_UNIT_NATIVE = "a416d5d67e65ddcd"

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