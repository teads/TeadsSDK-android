package tv.teads.teadssdkdemo.data

enum class FormatType(val value: Int) {
    INREAD(0),
    NATIVE(1)
}

enum class ProviderType(val value: Int) {
    DIRECT(0),
    MOPUB(1),
    ADMOB(2)
    //SMART(3)
}

enum class RecyclerItemType(val value: Int) {
    TYPE_TEADS(0),
    TYPE_SCROLL_DOWN (2),
    TYPE_ARTICLE_TITLE(3),
    TYPE_ARTICLE_REAL_LINES(4),
    TYPE_ARTICLE_FAKE_LINES(5)
}

enum class CreativeSize(val value: Int) {
    LANDSCAPE(84242),
    VERTICAL(127546),
    SQUARE(127547),
    CAROUSEL(128779),
    NATIVE(124859)
}