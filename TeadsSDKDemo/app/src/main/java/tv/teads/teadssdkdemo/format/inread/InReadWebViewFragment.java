package tv.teads.teadssdkdemo.format.inread;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.greenrobot.eventbus.Subscribe;

import tv.teads.sdk.android.AdResponse;
import tv.teads.sdk.android.TeadsAd;
import tv.teads.sdk.android.TeadsAdView;
import tv.teads.sdk.android.TeadsListener;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;
import tv.teads.webviewhelper.ObservableWebView;
import tv.teads.webviewhelper.SyncWebViewTeadsAdView;

/**
 * InRead format within a WebView
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class InReadWebViewFragment extends BaseFragment implements TeadsListener, SyncWebViewTeadsAdView.Listener {

    /**
     * An observable webview to listen the scroll event in the goal to move the ad following the webview scroll
     */
    private ObservableWebView mWebview;

    private SyncWebViewTeadsAdView mWebviewHelperSynch;

    private TeadsAdView mAdView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inread_webview, container, false);
        mWebview = rootView.findViewById(R.id.webview);
        return rootView;
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View rootView, Bundle savedInstanceState) {

        mWebview = rootView.findViewById(R.id.webview);


        mAdView = new TeadsAdView(getContext());

        /*
        For a webview integration, we provide a example of tool to synchronise the ad view with the webview.
        You can find it in the webviewhelper module. {@see SyncWebViewTeadsAdView}
         */
        mWebviewHelperSynch = new SyncWebViewTeadsAdView(mWebview, mAdView,this, "h2");

        mAdView.setPid(getPid());
        mAdView.setListener(this);

        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setWebViewClient(new CustomWebviewClient(mWebviewHelperSynch));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        mWebview.loadUrl(this.getWebViewUrl());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mAdView != null) {
            mAdView.clean();
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onReloadEvent(ReloadEvent event) {
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * Ad view listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onAdResponse(TeadsAd teadsAd, AdResponse adResponse) {
        mWebviewHelperSynch.updateSlot(adResponse.getMediaRatio());
    }

    @Override
    public void displayAd(TeadsAd teadsAd, float v) {
        mWebviewHelperSynch.displayAd();
    }

    @Override
    public void closeAd(TeadsAd teadsAd, boolean b) {
        mWebviewHelperSynch.closeAd();
    }

    @Override
    public void onError(TeadsAd teadsAd, String s) {
        Log.w(InReadWebViewFragment.class.getSimpleName(), "TeadsAd playback failed, reason: " + s);
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * WebView helper listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onHelperReady() {
        //The helper is ready we can now load the ad
        if (mAdView != null) {
            mAdView.load();
        }
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * WebViewClient
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    private class CustomWebviewClient extends WebViewClient {

        private SyncWebViewTeadsAdView webviewHelperSynch;

        private CustomWebviewClient(SyncWebViewTeadsAdView webviewHelperSynch) {
            this.webviewHelperSynch = webviewHelperSynch;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            webviewHelperSynch.injectJS();

            super.onPageFinished(view, url);
        }
    }
}
