package tv.teads.teadssdkdemo.v5.utils

import android.webkit.WebView
import android.webkit.WebViewClient
import tv.teads.webviewhelper.SyncAdWebView

class CustomInReadWebviewClient internal constructor(
    private val webviewHelperSynch: SyncAdWebView
) : WebViewClient() {

    override fun onPageFinished(view: WebView, url: String) {
        webviewHelperSynch.injectJS()
        super.onPageFinished(view, url)
    }
}
