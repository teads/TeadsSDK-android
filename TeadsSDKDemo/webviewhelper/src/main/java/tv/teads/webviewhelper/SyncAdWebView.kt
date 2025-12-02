package tv.teads.webviewhelper


import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdRatio
import tv.teads.webviewhelper.baseView.ObservableContainerAdView
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
class SyncAdWebView(context: Context,
                    private val webview: ObservableWebView,
                    private val listener: Listener,
                    selector: String,
                    private val topOffSet: Int = 0,
                    private val bottomOffSet: Int = 0,
) : WebViewHelper.Listener, ObservableWebView.OnScrollListener {


    private var opened: Boolean = false

    /**
     * Layout containing the ad and the webview
     */
    private lateinit var container: FrameLayout
    private val containerAdView: ObservableContainerAdView = ObservableContainerAdView(context)

    private val webviewHelper: WebViewHelper = WebViewHelper.Builder(webview, this, selector).build()

    private var currentAdRatio: AdRatio? = null

    private val mainHandler = Handler(Looper.getMainLooper())

    // Document-absolute position tracking (MARKER-BASED):
    // JS calculates position using a marker at document origin (0,0)
    // Both getBoundingClientRect() calls happen in same JS frame (atomic)
    // so scroll cancels out: (documentY - scrollY) - (-scrollY) = documentY
    // This gives TRUE document-absolute position, completely scroll-independent
    private var slotDocumentY: Int = 0        // True document-absolute Y position
    private var isPositionValid: Boolean = false

    init {
        containerAdView.setTouchForwardTarget(webview)
        webview.setOnScrollListener(this)
    }

    /**
     * Inject the js in your webview. Should be call when the webview is ready
     */
    fun injectJS() {
        webviewHelper.injectJS(webview.context.applicationContext)
    }

    /**
     * Insert the trackerView and the webview in a FrameLayout,
     * and inject it in the old webview parent hierarchy
     */
    private fun injectTeadsContainerAdView() {
        if (webview.parent !is ViewGroup) {
            return
        }
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
        webview.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT
        )

        container.addView(webview)

        val adLayer = FrameLayout(webview.context).apply {
            layoutParams = FrameLayout.LayoutParams(webview.layoutParams).also {
                it.topMargin = topOffSet
                it.bottomMargin = bottomOffSet
            }

            val containerParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            addView(containerAdView, containerParams)
        }

        container.addView(adLayer)
        webViewParent.addView(container, webviewPosition)

        listener.onHelperReady(container)
    }


    fun registerAdView(adView: ViewGroup) {
        val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        containerAdView.addView(adView, 0, layoutParams)
        displayAd()
    }

    fun registerTrackerView(trackerView: AdOpportunityTrackerView) {
        containerAdView.addView(trackerView)
        displayAd()
    }

    /**
     * Clean the webview helper and the observable web view
     */
    fun clean() {
        isPositionValid = false
        webview.clean()
        webviewHelper.reset()
    }

    /**
     * Set the current ad ratio and update the slot accordingly
     * Call this when ad is received or when ratio updates
     *
     * @param adRatio The AdRatio from the SDK
     */
    fun setAdRatio(adRatio: AdRatio) {
        currentAdRatio = adRatio
        // Calculate ratio for current WebView width and update slot
        val ratio = adRatio.getAdSlotRatio(webview.measuredWidth)
        updateSlot(ratio)
    }

    /**
     * Should be called when orientation/configuration changes
     * Handles recalculation of slot dimensions for new screen size
     */
    fun onConfigurationChanged() {
        // Invalidate position - it will change after layout
        isPositionValid = false

        // Wait for WebView to complete layout and have valid dimensions
        webview.viewTreeObserver.addOnGlobalLayoutListener(object : android.view.ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (webview.width > 0 && webview.height > 0) {
                    webview.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    currentAdRatio?.let { adRatio ->
                        // Recalculate ratio for new WebView width
                        val ratio = adRatio.getAdSlotRatio(webview.measuredWidth)
                        updateSlot(ratio)
                        // Force re-open to apply new dimensions and get fresh position
                        webview.post {
                            reopenSlot()
                        }
                    }
                }
            }
        })
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

    override fun onSlotUpdated(left: Int, top: Int, right: Int, bottom: Int, scrollY: Int) {
        // Post to main thread since JS bridge calls from background thread
        mainHandler.post {
            if (containerAdView.parent == null)
                injectTeadsContainerAdView()

            val width = right - left
            val height = bottom - top

            // 'top' is now TRUE document-absolute (calculated via MARKER in JS)
            // Marker-based: both getBoundingClientRect() calls happen in same JS frame
            // so scroll cancels out mathematically - completely timing-independent!
            slotDocumentY = top
            isPositionValid = true

            Log.d(TAG, "[Native] Marker-based documentY received: $top, currentScrollY: ${webview.scrollY}")

            // Update layout params (width, height, margins)
            if (containerAdView.layoutParams != null
                && containerAdView.layoutParams is ViewGroup.MarginLayoutParams
            ) {
                val params = containerAdView.layoutParams as ViewGroup.MarginLayoutParams
                params.width = width
                params.height = height
                params.leftMargin = left
                params.rightMargin = webview.width - width - left
                containerAdView.layoutParams = params
            }
            containerAdView.requestLayout()

            // Apply position immediately
            if (isPositionValid) {
                applyPosition(webview.scrollY)
            }
        }
    }

    override fun onError(error: String) {
        Log.w(TAG, "An Error occurs during the webview slot management")
    }

    fun updateSlot(ratio: Float?) {
        if (ratio != null) {
            webviewHelper.updateSlot(ratio, 0)
        }
    }

    /**
     * Open the slot
     */
    private fun displayAd() {
        if (!opened) webviewHelper.openSlot()
    }

    /**
     * Re-open the slot to force recalculation and application of dimensions
     * Used internally after orientation changes
     */
    private fun reopenSlot() {
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
        // Simply apply position - marker-based documentY is truly stable
        // No JS communication needed during scroll
        applyPosition(t)
    }

    /**
     * Apply the ad position using marker-based document-absolute coordinates.
     * Formula: viewportTop = documentY - scrollY
     *
     * This is simple and ALWAYS accurate because:
     * - documentY is calculated atomically in JS using marker (scroll-independent)
     * - scrollY is native's own value (always accurate)
     * - No timing issues possible - the math is deterministic
     */
    private fun applyPosition(currentScrollY: Int) {
        if (!isPositionValid) return

        // Direct conversion: document-absolute to viewport-relative
        val viewportTop = slotDocumentY - currentScrollY

        val newTranslationY = (viewportTop - topOffSet).toFloat()
        containerAdView.translationY = newTranslationY
    }

    interface Listener {
        /**
         * Called when the adview has been ready to be used
         */
        fun onHelperReady(adContainer: ViewGroup)
    }

    companion object {

        private val TAG = SyncAdWebView::class.java.simpleName
    }
}
