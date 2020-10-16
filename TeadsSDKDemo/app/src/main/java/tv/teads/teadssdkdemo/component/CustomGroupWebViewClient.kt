package tv.teads.teadssdkdemo.component

import android.webkit.WebView
import android.webkit.WebViewClient
import tv.teads.webviewhelper.SyncWebViewTeadsAdView
import tv.teads.webviewhelper.SyncWebViewViewGroup

class CustomGroupWebViewClient constructor(private val webviewHelperSynch: SyncWebViewViewGroup) : WebViewClient() {

    override fun onPageFinished(view: WebView, url: String) {
        webviewHelperSynch.injectJS()

        super.onPageFinished(view, url)
    }
}

