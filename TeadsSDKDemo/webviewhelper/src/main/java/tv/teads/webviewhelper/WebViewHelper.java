/*
 * Copyright (c) Teads 2018.
 */

package tv.teads.webviewhelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Helper to find , open, and close a slot in a the content of a {@link WebView}.
 * To do it, a JS ({@link } is injected in the webview
 */

class WebViewHelper implements JSInterface.Listener {

    private static final String TAG = WebViewHelper.class.getSimpleName();

    private final static String INSERT_SLOT_JS     = "javascript:window.utils.insertPlaceholder(%s);";
    private final static String UPDATE_SLOT_JS     = "javascript:window.utils.updatePlaceholder({" +
            "'offsetHeight':%s," +
            "'ratioVideo':%s" +
            "});";
    private final static String OPEN_SLOT_JS       = "javascript:window.utils.showPlaceholder();";
    private final static String CLOSE_SLOT_JS      = "javascript:window.utils.hidePlaceholder();";
    private final static String UPDATE_POSITION_JS = "javascript:window.utils.sendTargetGeometry();";

    private WebView mWebView;

    @Nullable
    private String mSelector;

    @Nullable
    private Listener mListener;

    private TimeoutCountdownTimer mRequestSlotTimeout;

    private Handler mMainThreadHandler;

    @SuppressLint("AddJavascriptInterface")
    private WebViewHelper(Builder builder) {
        JSInterface mJSInterface = new JSInterface(this);
        mWebView = builder.webview;
        mSelector = builder.selector;
        mListener = builder.listener;
        mMainThreadHandler = builder.handler;

        mWebView.addJavascriptInterface(mJSInterface, Constants.JAVASCRIPT_INTERFACE_TAG);

        mRequestSlotTimeout = new TimeoutCountdownTimer(Constants.WEBVIEW_TIMEOUT) {
            @Override
            protected void onTimeout() {
                Log.w(TAG, "Countdown timed out");
                if (mListener != null) {
                    mListener.onSlotNotFound();
                }
                reset();
            }
        };
    }

    /**
     * Inject the teads js in the webview
     *
     * @param context The application context
     */
    void injectJS(Context context) {
        if (mWebView != null && context != null) {
            InputStream inputStream = null;
            try {
                inputStream = context.getAssets().open("bootstrap.js");
                byte[] buffer = new byte[inputStream.available()];
                //noinspection ResultOfMethodCallIgnored
                inputStream.read(buffer);
                String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                mWebView.loadUrl("javascript:(function() {" +
                                         "var scriptElement = document.getElementById('teadsbootstrap');" +
                                         " if(scriptElement) scriptElement.remove();" +
                                         " var script = document.createElement('script');" +
                                         "script.innerHTML = window.atob('" + encoded + "');" +
                                         "script.setAttribute('id', 'teadsbootstrap');" +
                                         "script.setAttribute('type', 'text/javascript');" +
                                         " document.body.appendChild(script);})()");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Call the js in the main thread to insert the slot in the webview content.
     * If a {@link #mSelector} is specified, the bootstrap will use it
     */
    void insertSlot() {
        String command = String.format(INSERT_SLOT_JS, TextUtils.isEmpty(mSelector) ? "" : "'" + mSelector +
                "'");
        mMainThreadHandler.post(new LoadJSRunnable(mWebView, command));
        mRequestSlotTimeout.start();
    }

    /**
     * Update the slot height with the given ratio and offset
     *
     * @param ratio  the mediafile ratio
     * @param offset the offset
     */
    void updateSlot(final float ratio, final int offset) {
        mMainThreadHandler.post(new LoadJSRunnable(mWebView, String.format(UPDATE_SLOT_JS, offset, ratio)));
    }

    /**
     * Call the js in the main thread to open the slot
     */
    void openSlot() {
        mMainThreadHandler.post(new LoadJSRunnable(mWebView, OPEN_SLOT_JS));
    }

    /**
     * Call the js in the main thread to close the slot
     */
    void closeSlot() {
        mMainThreadHandler.post(new LoadJSRunnable(mWebView, CLOSE_SLOT_JS));
    }

    /**
     * Resets the internal state of the controller preparing it for
     * another use.
     */
    void reset() {
        mRequestSlotTimeout.cancel();
    }

    /**
     * Notify the js to sent us the slot position, as answer it should call {@link #onSlotUpdated(int, int, int, int, float)}
     */
    void askSlotPosition() {
        mMainThreadHandler.postDelayed(new LoadJSRunnable(mWebView, UPDATE_POSITION_JS), 200);
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////
     *
     *   Implements JS Interface
     *
     *///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onJsReady() {
        if (mListener != null) {
            mListener.onJsReady();
        }
    }

    @Override
    public void onSlotUpdated(int top, int left, int bottom, int right, float pixelRatio) {
        if (mListener == null) {
            mRequestSlotTimeout.cancel();
            return;
        }

        if (mRequestSlotTimeout.isTimeout()) {
            mListener.onSlotNotFound();
        } else {
            int l = (int) (pixelRatio * left);
            int t = (int) (pixelRatio * top);
            int r = (int) (pixelRatio * right);
            int b = (int) (pixelRatio * bottom);
            mListener.onSlotUpdated(l, t, r, b);
        }
        mRequestSlotTimeout.cancel();
    }

    @Override
    public void handleError(String error) {
        if (mListener != null) {
            mListener.onError(error);
        }
    }


    /*//////////////////////////////////////////////////////////////////////////////////////////////
     *
     *   Builder
     *
     *///////////////////////////////////////////////////////////////////////////////////////////////

    static class Builder {

        private WebView webview;

        private String selector;

        private Listener listener;

        private Handler handler;

        Builder(@NonNull WebView webView, @NonNull Listener listener, @NonNull String selector) {
            this.webview = webView;
            this.listener = listener;
            this.selector = selector;
        }

        @SuppressWarnings("unused")
        Builder mainThreadHandler(Handler handler) {
            this.handler = handler;
            return this;
        }

        WebViewHelper build() {
            if (this.handler == null) {
                this.handler = new Handler(Looper.getMainLooper());
            }
            return new WebViewHelper(this);
        }
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////
     *
     *   Listener
     *
     *///////////////////////////////////////////////////////////////////////////////////////////////

    public interface Listener {

        void onJsReady();

        void onSlotNotFound();

        void onSlotUpdated(int left, int top, int right, int bottom);

        void onError(String error);
    }

}
