package tv.teads.teadssdkdemo.component

import android.webkit.WebView
import android.webkit.WebViewClient
import tv.teads.webviewhelper.SyncWebViewTeadsAdView

class CustomInReadWebviewClient internal constructor(private val webviewHelperSynch: SyncWebViewTeadsAdView, private val mTitle: String)
    : WebViewClient() {

    override fun onPageFinished(view: WebView, url: String) {
        webviewHelperSynch.injectJS()

        view.evaluateJavascript("setTitle('$mTitle')") {}

        super.onPageFinished(view, url)
    }
}