package tv.teads.teadssdkdemo.format.inboard;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import tv.teads.sdk.adContent.AdContent;
import tv.teads.sdk.publisher.TeadsAdFactory;
import tv.teads.sdk.publisher.TeadsError;
import tv.teads.sdk.publisher.TeadsLog;
import tv.teads.sdk.publisher.TeadsNativeVideo;
import tv.teads.sdk.publisher.TeadsNativeVideoEventListener;
import tv.teads.teadssdkdemo.MainActivity;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;

/**
 * InBoard format within a ScrollView
 *
 * Created by Hugo Gresse on 30/03/15.
 */
public class InBoardAdFactoryScrollViewFragment extends BaseFragment implements TeadsNativeVideoEventListener,
        DrawerLayout.DrawerListener, TeadsAdFactory.Listener {

    /**
     * Teads Native Video instance
     */
    private TeadsNativeVideo    mTeadsNativeVideo;

    /**
     * Your FrameLayout used to display video in
     */
    private FrameLayout         mFrameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_native_inboard_scrollview, container, false);
        mFrameLayout = (FrameLayout) rootView.findViewById(R.id.ad_framelayout);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        TeadsLog.setLogLevel(TeadsLog.LogLevel.verbose);
        // Instanciate Teads Native Video in inboard format
        mTeadsNativeVideo = new TeadsNativeVideo(
                this.getActivity(),
                mFrameLayout,
                this.getPid(),
                TeadsNativeVideo.NativeVideoContainerType.inBoard,
                this);

        if(TeadsAdFactory.getInstance(getActivity()).isLoaded(getPid(), AdContent.PlacementAdType.PlacementAdTypeNativeVideo)){
            // Load the Ad if loaded
            mTeadsNativeVideo.loadFromAdFactory();
        } else {
            // Subscribe to listener
            TeadsAdFactory.getInstance(getActivity()).setListener(this);
            // Prevent null ad if already poped
            TeadsAdFactory.getInstance(getActivity()).loadAdContent(getPid(), AdContent.PlacementAdType.PlacementAdTypeNativeVideo);
        }
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

        TeadsAdFactory.getInstance(getActivity()).setListener(null);

        if(mTeadsNativeVideo != null){
            mTeadsNativeVideo.clean();
        }
    }


    /*----------------------------------------
    * implements TeadsNativeVideoEventListener
    */

    @Override
    public void teadsVideoDidFailLoading(TeadsError teadsError) {
        try {
            Toast.makeText(this.getActivity(), getString(R.string.didfail), Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException ignored){

        }
    }

    @Override
    public void teadsVideoWillLoad() {

    }

    @Override
    public void teadsVideoDidLoad() {

    }

    @Override
    public void teadsVideoWillStart() {

    }

    @Override
    public void teadsVideoDidStart() {

    }

    @Override
    public void teadsVideoWillStop() {

    }

    @Override
    public void teadsVideoDidStop() {

    }

    @Override
    public void teadsVideoDidResume() {

    }

    @Override
    public void teadsVideoDidPause() {

    }

    @Override
    public void teadsVideoDidMute() {

    }

    @Override
    public void teadsVideoDidUnmute() {

    }

    @Override
    public void teadsVideoDidOpenInternalBrowser() {

    }

    @Override
    public void teadsVideoDidClickBrowserClose() {

    }

    @Override
    public void teadsVideoWillTakerOverFullScreen() {

    }

    @Override
    public void teadsVideoDidTakeOverFullScreen() {

    }

    @Override
    public void teadsVideoWillDismissFullscreen() {

    }

    @Override
    public void teadsVideoDidDismissFullscreen() {

    }

    @Override
    public void teadsVideoSkipButtonTapped() {

    }

    @Override
    public void teadsVideoSkipButtonDidShow() {

    }

    @Override
    public void teadsVideoWillExpand() {

    }

    @Override
    public void teadsVideoDidExpand() {

    }

    @Override
    public void teadsVideoWillCollapse() {

    }

    @Override
    public void teadsVideoDidCollapse() {

    }

    @Override
    public void teadsVideoDidClean() {

    }

    @Override
    public void teadsVideoWebViewNoSlotAvailable() {

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


    /*----------------------------------------
    * implements TeadsAdFactory.Listener
    */

    @Override
    public void adFactoryDidFailLoading(String s, TeadsError teadsError) {

    }

    @Override
    public void adFactoryWillLoad(String s, AdContent.PlacementAdType placementAdType) {

    }

    @Override
    public void adFactoryDidLoad(String s, AdContent.PlacementAdType placementAdType) {
        if(s.equals(getPid()) && placementAdType == AdContent.PlacementAdType.PlacementAdTypeNativeVideo){
            mTeadsNativeVideo.loadFromAdFactory();
        }
    }

    @Override
    public void adFactoryHasConsumed(String s, AdContent.PlacementAdType placementAdType) {

    }

    @Override
    public void adFactoryDidExpire(String s, AdContent.PlacementAdType placementAdType) {

    }
}
