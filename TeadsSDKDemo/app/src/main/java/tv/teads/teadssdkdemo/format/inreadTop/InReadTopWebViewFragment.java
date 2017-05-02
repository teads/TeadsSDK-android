package tv.teads.teadssdkdemo.format.inreadTop;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import tv.teads.sdk.publisher.TeadsAd;
import tv.teads.sdk.publisher.TeadsAdListener;
import tv.teads.sdk.publisher.TeadsConfiguration;
import tv.teads.sdk.publisher.TeadsContainerType;
import tv.teads.sdk.publisher.TeadsLog;
import tv.teads.sdk.publisher.TeadsObservableWebView;
import tv.teads.teadssdkdemo.MainActivity;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;
import tv.teads.utils.TeadsError;

/**
 * InReadTop format within a WebView
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class InReadTopWebViewFragment extends BaseFragment implements TeadsAdListener,
        DrawerLayout.DrawerListener {

    /**
     * Teads Ad instance
     */
    private TeadsAd mTeadsAd;

    /**
     * Your WebView extending the TeadsObservableWebView class
     */
    private TeadsObservableWebView mTeadsWebView;

    /**
     * The inReadTop ad view
     */
    private ViewGroup mInReadTopAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inreadtop_webview, container, false);

        mTeadsWebView = (TeadsObservableWebView) rootView.findViewById(R.id.webViewVideo);

        // Retrieve ad view
        mInReadTopAdView = (ViewGroup) rootView.findViewById(R.id.teads_adview);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        TeadsLog.setLogLevel(TeadsLog.LogLevel.verbose);
        // Load url in the WebView
        mTeadsWebView.loadUrl(this.getWebViewUrl());

        TeadsConfiguration teadsConfig = new TeadsConfiguration();
        teadsConfig.endScreenMode = getEndScreenMode();

        // Instanciate Teads Ad in inReadTop format
        mTeadsAd = new TeadsAd.TeadsAdBuilder(
                getActivity(),
                getPid())
                .viewGroup(mInReadTopAdView)
                .eventListener(this)
                .containerType(TeadsContainerType.inReadTop)
                .configuration(teadsConfig)
                .build();

        // Load the Ad
        mTeadsAd.load();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Attach listener to MainActivity to be notified when drawer is opened
        ((MainActivity) getActivity()).setDrawerListener(this);
        mTeadsAd.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).setDrawerListener(null);
        mTeadsAd.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mTeadsAd != null) {
            mTeadsAd.clean();
        }
    }

    @Subscribe
    public void onReloadEvent(ReloadEvent event) {
        if (mTeadsAd != null && !mTeadsAd.isLoaded()) {
            mTeadsAd.reset();
            mTeadsAd.load();
        }
    }

    /*----------------------------------------
    * implements TeadsAdListener
    */

    @Override
    public void teadsAdDidFailLoading(TeadsError teadsError) {
        try {
            Toast.makeText(this.getActivity(), getString(R.string.didfail), Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException ignored) {

        }
    }

    @Override
    public void teadsAdWillLoad() {

    }

    @Override
    public void teadsAdDidLoad() {

    }

    @Override
    public void teadsAdWillStart() {

    }

    @Override
    public void teadsAdDidStart() {

    }

    @Override
    public void teadsAdWillStop() {

    }

    @Override
    public void teadsAdDidStop() {

    }

    @Override
    public void teadsAdDidResume() {

    }

    @Override
    public void teadsAdDidPause() {

    }

    @Override
    public void teadsAdDidMute() {

    }

    @Override
    public void teadsAdDidUnmute() {

    }

    @Override
    public void teadsAdDidOpenInternalBrowser() {

    }

    @Override
    public void teadsAdDidClickBrowserClose() {

    }

    @Override
    public void teadsAdWillTakerOverFullScreen() {

    }

    @Override
    public void teadsAdDidTakeOverFullScreen() {

    }

    @Override
    public void teadsAdWillDismissFullscreen() {

    }

    @Override
    public void teadsAdDidDismissFullscreen() {

    }

    @Override
    public void teadsAdSkipButtonTapped() {

    }

    @Override
    public void teadsAdSkipButtonDidShow() {

    }

    @Override
    public void teadsAdWillExpand() {

    }

    @Override
    public void teadsAdDidExpand() {

    }

    @Override
    public void teadsAdWillCollapse() {

    }

    @Override
    public void teadsAdDidCollapse() {

    }

    @Override
    public void teadsAdDidClean() {

    }

    @Override
    public void teadsAdNoSlotAvailable() {

    }


    /*----------------------------------------
    * implements DrawerLayout.DrawerListener
    */

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mTeadsAd.requestPause();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mTeadsAd.requestResume();
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

}
