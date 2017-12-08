package tv.teads.teadssdkdemo.format.inread;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import teads.tv.webviewhelper.ObservableWebView;
import teads.tv.webviewhelper.TeadsWebViewSynchronizer;
import tv.teads.sdk.android.AdResponse;
import tv.teads.sdk.android.TeadsAd;
import tv.teads.sdk.android.TeadsAdView;
import tv.teads.sdk.android.TeadsListener;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;

/**
 * InRead format within a WebView
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class InReadWebViewFragment extends BaseFragment implements TeadsListener {

    boolean mIsTeadsJSReady = false;

    /**
     * An observable webview to listen the scroll event in the goal to move the ad following the webview scroll
     */
    private ObservableWebView mWebview;

    private TeadsAdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inread_webview, container, false);
        mWebview = rootView.findViewById(R.id.webview);
        return rootView;
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {

        mWebview = rootView.findViewById(R.id.webview);


        mAdView = new TeadsAdView(getContext());
        TeadsWebViewSynchronizer mWebviewHelperSynch = new TeadsWebViewSynchronizer(mWebview, mAdView, ".module");

        mAdView.setPid(getPid());

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
        if (mAdView != null) {
            mAdView.load();
        }
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
    * implements TeadsAdListener
    *//////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onAdResponse(TeadsAd teadsAd, AdResponse adResponse) {
        if (!adResponse.isSuccessful()) {
            Toast.makeText(this.getActivity(), getString(R.string.didfail), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void displayAd(TeadsAd teadsAd, float i) {

    }

    @Override
    public void closeAd(TeadsAd teadsAd, boolean b) {

    }

    @Override
    public void onError(TeadsAd teadsAd, String s) {
        Toast.makeText(this.getActivity(), getString(R.string.didfail_playback), Toast.LENGTH_SHORT).show();
    }


    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * WebViewClient
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    private class CustomWebviewClient extends WebViewClient {

        private TeadsWebViewSynchronizer webviewHelperSynch;

        private CustomWebviewClient(TeadsWebViewSynchronizer webviewHelperSynch) {
            this.webviewHelperSynch = webviewHelperSynch;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (mIsTeadsJSReady) {
                return;
            }
            webviewHelperSynch.injectJS();

            super.onPageFinished(view, url);
        }
    }
}
