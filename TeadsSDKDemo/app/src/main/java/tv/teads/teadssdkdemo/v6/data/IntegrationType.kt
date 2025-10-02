package tv.teads.teadssdkdemo.v6.data

/**
 * Represents the different integration types available in the demo
 */
enum class IntegrationType(val displayName: String) {
    COLUMN("Column"),
    LAZY_COLUMN("Lazy Column"),
    SCROLL_VIEW("ScrollView"),
    RECYCLER_VIEW("RecyclerView")
}
