package tv.teads.teadssdkdemo.format.inread.identifier

import tv.teads.teadssdkdemo.data.CreativeSize

object DirectIdentifier {
    private val mPids = mapOf(
            CreativeSize.LANDSCAPE.value to 0,
            CreativeSize.VERTICAL.value to 1,
            CreativeSize.SQUARE.value to 2,
            CreativeSize.CAROUSEL.value to 3
    )

    fun getPositionByPid(pid: Int): Int {
        return mPids.getOrElse(pid) { -1 }
    }
}