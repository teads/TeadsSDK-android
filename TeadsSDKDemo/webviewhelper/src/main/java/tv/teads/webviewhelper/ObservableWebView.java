/*
 * Copyright (c) Teads 2017.
 */

package tv.teads.webviewhelper;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * This WebView dispatch scroll event to the register listener.
 * <p>
 * Created by Benjamin Volland on 13/09/2017.
 */

public class ObservableWebView extends WebView {

    private OnScrollListener mOnScrollListener;

    public ObservableWebView(final Context context) {
        super(context.getApplicationContext());
    }

    public ObservableWebView(final Context context, final AttributeSet attrs) {
        super(context.getApplicationContext(), attrs);
    }

    public ObservableWebView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context.getApplicationContext(), attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(l, t);
        }
    }

    public void setOnScrollListener(final OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    public void clean() {
        mOnScrollListener = null;
    }

    /**
     * Implement in the activity/fragment/view that you want to listen to the webview
     */
    public interface OnScrollListener {
        void onScroll(int l, int t);
    }

}
