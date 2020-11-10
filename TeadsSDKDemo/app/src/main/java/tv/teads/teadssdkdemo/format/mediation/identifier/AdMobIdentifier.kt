package tv.teads.teadssdkdemo.format.mediation.identifier

import tv.teads.teadssdkdemo.data.CreativeSize

object AdMobIdentifier {
    const val ADMOB_TEADS_APP_ID = "ca-app-pub-3068786746829754~3613028870"

    private const val AD_UNIT_LANDSCAPE = "ca-app-pub-3068786746829754/3486435166"
    private const val AD_UNIT_VERTICAL = "ca-app-pub-3068786746829754/1731249109"
    private const val AD_UNIT_SQUARE = "ca-app-pub-3068786746829754/5867288248"
    private const val AD_UNIT_CAROUSEL = "ca-app-pub-3068786746829754/1761017118"

    fun getAdUnitFromPid(pid: Int): String {
        return when (pid) {
            CreativeSize.VERTICAL.value -> AD_UNIT_VERTICAL
            CreativeSize.SQUARE.value -> AD_UNIT_SQUARE
            CreativeSize.CAROUSEL.value -> AD_UNIT_CAROUSEL
            else -> AD_UNIT_LANDSCAPE
        }
    }
}