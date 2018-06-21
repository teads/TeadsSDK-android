<<<<<<< HEAD
=======
/*
 * Copyright (c) Teads 2018.
 */

>>>>>>> origin/update_to_sdk_V4
package tv.teads.webviewhelper;

import android.util.Log;
import android.webkit.WebView;

import java.lang.ref.WeakReference;

/**
 * Created by Benjamin Volland on 20/04/2018.
 */
public class LoadJSRunnable implements Runnable {


    private static final String TAG = WebViewHelper.class.getSimpleName();

    private final WeakReference<WebView> mWeakWebView;
    private final String                 mCommand;

    public LoadJSRunnable(WebView webview, String command) {
        mWeakWebView = new WeakReference<>(webview);
        mCommand = command;
    }

    @Override
    public void run() {
        WebView webView = mWeakWebView.get();
        if (webView != null && webView.getParent() != null) {
            try {
                webView.loadUrl(mCommand);
            } catch (Exception e) {
                Log.w(TAG, "Unable execute the following command: " + mCommand + ", the WebView may have been deallocated. " +
                        "Message: " + e.getMessage());
            }
        }
    }
}