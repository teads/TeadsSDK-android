/*
 * Copyright (c) Teads 2018.
 */

package tv.teads.webviewhelper


import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import tv.teads.sdk.android.CustomAdView
import tv.teads.sdk.android.engine.ui.view.ObservableAdView
import tv.teads.webviewhelper.baseView.ObservableWebView

/**
 *
 * Insert and Synchronise the scroll between the TeadsAdView and the webview
 *
 * This helper has been provided to give you a hand in your integration webview.
 * It's not designed to work on every integration, it needs to be customised to suit your needs
 *
 * @param webview  Webview with which we synchronize the scroll. [ObservableWebView]
 * @param adView   The adview with which we synchronize the scroll
 * @param listener The listener
 * @param selector The selector where we want insert the ad view
 *
 */
class SyncWebViewTeadsAdView(private val webview: ObservableWebView,
                             private val adView: CustomAdView,
                             private val listener: Listener,
                             selector: String) : WebViewHelper.Listener, ObservableWebView.OnScrollListener, ObservableAdView.ActionMoveListener {

    /**
     * Layout containing the ad and the webview
     */
    private lateinit var container: FrameLayout

    private val webviewHelper: WebViewHelper

    private var initialY = 0

    init {
        adView.setMoveListener(this)
        webview.setOnScrollListener(this)
        webviewHelper = WebViewHelper.Builder(webview, this, selector)
                .build()

    }

    /**
     * Inject the js in your webview. Should be call when the webview is ready
     */
    fun injectJS() {
        webviewHelper.injectJS(webview.context.applicationContext)
    }

    /**
     * Insert the teadsAdView and the webview in a FrameLayout,
     * and inject it in the old webview parent hierarchy
     */
    private fun injectTeadsAdView() {
        if (webview.parent !is ViewGroup) {
            return
        }
        val handler = Handler(Looper.getMainLooper())
        handler.post {

            val webViewParent = webview.parent as ViewGroup
            container = FrameLayout(webview.context)
            container.layoutParams = ViewGroup.LayoutParams(webview.layoutParams)
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
            container.addView(webview)
            adView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)

            if (adView.parent != null && adView.parent is ViewGroup) {
                (adView.parent as ViewGroup).removeView(adView)
            }

            container.addView(adView)

            webViewParent.addView(container, webviewPosition)
            listener.onHelperReady(container)
        }

    }

    /**
     * Clean the webview helper and the observable web view
     */
    fun clean() {
        webview.clean()
        webviewHelper.reset()
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
        if (adView.parent == null) {
            injectTeadsAdView()
        }

        val width = right - left

        initialY = top
        adView.translationY = (initialY - webview.scrollY).toFloat()

        if (adView.layoutParams != null
                && adView.layoutParams is ViewGroup.MarginLayoutParams) {
            (adView.layoutParams as ViewGroup.MarginLayoutParams).leftMargin = left
            (adView.layoutParams as ViewGroup.MarginLayoutParams).rightMargin = webview.width - width - left
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
        adView.translationY = (initialY - t).toFloat()
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

    interface Listener {
        /**
         * Called when the adview has been ready to be used
         */
        fun onHelperReady(adContainer: ViewGroup)
    }

    companion object {

        private val TAG = SyncWebViewTeadsAdView::class.java.simpleName
    }
}
