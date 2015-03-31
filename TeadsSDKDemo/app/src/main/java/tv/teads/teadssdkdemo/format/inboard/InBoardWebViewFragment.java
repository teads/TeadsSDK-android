package tv.teads.teadssdkdemo.format.inboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import tv.teads.sdk.publisher.TeadsError;
import tv.teads.sdk.publisher.TeadsNativeVideo;
import tv.teads.sdk.publisher.TeadsNativeVideoEventListener;
import tv.teads.sdk.publisher.TeadsObservableWebView;
import tv.teads.teadssdkdemo.MainActivity;
import tv.teads.teadssdkdemo.R;

/**
 * InBoard format within a WebView
 *
 * Created by Hugo Gresse on 30/03/15.
 */
public class InBoardWebViewFragment extends Fragment implements TeadsNativeVideoEventListener,
        DrawerLayout.DrawerListener {

    /**
     * Teads Native Video instance
     */
    private TeadsNativeVideo       mTeadsNativeVideo;

    /**
     * Your WebView extending the TeadsObservableWebView class
     */
    private TeadsObservableWebView mTeadsWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_native_inboard_webview, container, false);
        mTeadsWebView = (TeadsObservableWebView) rootView.findViewById(R.id.webViewNativeVideo);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        // Load url in the WebView
        mTeadsWebView.loadUrl("http://mobile.lemonde.fr/planete/article/2015/01/24/la-grande-barr" +
                "iere-de-corail-bientot-debarrassee-des-dechets-de-dragage_4562880_3244.html");

        // Instanciate Teads Native Video in inboard format
        mTeadsNativeVideo = new TeadsNativeVideo(
                this.getActivity(),
                mTeadsWebView,
                "27695",
                TeadsNativeVideo.NativeVideoContainerType.inBoard,
                this);

        // Load the Ad
        mTeadsNativeVideo.load();
    }

    @Override
    public void onResume(){
        super.onResume();
        // Attach listener to MainActivity to be notified when drawer is opened
        ((MainActivity)getActivity()).setDrawerListener(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        ((MainActivity)getActivity()).setDrawerListener(null);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mTeadsNativeVideo.clean();
    }


    /*----------------------------------------
    * implements TeadsNativeVideoEventListener
    */

    @Override
    public void didFailLoading(TeadsError teadsError) {
        Toast.makeText(this.getActivity(), getString(R.string.didfail), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void nativeVideoWillLoad() {

    }

    @Override
    public void nativeVideoDidLoad() {

    }

    @Override
    public void nativeVideoWillStart() {

    }

    @Override
    public void nativeVideoDidStart() {

    }

    @Override
    public void nativeVideoWillStop() {

    }

    @Override
    public void nativeVideoDidStop() {

    }

    @Override
    public void nativeVideoDidResume() {

    }

    @Override
    public void nativeVideoDidPause() {

    }

    @Override
    public void nativeVideoDidMute() {

    }

    @Override
    public void nativeVideoDidUnmute() {

    }

    @Override
    public void nativeVideoDidOpenInternalBrowser() {

    }

    @Override
    public void nativeVideoDidClickBrowserClosed() {

    }

    @Override
    public void nativeVideoWillTakerOverFullScreen() {

    }

    @Override
    public void nativeVideoDidTakeOverFullScreen() {

    }

    @Override
    public void nativeVideoWillDismiss() {

    }

    @Override
    public void nativeVideoDidDismiss() {

    }

    @Override
    public void nativeVideoSkipButtonTapped() {

    }

    @Override
    public void nativeVideoSkipButtonDidShow() {

    }

    @Override
    public void nativeVideoWillExpand() {

    }

    @Override
    public void nativeVideoDidExpand() {

    }

    @Override
    public void nativeVideoWillCollapse() {

    }

    @Override
    public void nativeVideoDidCollapse() {

    }


    /*----------------------------------------
    * implements DrawerLayout.DrawerListener
    */

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mTeadsNativeVideo.requestPause();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mTeadsNativeVideo.requestResume();
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
