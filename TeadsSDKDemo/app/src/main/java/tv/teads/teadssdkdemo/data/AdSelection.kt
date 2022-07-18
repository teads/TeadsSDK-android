package tv.teads.teadssdkdemo.data

enum class FormatType(val value: String) {
    INREAD("inRead"),
    INFEED("inFeed")
}

enum class ProviderType(val value: Int) {
    DIRECT(0),
    ADMOB(2),
    //SMART(3)
    APPLOVIN(4)
}

enum class RecyclerItemType(val value: Int) {
    TYPE_EMPTY(-1),
    TYPE_TEADS(0),
    TYPE_SCROLL_DOWN (2),
    TYPE_ARTICLE_TITLE(3),
    TYPE_ARTICLE_REAL_LINES(4),
    TYPE_ARTICLE_FAKE_LINES(5),
    TYPE_NATIVE_AD(5),
    TYPE_FAKE_FEED(6)
}

enum class CreativeSize(val value: Int) {
    LANDSCAPE(84242),
    VERTICAL(127546),
    SQUARE(127547),
    CAROUSEL(128779),
    NATIVE(124859)
}
