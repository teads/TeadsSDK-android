/*
 * Copyright (c) Teads 2017.
 */

package teads.tv.webviewhelper;

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

import java.io.InputStream;

/**
 * Helper to find , open, and close a slot in a the content of a {@link WebView}.
 * To do it, a JS ({@link } is injected in the webview
 * Created by Benjamin Volland on 11/08/2017.
 */

public class WebViewHelper implements JSInterface.Listener {

    private static final String TAG = WebViewHelper.class.getSimpleName();

    private final static String INSERT_SLOT_JS = "javascript:window.utils.insertPlaceholder(%s);";
    private final static String UPDATE_SLOT_JS = "javascript:window.utils.updatePlaceholder({" +
                                                         "'offsetHeight':%s," +
                                                         "'ratioVideo':%s" +
                                                         "});";
    private final static String OPEN_SLOT_JS   = "javascript:window.utils.showPlaceholder('%d');";
    private final static String CLOSE_SLOT_JS  = "javascript:window.utils.hidePlaceholder('%d');";

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
     * Inject the teads bootstrap in the webview
     *
     * @param context The application context
     */
    public void injectJS(Context context) {
        if (mWebView != null && context != null) {
            try {
                InputStream inputStream = context.getAssets().open("bootstrap.js");
                byte[]      buffer      = new byte[inputStream.available()];
                //noinspection ResultOfMethodCallIgnored
                inputStream.read(buffer);
                inputStream.close();
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
            }
        }
    }

    /**
     * Call the bootstrap in the main thread to insert the slot in the webview content.
     * If a {@link #mSelector} is specified, the bootstrap will use it
     */
    public void insertSlot() {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {

                String command = String.format(INSERT_SLOT_JS, TextUtils.isEmpty(mSelector) ? "" : "'" + mSelector + "'");

                try {
                    mWebView.loadUrl(command);
                } catch (Exception e) {
                    Log.e(TAG, "Unable to insertPlaceholder, the WebView may have been deallocated. " +
                                       "Message: " + e.getMessage());
                }
            }
        });
        mRequestSlotTimeout.start();
    }

    /**
     * Update the slot height with the given ratio and offset
     *
     * @param ratio  the mediafile ratio
     * @param offset the offset
     */
    public void updateSlot(final float ratio, final int offset) {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mWebView.loadUrl(String.format(UPDATE_SLOT_JS, offset, ratio));
                } catch (Exception ignored) {
                    Log.e(TAG, "Unable to updatePlaceholder, the WebView may have been deallocated.");
                }
            }
        });
    }

    /**
     * Call the bootstrap in the main thread to open the slot
     */
    public void openSlot() {
        mMainThreadHandler.post(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                try {
                    mWebView.loadUrl(String.format(OPEN_SLOT_JS, 0));
                } catch (Exception e) {
                    Log.e(TAG, "Unable to open slot, the WebView may have been deallocated.. " +
                                       "Message: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Call the bootstrap in the main thread to close the slot
     */
    public void closeSlot() {
        mMainThreadHandler.post(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                try {
                    mWebView.loadUrl(String.format(CLOSE_SLOT_JS, 0));
                } catch (Exception e) {
                    Log.e(TAG, "Unable to close slot, the WebView may have been deallocated.. " +
                                       "Message: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Resets the internal state of the controller preparing it for
     * another use.
     */
    public void reset() {
        mRequestSlotTimeout.cancel();
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
    public void onSlotStartShow() {
        if (mListener != null) {
            mListener.onSlotStartShow();
        }
    }

    @Override
    public void onSlotStartHide() {
        if (mListener != null) {
            mListener.onSlotStartHide();
        }
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

    public static class Builder {

        private WebView webview;

        private String selector;

        private Listener listener;

        private Handler handler;

        public Builder(@NonNull WebView webView, @NonNull Listener listener, @NonNull String selector) {
            this.webview = webView;
            this.listener = listener;
            this.selector = selector;
        }

        @SuppressWarnings("unused")
        public Builder mainThreadHandler(Handler handler) {
            this.handler = handler;
            return this;
        }

        public WebViewHelper build() {
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

        void onSlotStartShow();

        void onSlotStartHide();

        void onError(String error);
    }

}
