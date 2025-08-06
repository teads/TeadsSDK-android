package tv.teads.teadssdkdemo.format.mediation.identifier

import com.google.android.gms.ads.RequestConfiguration
import tv.teads.teadssdkdemo.data.CreativeSize

object AdMobIdentifier {
    val TEST_DEVICE_ID_CONFIG = RequestConfiguration
        .Builder()
        .setTestDeviceIds(listOf("BAC58D23C8C5265E2C8A56FE7FBAE2C1"))
        .build()

    private const val AD_UNIT_LANDSCAPE = "ca-app-pub-3068786746829754/3486435166"
    private const val AD_UNIT_VERTICAL = "ca-app-pub-3068786746829754/1731249109"
    private const val AD_UNIT_SQUARE = "ca-app-pub-3068786746829754/5867288248"
    private const val AD_UNIT_CAROUSEL = "ca-app-pub-3068786746829754/1761017118"
    private const val AD_UNIT_NATIVE = "ca-app-pub-3068786746829754/9820813147"

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