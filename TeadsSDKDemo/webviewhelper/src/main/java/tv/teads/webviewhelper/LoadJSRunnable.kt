/*
 * Copyright (c) Teads 2018.
 */

package tv.teads.webviewhelper

import android.util.Log
import android.webkit.WebView

import java.lang.ref.WeakReference

class LoadJSRunnable(webview: WebView, private val command: String) : Runnable {

    private val weakWebView: WeakReference<WebView> = WeakReference(webview)

    override fun run() {
        val webView = weakWebView.get()
        if (webView != null && webView.parent != null) {
            try {
                webView.loadUrl(command)
            } catch (e: Exception) {
                Log.w(TAG, "Unable execute the following command: " + command + ", the WebView may have been deallocated. " +
                        "Message: " + e.message)
            }

        }
    }

    companion object {
        private val TAG = WebViewHelper::class.java.simpleName
    }
}