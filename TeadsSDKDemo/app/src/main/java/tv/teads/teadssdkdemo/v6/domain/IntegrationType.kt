package tv.teads.teadssdkdemo.v6.domain

/**
 * Represents the different integration types available in the demo
 */
enum class IntegrationType(val displayName: String) {
    COLUMN("Column"),
    LAZYCOLUMN("Lazy Column"),
    SCROLLVIEW("ScrollView"),
    RECYCLERVIEW("RecyclerView")
}
