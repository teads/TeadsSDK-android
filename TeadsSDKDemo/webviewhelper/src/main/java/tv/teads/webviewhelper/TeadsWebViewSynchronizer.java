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

import tv.teads.sdk.android.AdResponse;
import tv.teads.sdk.android.TeadsAd;
import tv.teads.sdk.android.TeadsAdView;
import tv.teads.sdk.android.TeadsListener;
import tv.teads.sdk.android.engine.ui.view.ObservableAdView;

/**
 * Insert and Synchronise the scroll between the TeadsAdView and the webview
 * <p>
 * Created by Benjamin Volland on 31/08/2017.
 */

public class TeadsWebViewSynchronizer implements WebViewHelper.Listener,
                                                   TeadsListener,
                                                   ObservableWebView.OnScrollListener,
                                                   ObservableAdView.ActionMoveListener {

    private static final String TAG = TeadsWebViewSynchronizer.class.getSimpleName();

    /**
     * Layout containing the ad and the webview
     */
    @Nullable
    private FrameLayout mContainer;

    private TeadsAdView mTeadsAdView;

    private WebViewHelper mWebviewHelper;

    private ObservableWebView mWebview;

    private int mInitialY = 0;


    public TeadsWebViewSynchronizer(ObservableWebView webView, TeadsAdView teadsAdView, String selector) {
        mTeadsAdView = teadsAdView;

        mTeadsAdView.setListener(this);
        mTeadsAdView.setMoveListener(this);
        mWebview = webView;
        mWebview.setOnScrollListener(this);
        mWebviewHelper = new WebViewHelper.Builder(webView, this, selector)
                           .build();

    }

    public void injectJS() {
        mWebviewHelper.injectJS(mWebview.getContext().getApplicationContext());
    }

    /**
     * Insert the teadsAdView and the webview in a FrameLayout,
     * and inject it in the old webview parent hierarchy
     */
    private void injectTeadsAdView() {
        if (!(mWebview.getParent() instanceof ViewGroup) || mTeadsAdView == null) {
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
                mTeadsAdView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                          ViewGroup.LayoutParams.WRAP_CONTENT));
                mContainer.addView(mTeadsAdView);

                webViewParent.addView(mContainer);
                mTeadsAdView.load();
            }
        });

    }

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
        if (mTeadsAdView == null) {
            return;
        }
        if (mTeadsAdView.getParent() == null) {
            injectTeadsAdView();
        }

        int width = right - left;

        mInitialY = top;
        mTeadsAdView.setTranslationY(mInitialY - mWebview.getScrollY());

        ((ViewGroup.MarginLayoutParams)mTeadsAdView.getLayoutParams()).leftMargin = left;
        mTeadsAdView.getLayoutParams().width = width;
    }

    @Override
    public void onSlotStartShow() {

    }

    @Override
    public void onSlotStartHide() {

    }

    @Override
    public void onError(String error) {
        Log.w(TAG, "An Error occurs during the webview slot managment");
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////
   *
   * Teads ad listener
   *
    *//////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onAdResponse(TeadsAd teadsAd, AdResponse adResponse) {
        if (adResponse.getMediaRatio() != null) {
            mWebviewHelper.updateSlot(adResponse.getMediaRatio(), 40);
        }

    }

    @Override
    public void displayAd(TeadsAd teadsAd, float v) {
        mWebviewHelper.openSlot();
    }

    @Override
    public void closeAd(TeadsAd teadsAd, boolean userAction) {
        mWebviewHelper.closeSlot();
    }

    @Override
    public void onError(TeadsAd teadsAd, String s) {
        Log.w(TAG, "TeadsAd playback failed, reason: " + s);

    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////
    *
    * Webview Scroll listener
    *
    *//////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onScroll(int l, int t) {
        if (mTeadsAdView != null) {
            mTeadsAdView.setTranslationY(mInitialY - t);
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
}
