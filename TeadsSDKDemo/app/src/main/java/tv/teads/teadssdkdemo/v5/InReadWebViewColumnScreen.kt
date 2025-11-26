package tv.teads.teadssdkdemo.v5

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import tv.teads.sdk.AdOpportunityTrackerView
import tv.teads.sdk.AdPlacementSettings
import tv.teads.sdk.AdRatio
import tv.teads.sdk.AdRequestSettings
import tv.teads.sdk.InReadAdViewListener
import tv.teads.sdk.TeadsSDK
import tv.teads.sdk.renderer.InReadAdView
import tv.teads.teadssdkdemo.BuildConfig
import tv.teads.teadssdkdemo.v5.utils.CustomInReadWebviewClient
import tv.teads.teadssdkdemo.v6.data.DemoSessionConfiguration
import tv.teads.webviewhelper.SyncAdWebView
import tv.teads.webviewhelper.baseView.ObservableWebView

private const val TAG = "InReadWebViewColumn"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun InReadWebViewColumnScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    // Track configuration for rotation handling
    var lastOrientation by remember { mutableIntStateOf(configuration.orientation) }

    // Create WebView - remember across recompositions
    val webView = remember {
        ObservableWebView(context).apply {
            settings.javaScriptEnabled = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            visibility = android.view.View.VISIBLE
        }
    }

    // Create ad placement - remember across recompositions
    val adPlacement = remember {
        // Enable more logging visibility for testing purposes
        TeadsSDK.testMode = BuildConfig.DEBUG

        val placementSettings = AdPlacementSettings.Builder()
            .enableDebug()
            .build()

        TeadsSDK.createInReadPlacement(
            context,
            DemoSessionConfiguration.getPlacementIdOrDefault().toInt(),
            placementSettings
        )
    }

    // Create helper with listener - remember across recompositions
    val webviewHelperSync = remember {
        var helperRef: SyncAdWebView? = null

        val helper = SyncAdWebView(
            context = context,
            webview = webView,
            listener = object : SyncAdWebView.Listener {
                override fun onHelperReady(adContainer: ViewGroup) {
                    Log.d(TAG, "Helper ready, requesting ad")

                    val helper = helperRef ?: return

                    val requestSettings = AdRequestSettings.Builder()
                        .pageSlotUrl(DemoSessionConfiguration.getArticleUrlOrDefault())
                        .build()

                    adPlacement.requestAd(requestSettings, object : InReadAdViewListener {
                        override fun adOpportunityTrackerView(trackerView: AdOpportunityTrackerView) {
                            helper.registerTrackerView(trackerView)
                            Log.d(TAG, "Tracker view registered")
                        }

                        override fun onAdReceived(ad: InReadAdView, adRatio: AdRatio) {
                            helper.registerAdView(ad)
                            helper.setAdRatio(adRatio)
                            Log.d(TAG, "Ad received with ratio: $adRatio")
                        }

                        override fun onAdRatioUpdate(adRatio: AdRatio) {
                            helper.setAdRatio(adRatio)
                            Log.d(TAG, "Ad ratio updated: $adRatio")
                        }

                        override fun onAdClosed() {
                            helper.closeAd()
                            Log.d(TAG, "Ad closed")
                        }

                        override fun onAdError(code: Int, description: String) {
                            helper.clean()
                            Log.e(TAG, "Ad error: $code - $description")
                        }

                        override fun onFailToReceiveAd(failReason: String) {
                            helper.clean()
                            Log.e(TAG, "Failed to receive ad: $failReason")
                        }

                        override fun onAdClicked() {
                            Log.d(TAG, "Ad clicked")
                        }

                        override fun onAdImpression() {
                            Log.d(TAG, "Ad impression")
                        }
                    })
                }
            },
            selector = "#teads-placement-slot"
        )

        helperRef = helper
        helper
    }

    // Handle WebView setup
    LaunchedEffect(Unit) {
        webView.webViewClient = CustomInReadWebviewClient(webviewHelperSync)
        webView.loadUrl("file:///android_asset/demo.html")
    }

    // Handle configuration changes (rotation)
    LaunchedEffect(configuration.orientation) {
        if (lastOrientation != configuration.orientation) {
            Log.d(TAG, "Configuration changed, notifying helper")
            webviewHelperSync.onConfigurationChanged()
            lastOrientation = configuration.orientation
        }
    }

    // Cleanup when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            Log.d(TAG, "Cleaning up WebView helper")
            webviewHelperSync.clean()
        }
    }

    AndroidView(
        factory = {
            FrameLayout(context).apply {
                addView(webView)
            }
        },
        modifier = modifier
    )
}
