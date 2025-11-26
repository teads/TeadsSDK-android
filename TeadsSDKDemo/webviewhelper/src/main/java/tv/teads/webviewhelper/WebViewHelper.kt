package tv.teads.webviewhelper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.webkit.WebView

import java.io.IOException
import java.io.InputStream

/**
 * Helper to find , open, and close a slot in a the content of a [WebView].
 * To do it, a JS ([] is injected in the webview
 */

internal class WebViewHelper @SuppressLint("AddJavascriptInterface")
private constructor(builder: Builder) : JSInterface.Listener {

    private val webview: WebView

    private val selector: String

    private val listener: Listener?

    private val requestSlotTimeout: TimeoutCountdownTimer

    private val mainThreadHandler: Handler?

    init {
        val mJSInterface = JSInterface(this)
        webview = builder.webview
        selector = builder.selector
        listener = builder.listener
        mainThreadHandler = builder.handler

        webview.addJavascriptInterface(mJSInterface, Constants.JAVASCRIPT_INTERFACE_TAG)

        requestSlotTimeout = object : TimeoutCountdownTimer(Constants.WEBVIEW_TIMEOUT) {
            override fun onTimeout() {
                Log.w(TAG, "Countdown timed out")
                listener?.onSlotNotFound()
                reset()
            }
        }
    }

    /**
     * Inject the teads js in the webview
     *
     * @param context The application context
     */
    fun injectJS(context: Context?) {
        if (context != null) {
            var inputStream: InputStream? = null
            try {
                inputStream = context.assets.open("bootstrap.js")
                val buffer = ByteArray(inputStream.available())

                inputStream.read(buffer)
                val encoded = Base64.encodeToString(buffer, Base64.NO_WRAP)
                webview.loadUrl("javascript:(function() {" +
                        "var scriptElement = document.getElementById('teadsbootstrap');" +
                        " if(scriptElement) scriptElement.remove();" +
                        " var script = document.createElement('script');" +
                        "script.innerHTML = window.atob('" + encoded + "');" +
                        "script.setAttribute('id', 'teadsbootstrap');" +
                        "script.setAttribute('type', 'text/javascript');" +
                        " document.body.appendChild(script);})()")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }

    /**
     * Call the js in the main thread to insert the slot in the webview content.
     * If a [.selector] is specified, the bootstrap will use it
     */
    fun insertSlot() {
        val command = String.format(INSERT_SLOT_JS, if (TextUtils.isEmpty(selector))
            ""
        else
            "'$selector'")
        mainThreadHandler?.post(LoadJSRunnable(webview, command))
        requestSlotTimeout.start()
    }

    /**
     * Update the slot height with the given ratio and offset
     *
     * @param ratio  the mediafile ratio
     * @param offset the offset
     */
    fun updateSlot(ratio: Float, offset: Int) {
        mainThreadHandler?.post(LoadJSRunnable(webview, String.format(UPDATE_SLOT_JS, offset, ratio)))
    }

    /**
     * Call the js in the main thread to open the slot
     */
    fun openSlot() {
        mainThreadHandler?.post(LoadJSRunnable(webview, OPEN_SLOT_JS))
    }

    /**
     * Call the js in the main thread to close the slot
     */
    fun closeSlot() {
        mainThreadHandler?.post(LoadJSRunnable(webview, CLOSE_SLOT_JS))
    }

    /**
     * Resets the internal state of the controller preparing it for
     * another use.
     */
    fun reset() {
        requestSlotTimeout.cancel()
    }

    /**
     * Notify the js to sent us the slot position, as answer it should call [.onSlotUpdated]
     */
    fun askSlotPosition() {
        mainThreadHandler?.post(LoadJSRunnable(webview, UPDATE_POSITION_JS))
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////
     *
     *   Implements JS Interface
     *
     *///////////////////////////////////////////////////////////////////////////////////////////////

    override fun onJsReady() {
        listener?.onJsReady()
    }

    override fun onSlotUpdated(top: Int, left: Int, bottom: Int, right: Int, pixelRatio: Float) {
        if (listener == null) {
            requestSlotTimeout.cancel()
            return
        }

        if (requestSlotTimeout.isTimeout) {
            listener.onSlotNotFound()
        } else {
            val l = (pixelRatio * left).toInt()
            val t = (pixelRatio * top).toInt()
            val r = (pixelRatio * right).toInt()
            val b = (pixelRatio * bottom).toInt()
            listener.onSlotUpdated(l, t, r, b)
        }
        requestSlotTimeout.cancel()
    }

    override fun handleError(error: String) {
        listener?.onError(error)
    }


    /*//////////////////////////////////////////////////////////////////////////////////////////////
     *
     *   Builder
     *
     *///////////////////////////////////////////////////////////////////////////////////////////////

    internal class Builder(internal val webview: WebView, internal val listener: Listener?, internal val selector: String) {

        internal var handler: Handler? = null

        fun mainThreadHandler(handler: Handler): Builder {
            this.handler = handler
            return this
        }

        fun build(): WebViewHelper {
            if (this.handler == null) {
                this.handler = Handler(Looper.getMainLooper())
            }
            return WebViewHelper(this)
        }
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////
     *
     *   Listener
     *
     *///////////////////////////////////////////////////////////////////////////////////////////////

    interface Listener {

        fun onJsReady()

        fun onSlotNotFound()

        fun onSlotUpdated(left: Int, top: Int, right: Int, bottom: Int)

        fun onError(error: String)
    }

    companion object {

        private val TAG = WebViewHelper::class.java.simpleName

        private val INSERT_SLOT_JS = "javascript:window.utils.insertPlaceholder(%s);"
        private val UPDATE_SLOT_JS = "javascript:window.utils.updatePlaceholder({" +
                "'offsetHeight':%s," +
                "'ratioVideo':%s" +
                "});"
        private val OPEN_SLOT_JS = "javascript:window.utils.showPlaceholder();"
        private val CLOSE_SLOT_JS = "javascript:window.utils.hidePlaceholder();"
        private val UPDATE_POSITION_JS = "javascript:window.utils.sendTargetGeometry();"
    }

}
