package tv.teads.teadssdkdemo.v5

import android.webkit.WebView
import android.webkit.WebViewClient
import tv.teads.webviewhelper.SyncAdWebView

class CustomInReadWebviewClient internal constructor(
    private val webviewHelperSynch: SyncAdWebView,
    private val mTitle: String?
) : WebViewClient() {

    override fun onPageFinished(view: WebView, url: String) {
        webviewHelperSynch.injectJS()

        view.evaluateJavascript("setTitle('$mTitle')") {}

        super.onPageFinished(view, url)
    }
}
