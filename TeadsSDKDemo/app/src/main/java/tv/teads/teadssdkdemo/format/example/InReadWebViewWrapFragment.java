package tv.teads.teadssdkdemo.format.example;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import tv.teads.sdk.android.AdFailedReason;
import tv.teads.sdk.android.AdSettings;
import tv.teads.sdk.android.InReadAdView;
import tv.teads.sdk.android.TeadsListener;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;
import tv.teads.webviewhelper.ObservableWebView;
import tv.teads.webviewhelper.SyncWebViewTeadsAdView;

/**
 * InRead format within a WebView where the WebView scroll is managed by a ScrollView and not the WebView it self,
 * equal to have the WebView height set to wrap_content.
 */
public class InReadWebViewWrapFragment extends BaseFragment implements SyncWebViewTeadsAdView.Listener {

    /**
     * An observable webview to listen the scroll event in the goal to move the ad following the webview scroll
     */
    private ObservableWebView mWebview;

    private SyncWebViewTeadsAdView mWebviewHelperSynch;

    private InReadAdView mAdView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inread_webview_wrap, container, false);
        mWebview = rootView.findViewById(R.id.webview);
        return rootView;
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View rootView, Bundle savedInstanceState) {

        mWebview = rootView.findViewById(R.id.webview);


        mAdView = new InReadAdView(getContext());

        /*
        For a webview integration, we provide a example of tool to synchronise the ad view with the webview.
        You can find it in the webviewhelper module. {@see SyncWebViewTeadsAdView}
         */
        mWebviewHelperSynch = new SyncWebViewTeadsAdView(mWebview, mAdView, this, "h2");

        mAdView.setPid(getPid());
        mAdView.enableDebug();
        mAdView.setListener(mTeadsListener);

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mWebviewHelperSynch != null) {
            mWebviewHelperSynch.onConfigurationChanged();
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onReloadEvent(ReloadEvent event) {
        // Not used
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * Ad view listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    private final TeadsListener mTeadsListener = new TeadsListener() {

        @Override
        public void onAdFailedToLoad(AdFailedReason adFailedReason) {
            Toast.makeText(InReadWebViewWrapFragment.this.getActivity(), getString(R.string.didfail), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(String s) {
            Toast.makeText(InReadWebViewWrapFragment.this.getActivity(), getString(R.string.didfail_playback), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdLoaded(float adRatio) {
            mWebviewHelperSynch.updateSlot(adRatio);
            mWebviewHelperSynch.displayAd();
        }

        @Override
        public void closeAd() {
            mWebviewHelperSynch.closeAd();
        }
    };

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * WebView helper listener
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onHelperReady(@NonNull ViewGroup adContainer) {
        //The helper is ready we can now load the ad
        if (mAdView != null) {
            mAdView.setAdContainerView(adContainer);
            mAdView.load(new AdSettings.Builder().pageUrl("https://example.com/article1").build());
        }
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
     * WebViewClient
     *//////////////////////////////////////////////////////////////////////////////////////////////////

    private class CustomWebviewClient extends WebViewClient {

        private final SyncWebViewTeadsAdView webviewHelperSynch;

        private CustomWebviewClient(SyncWebViewTeadsAdView webviewHelperSynch) {
            this.webviewHelperSynch = webviewHelperSynch;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            webviewHelperSynch.injectJS();

            super.onPageFinished(view, url);
        }
    }
}
