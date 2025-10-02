package tv.teads.teadssdkdemo.v6.data

/**
 * Represents the different creative types available in the demo
 */
enum class CreativeType(val displayName: String, val pid: Int) {
    LANDSCAPE("Landscape", 84242),
    VERTICAL("Vertical", 127546),
    SQUARE("Square", 127547),
    CAROUSEL("Carousel", 128779)
}
