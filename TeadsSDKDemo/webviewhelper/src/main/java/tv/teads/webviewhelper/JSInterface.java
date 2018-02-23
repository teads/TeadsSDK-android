/*
 * Copyright (c) Teads 2017.
 */

package tv.teads.webviewhelper;

import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

/**
 * Callback from Javascript JavaBridge. Use to get callback from WebView after registering a
 * JavascriptInterface
 * <p/>
 * Created by Benjamin Volland on 28/08/2017.
 */
class JSInterface {

    @NonNull
    private Listener mListener;

    JSInterface(@NonNull Listener mListener) {
        this.mListener = mListener;
    }

    /**
     * Callback invoked by teads Teads js bootstrap when lib is injected and ready for
     * processing.
     */
    @SuppressWarnings("unused")
    @JavascriptInterface
    public void onTeadsJsLibReady() {
        mListener.onJsReady();
    }

    /**
     * Called when the HTML placeholder has been inserted into DOM
     *
     * @param top        pixel value (web)
     * @param left       pixel value (web)
     * @param bottom     pixel value (web)
     * @param right      pixel value (web)
     * @param pixelRatio corresponding to screen ratio
     */
    @SuppressWarnings("unused")
    @JavascriptInterface
    public void onSlotUpdated(int top, int left,
                              int bottom, int right,
                              float pixelRatio) {
        mListener.onSlotUpdated(top, left, bottom, right, pixelRatio);
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void onSlotStartShow() {
        mListener.onSlotStartShow();
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void onSlotStartHide() {
        mListener.onSlotStartHide();
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void handleError(String error) {
        mListener.handleError(error);
    }

    /**
     * Javascript interface listener, provide method to synchronize the ad with the slot
     */
    interface Listener {

        void onJsReady();

        void onSlotUpdated(int top, int left,
                           int bottom, int right,
                           float pixelRatio);

        void onSlotStartShow();

        void onSlotStartHide();

        void handleError(String error);
    }
}
