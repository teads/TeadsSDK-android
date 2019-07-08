/*
 * Copyright (c) Teads 2019.
 */

package tv.teads.webviewhelper


import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout

import tv.teads.webviewhelper.baseView.ObservableWebView
import tv.teads.webviewhelper.baseView.ObservableWrapperView

/**
 * Insert and Synchronise the scroll between a given ViewGroup and a WebView
 *
 * @param webview               Webview with which we synchronize the scroll. [ObservableWebView]
 * @param observableWrapperView The wrapper view surrounding any ViewGroup insance, for example GAM AdView
 * @param listener              The listener
 * @param selector              The selector where we want insert the ad view
 */
class SyncWebViewViewGroup(private val webview: ObservableWebView,
                           private val wrapperView: ObservableWrapperView,
                           private val listener: Listener,
                           selector: String) : WebViewHelper.Listener,
                                               ObservableWebView.OnScrollListener,
                                               ObservableWrapperView.Listener {

    /**
     * Layout containing the ad and the webview
     */
    private var container: FrameLayout? = null

    private val webviewHelper: WebViewHelper

    private var initialY = 0

    init {
        wrapperView.setListener(this)
        webview.setOnScrollListener(this)
        webviewHelper = WebViewHelper.Builder(webview, this, selector).build()
    }

    /**
     * Inject the js in your webview. Should be call when the webview is ready
     */
    fun injectJS() {
        webviewHelper.injectJS(webview.context.applicationContext)
    }

    /**
     * Insert the given ViewGroup and the webview in a FrameLayout,
     * and inject it in the old webview parent hierarchy
     */
    private fun injectViewGroup() {
        if (webview.parent !is ViewGroup) {
            return
        }
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val webViewParent = webview.parent as ViewGroup

            container = FrameLayout(webview.context)
            container!!.layoutParams = ViewGroup.LayoutParams(webview.layoutParams)
            var webviewPosition = 0
            val childCount = webViewParent.childCount
            for (i in 0 until childCount) {
                if (webViewParent.getChildAt(i) == webview) {
                    webviewPosition = i
                }
            }

            webViewParent.removeViewAt(webviewPosition)
            webview.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                    .LayoutParams.MATCH_PARENT)
            container!!.addView(webview)
            wrapperView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                ViewGroup.LayoutParams.WRAP_CONTENT)

            if (wrapperView.parent != null && wrapperView.parent is ViewGroup) {
                (wrapperView.parent as ViewGroup).removeView(wrapperView)
            }

            container!!.addView(wrapperView)

            webViewParent.addView(container, webviewPosition)
            listener.onHelperReady(container!!)
        }
    }

    /**
     * Clean the webview helper and the observable web view
     */
    fun clean() {
        webview.clean()
        webviewHelper.reset()
        wrapperView.clean()
    }

    /**
     * Should be called when the rotation change
     */
    fun onConfigurationChanged() {
        webviewHelper.askSlotPosition()
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////
     *
     * WebviewHelper listener methods
     *
     *//////////////////////////////////////////////////////////////////////////////////////////////

    override fun onJsReady() {
        webviewHelper.insertSlot()
    }

    override fun onSlotNotFound() {
        Log.w(TAG, "No slot found.")
    }

    override fun onSlotUpdated(left: Int, top: Int, right: Int, bottom: Int) {
        if (wrapperView.parent == null) {
            injectViewGroup()
        }

        val width = right - left

        initialY = top
        wrapperView.translationY = (initialY - webview.scrollY).toFloat()

        if (wrapperView.layoutParams != null && wrapperView.layoutParams is ViewGroup.MarginLayoutParams) {
            (wrapperView.layoutParams as ViewGroup.MarginLayoutParams).leftMargin = left
            (wrapperView.layoutParams as ViewGroup.MarginLayoutParams).rightMargin = webview.width - width - left
        }
    }

    override fun onError(error: String) {
        Log.w(TAG, "An Error occurs during the webview slot managment")
    }

    fun updateSlot(ratio: Float?) {
        if (ratio != null) {
            webviewHelper.updateSlot(ratio, 0)
        }

        // After updating the slot the WebView height is not correct, hidding some content in the WebView
        webview.postDelayed({ webview.requestLayout() }, 300)
    }

    /**
     * Open the slot
     */
    fun displayAd() {
        webviewHelper.openSlot()
    }

    /**
     * Close the slot
     */
    fun closeAd() {
        webviewHelper.closeSlot()
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////
     *
     * Webview Scroll listener
     *
     *//////////////////////////////////////////////////////////////////////////////////////////////

    override fun onScroll(l: Int, t: Int) {
        wrapperView.translationY = (initialY - t).toFloat()
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////
     *
     * AdView Move listener
     *
     *//////////////////////////////////////////////////////////////////////////////////////////////

    override fun onActionMove(moveX: Int, moveY: Int) {
        if (webview.scrollY + moveY < 0) {
            webview.scrollBy(moveX, -webview.scrollY)
        } else {
            webview.scrollBy(moveX, moveY)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w == 0 || h == 0) {
            return
        }
        Log.d(TAG, "SizeCHanges: " + w + " h" + h + " ratio:" + w.toFloat() / h)
        updateSlot(w.toFloat() / h)
    }

    interface Listener {
        /**
         * Called when the adview has been ready to be used
         */
        fun onHelperReady(adContainer: ViewGroup)
    }

    companion object {
        private val TAG = SyncWebViewViewGroup::class.java.simpleName
    }
}
