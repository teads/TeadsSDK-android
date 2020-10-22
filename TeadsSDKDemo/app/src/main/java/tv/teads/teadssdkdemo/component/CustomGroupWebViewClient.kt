package tv.teads.teadssdkdemo.component

import android.webkit.WebView
import android.webkit.WebViewClient
import tv.teads.webviewhelper.SyncWebViewViewGroup

class CustomGroupWebViewClient constructor(private val webviewHelperSynch: SyncWebViewViewGroup, private val mTitle: String)
    : WebViewClient() {

    override fun onPageFinished(view: WebView, url: String) {
        webviewHelperSynch.injectJS()

        view.evaluateJavascript("setTitle('$mTitle')") {}

        super.onPageFinished(view, url)
    }
}

