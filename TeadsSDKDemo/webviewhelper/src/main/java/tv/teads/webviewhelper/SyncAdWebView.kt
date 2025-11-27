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
) : WebViewHelper.Listener, ObservableWebView.OnScrollListener, ObservableContainerAdView.ActionMoveListener {


    private var opened: Boolean = false

    /**
     * Layout containing the ad and the webview
     */
    private lateinit var container: FrameLayout
    private val containerAdView: ObservableContainerAdView = ObservableContainerAdView(context)

    private val webviewHelper: WebViewHelper = WebViewHelper.Builder(webview, this, selector).build()

    private var initialY = 0
    private var currentAdRatio: AdRatio? = null

    private val mainHandler = Handler(Looper.getMainLooper())

    // Store last viewport position from JavaScript and the scroll position when it was calculated
    private var lastViewportTop = 0
    private var scrollYWhenJsUpdated = 0

    // Track scroll velocity for adaptive smoothing
    private var lastScrollY = 0
    private var lastScrollTime = 0L
    private var scrollVelocity = 0f // pixels per millisecond

    // Sanity check for edge cases
    private val SANITY_CHECK_INTERVAL = 1000L // Check every 1 second
    private val MAX_POSITION_ERROR = 80 // Force resync if off by more than 80px

    private val sanityCheckRunnable = object : Runnable {
        override fun run() {
            // Calculate where ad should be vs where it actually is
            val currentScrollY = webview.scrollY
            val expectedViewportTop = lastViewportTop + (scrollYWhenJsUpdated - currentScrollY)
            val actualTranslationY = containerAdView.translationY + topOffSet
            val positionError = kotlin.math.abs(expectedViewportTop - actualTranslationY)

            // Always log for debugging
            Log.d(TAG, "[Sanity Check] scrollY: $currentScrollY, expected: $expectedViewportTop, " +
                    "actual: $actualTranslationY, error: $positionError px, " +
                    "lastViewportTop: $lastViewportTop, scrollYWhenJsUpdated: $scrollYWhenJsUpdated")

            if (positionError > MAX_POSITION_ERROR && lastViewportTop > 0) {
                Log.w(TAG, "[Sanity Check] ⚠️ RESYNC TRIGGERED - Position error: $positionError px > $MAX_POSITION_ERROR px")
                // Force JavaScript to send fresh position
                webviewHelper.askSlotPosition()
            }

            // Schedule next check
            mainHandler.postDelayed(this, SANITY_CHECK_INTERVAL)
        }
    }

    init {
        containerAdView.setMoveListener(this)
        webview.setOnScrollListener(this)
        // Start sanity check loop
        mainHandler.postDelayed(sanityCheckRunnable, SANITY_CHECK_INTERVAL)
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
        // Stop sanity check loop
        mainHandler.removeCallbacks(sanityCheckRunnable)
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
        // Wait for WebView to complete layout and have valid dimensions
        webview.viewTreeObserver.addOnGlobalLayoutListener(object : android.view.ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (webview.width > 0 && webview.height > 0) {
                    webview.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    currentAdRatio?.let { adRatio ->
                        // Recalculate ratio for new WebView width
                        val ratio = adRatio.getAdSlotRatio(webview.measuredWidth)
                        updateSlot(ratio)
                        // Force re-open to apply new dimensions
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

    override fun onSlotUpdated(left: Int, top: Int, right: Int, bottom: Int) {
        // Post to main thread since JS bridge calls from background thread
        mainHandler.post {
            if (containerAdView.parent == null)
                injectTeadsContainerAdView()

            val width = right - left
            val height = bottom - top

            val currentScrollY = webview.scrollY

            // Always accept updates, but apply velocity-aware smoothing to prevent jitter
            if (lastViewportTop == 0) {
                // First update - accept immediately
                scrollYWhenJsUpdated = currentScrollY
                lastViewportTop = top
                initialY = top + scrollYWhenJsUpdated
                Log.d(TAG, "[Native] First JS update - viewportTop: $top, scrollY: $scrollYWhenJsUpdated")
            } else {
                // Calculate what interpolation expects vs what JS says
                val interpolatedViewportTop = lastViewportTop + (scrollYWhenJsUpdated - currentScrollY)
                val delta = top - interpolatedViewportTop

                // Velocity-aware smoothing:
                // At high velocity, larger deltas are normal (not errors), so smooth more
                // At low velocity, small deltas are errors, correct them
                val velocityFactor = kotlin.math.min(kotlin.math.abs(scrollVelocity) / 3f, 1f)
                val adaptiveThreshold = 30f + (velocityFactor * 70f) // 30-100px based on velocity

                val weight = kotlin.math.min(kotlin.math.abs(delta).toFloat() / adaptiveThreshold, 1f)
                val smoothedViewportTop = (interpolatedViewportTop * (1f - weight) + top * weight).toInt()

                scrollYWhenJsUpdated = currentScrollY
                lastViewportTop = smoothedViewportTop
                initialY = smoothedViewportTop + scrollYWhenJsUpdated

                Log.d(TAG, "[Native] Smoothed update - jsTop: $top, interpolated: $interpolatedViewportTop, delta: $delta, velocity: $scrollVelocity, threshold: $adaptiveThreshold, weight: $weight, result: $smoothedViewportTop")
            }

            // Always update layout params (width, height, margins)
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

            // Apply position
            onScroll(webview.scrollX, webview.scrollY)
        }
    }

    override fun onError(error: String) {
        Log.w(TAG, "An Error occurs during the webview slot managment")
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
        // Calculate scroll velocity for adaptive smoothing
        val currentTime = System.currentTimeMillis()
        if (lastScrollTime > 0) {
            val timeDelta = currentTime - lastScrollTime
            if (timeDelta > 0) {
                val positionDelta = t - lastScrollY
                // Smooth velocity using exponential moving average
                val instantVelocity = kotlin.math.abs(positionDelta.toFloat() / timeDelta.toFloat())
                scrollVelocity = scrollVelocity * 0.7f + instantVelocity * 0.3f
            }
        }
        lastScrollY = t
        lastScrollTime = currentTime

        // Smooth interpolation:
        // JavaScript gave us viewport position at a specific scroll offset
        // We interpolate smoothly as scroll changes
        //
        // Formula: currentViewportPos = lastViewportTop + (scrollYWhenJsUpdated - currentScrollY)
        // This keeps position perfectly smooth during scroll

        val scrollDelta = scrollYWhenJsUpdated - t
        val interpolatedViewportTop = lastViewportTop + scrollDelta
        val newTranslationY = (interpolatedViewportTop - topOffSet).toFloat()

        containerAdView.translationY = newTranslationY
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

        private val TAG = SyncAdWebView::class.java.simpleName
    }
}