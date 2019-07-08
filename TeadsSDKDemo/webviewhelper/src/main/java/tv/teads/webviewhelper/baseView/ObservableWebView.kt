/*
 * Copyright (c) Teads 2018.
 */

package tv.teads.webviewhelper.baseView

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

/**
 * This WebView dispatch scroll event to the register listener.
 */

class ObservableWebView : WebView {

    private var onScrollListener: OnScrollListener? = null

    constructor(context: Context) : super(context.applicationContext) {}

    constructor(context: Context, attrs: AttributeSet) : super(context.applicationContext, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context.applicationContext, attrs, defStyle)

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        onScrollListener?.onScroll(l, t)
    }

    fun setOnScrollListener(onScrollListener: OnScrollListener) {
        this.onScrollListener = onScrollListener
    }

    fun clean() {
        onScrollListener = null
    }

    /**
     * Implement in the activity/fragment/view that you want to listen to the webview
     */
    interface OnScrollListener {
        fun onScroll(l: Int, t: Int)
    }

}
