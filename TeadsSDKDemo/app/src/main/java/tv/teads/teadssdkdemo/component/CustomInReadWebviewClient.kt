package tv.teads.teadssdkdemo.component

import android.webkit.WebView
import android.webkit.WebViewClient
import tv.teads.webviewhelper.SyncWebViewTeadsAdView

class CustomInReadWebviewClient internal constructor(private val webviewHelperSynch: SyncWebViewTeadsAdView) : WebViewClient() {

    override fun onPageFinished(view: WebView, url: String) {
        webviewHelperSynch.injectJS()

        super.onPageFinished(view, url)
    }
}