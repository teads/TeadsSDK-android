package tv.teads.webviewhelper

import android.webkit.JavascriptInterface

/**
 * Callback from Javascript JavaBridge. Use to get callback from WebView after registering a
 * JavascriptInterface
 */
internal class JSInterface(private val listener: Listener) {

    /**
     * Callback invoked by teads Teads js bootstrap when lib is injected and ready for
     * processing.
     */
    @JavascriptInterface
    fun onTeadsJsLibReady() {
        listener.onJsReady()
    }

    /**
     * Called when the HTML placeholder has been inserted into the DOM
     *
     * @param top        pixel value (web) - viewport relative
     * @param left       pixel value (web)
     * @param bottom     pixel value (web)
     * @param right      pixel value (web)
     * @param scrollY    the native scroll position that JS used for calculation (handshake)
     * @param pixelRatio corresponding to screen ratio
     */
    @JavascriptInterface
    fun onSlotUpdated(top: Int, left: Int,
                      bottom: Int, right: Int,
                      scrollY: Int,
                      pixelRatio: Float) {
        listener.onSlotUpdated(top, left, bottom, right, scrollY, pixelRatio)
    }

    @JavascriptInterface
    fun onSlotStartShow() {

    }

    @JavascriptInterface
    fun onSlotStartHide() {

    }

    @JavascriptInterface
    fun handleError(error: String) {
        listener.handleError(error)
    }

    /**
     * Javascript interface listener, provide method to synchronize the ad with the slot
     */
    internal interface Listener {

        fun onJsReady()

        fun onSlotUpdated(top: Int, left: Int,
                          bottom: Int, right: Int,
                          scrollY: Int,
                          pixelRatio: Float)

        fun handleError(error: String)
    }
}
