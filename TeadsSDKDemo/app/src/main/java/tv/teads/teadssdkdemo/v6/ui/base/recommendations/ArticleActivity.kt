package tv.teads.teadssdkdemo.v6.ui.base.recommendations

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

/**
 * Minimal in-app screen for organic recommendations. Replace it with your own article screen.
 */
class ArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient() // keep navigation inside the app
        }
        setContentView(webView)

        // Open the url as is, without any escaping or decoding
        webView.loadUrl(intent.getStringExtra(EXTRA_URL) ?: return)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        private const val EXTRA_URL = "extra_url"

        fun newIntent(context: Context, url: String): Intent =
            Intent(context, ArticleActivity::class.java).putExtra(EXTRA_URL, url)
    }
}
