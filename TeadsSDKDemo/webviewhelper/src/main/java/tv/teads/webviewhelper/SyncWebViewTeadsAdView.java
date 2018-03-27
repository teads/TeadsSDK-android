/*
 * Copyright (c) Teads 2017.
 */

package tv.teads.webviewhelper;


import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import tv.teads.sdk.android.TeadsAdView;
import tv.teads.sdk.android.engine.ui.view.ObservableAdView;

/**
 * Insert and Synchronise the scroll between the TeadsAdView and the webview
 * <p>
 * Created by Benjamin Volland on 31/08/2017.
 */

public class SyncWebViewTeadsAdView implements WebViewHelper.Listener,
                                                 ObservableWebView.OnScrollListener,
                                                 ObservableAdView.ActionMoveListener {

    private static final String TAG = SyncWebViewTeadsAdView.class.getSimpleName();

    /**
     * Layout containing the ad and the webview
     */
    @Nullable
    private FrameLayout mContainer;

    private TeadsAdView mAdView;

    private WebViewHelper mWebviewHelper;

    private ObservableWebView mWebview;

    private Listener mListener;

    private int mInitialY = 0;

    /**
     * Constructor
     *
     * @param webView  Webview with which we synchronize the scroll. {@link ObservableWebView}
     * @param adView   The adview with which we synchronize the scroll
     * @param listener The listener
     * @param selector The selector where we want insert the ad view
     */
    public SyncWebViewTeadsAdView(ObservableWebView webView, TeadsAdView adView, Listener listener, String selector) {
        mAdView = adView;
        mListener = listener;
        mAdView.setMoveListener(this);
        mWebview = webView;
        mWebview.setOnScrollListener(this);
        mWebviewHelper = new WebViewHelper.Builder(webView, this, selector)
                           .build();

    }

    /**
     * Inject the js in your webview. Should be call when the webview is ready
     */
    public void injectJS() {
        mWebviewHelper.injectJS(mWebview.getContext().getApplicationContext());
    }

    /**
     * Insert the teadsAdView and the webview in a FrameLayout,
     * and inject it in the old webview parent hierarchy
     */
    private void injectTeadsAdView() {
        if (!(mWebview.getParent() instanceof ViewGroup) || mAdView == null) {
            return;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup webViewParent = (ViewGroup) mWebview.getParent();
                mContainer = new FrameLayout(mWebview.getContext());
                mContainer.setLayoutParams(new ViewGroup.LayoutParams(mWebview.getLayoutParams()));
                int webviewPosition = 0;
                int childCount      = webViewParent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    if (webViewParent.getChildAt(i).equals(mWebview)) {
                        webviewPosition = i;
                    }
                }

                webViewParent.removeViewAt(webviewPosition);
                mWebview.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                                                                                                             .LayoutParams.MATCH_PARENT));
                mContainer.addView(mWebview);
                mAdView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                     ViewGroup.LayoutParams.WRAP_CONTENT));
                mContainer.addView(mAdView);

                webViewParent.addView(mContainer);
                mListener.onHelperReady();
            }
        });

    }

    /**
     * Clean the webview helper and the observable web view
     */
    @SuppressWarnings("unused")
    public void clean() {
        mWebview.clean();
        mWebviewHelper.reset();
    }


    /*//////////////////////////////////////////////////////////////////////////////////////////////
    *
    * WebviewHelper listener methods
    *
     *//////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onJsReady() {
        mWebviewHelper.insertSlot();
    }

    @Override
    public void onSlotNotFound() {
        Log.w(TAG, "No slot found.");
    }

    @Override
    public void onSlotUpdated(int left, int top, int right, int bottom) {
        if (mAdView == null) {
            return;
        }
        if (mAdView.getParent() == null) {
            injectTeadsAdView();
        }

        int width = right - left;

        mInitialY = top;
        mAdView.setTranslationY(mInitialY - mWebview.getScrollY());

        ((ViewGroup.MarginLayoutParams) mAdView.getLayoutParams()).leftMargin = left;
        mAdView.getLayoutParams().width = width;
    }

    @Override
    public void onError(String error) {
        Log.w(TAG, "An Error occurs during the webview slot managment");
    }

    public void updateSlot(final Float ratio) {
        if (ratio != null) {
            mWebviewHelper.updateSlot(ratio, 40);
        }
    }

    /**
     * Open the slot
     */
    public void displayAd() {
        mWebviewHelper.openSlot();
    }

    /**
     * Close the slot
     */
    public void closeAd() {
        mWebviewHelper.closeSlot();
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////
    *
    * Webview Scroll listener
    *
    *//////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onScroll(int l, int t) {
        if (mAdView != null) {
            mAdView.setTranslationY(mInitialY - t);
        }
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////
    *
    * AdView Move listener
    *
    *//////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onActionMove(int moveX, int moveY) {
        if (mWebview.getScrollY() + moveY < 0) {
            mWebview.scrollBy(moveX, -mWebview.getScrollY());
        } else {
            mWebview.scrollBy(moveX, moveY);
        }
    }

    public interface Listener {
        /**
         * Called when the adview has been ready to be used
         */
        void onHelperReady();
    }
}
