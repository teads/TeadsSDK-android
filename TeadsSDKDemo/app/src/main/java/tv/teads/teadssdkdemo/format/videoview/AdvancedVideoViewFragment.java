package tv.teads.teadssdkdemo.format.videoview;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.ListView;

import tv.teads.sdk.publisher.TeadsError;
import tv.teads.sdk.publisher.TeadsNativeVideoEventListener;
import tv.teads.sdk.publisher.TeadsVideoView;
import tv.teads.teadssdkdemo.MainActivity;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.format.adapter.VideoViewCustomAdapter;
import tv.teads.teadssdkdemo.utils.BaseFragment;

/**
 * Custom format base on {@link TeadsVideoView} that will display the ad every 20 items of the ListView.
 * This sample is supplied for a demostration of TeadsVideoView. It may contains cases that is not managed.
 *
 *
 * <p/>
 * Created by Hugo Gresse on 06/08/15.
 */
public class AdvancedVideoViewFragment extends BaseFragment implements
        TeadsNativeVideoEventListener,
        VideoViewCustomAdapter.ExternalAdapterListener,
        AbsListView.OnScrollListener,
        DrawerLayout.DrawerListener {

    public static final String LOG_TAG = "AdvancedVideoViewFrag";

    private static final int sRepeatableAdPosition = 20;

    /**
     * ListView used as the root layout in this fragment
     */
    private ListView mListView;

    /**
     * A VideoView displaying the ad as a part of ListView
     */
    private TeadsVideoView mTeadsVideoView;

    /**
     * Flag to manage Ad State
     */
    private boolean mIsAnimating;
    private boolean mIsOpen;
    private boolean mIsFullscreen;

    /**
     * Prevent loading multiple time the Ad is case of fast scroll
     */
    private boolean mLoadCalled;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_native_inread_listview, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Attach scroll listener to know when we should play or pause the ad
        mListView.setOnScrollListener(this);

        // Set a very custom listview adapter
        setListViewAdapter(mListView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mTeadsVideoView != null){
            // reset views and flags
            mIsAnimating = mIsOpen = mIsFullscreen = mLoadCalled = false;
            mTeadsVideoView.clean();
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

    /**
     * Open the VideoView with expand animation
     */
    private void openInRead() {
        // Update inner videoView size
        mTeadsVideoView.updateSize();

        // Reset the state to collapsed to prevent wrong height
        mTeadsVideoView.setCollapsed();

        // Expand TeadsVideoView. It's not mandatory to use TeadsVideoView expand but it make think easier
        // for you if you use it.
        mTeadsVideoView.expand(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(LOG_TAG, "onAnimationStart");
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(LOG_TAG, "onAnimationEnd");
                mIsOpen = true;
                mIsAnimating = false;

                // Displayed the controls (mute button, skip button, progressBar)
                mTeadsVideoView.setControlVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * Close VideoView with collapse animation
     */
    private void closeInRead() {
        // Collapse TeadsVideoView. It's not mandatory to use TeadsVideoView collapse but it make think easier
        // for you if you use it.
        mTeadsVideoView.collapse(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsAnimating = mIsOpen = false;
                // TeadsVideoView reset by himself once collapsed
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    /**
     * Called from the {@link VideoViewCustomAdapter}
     *
     * @param layout the new created VideoView
     */
    @Override
    public void onVideoChanged(TeadsVideoView layout) {
        Log.d(LOG_TAG, "onVideoChanged");
        mTeadsVideoView = layout;

        if(!mTeadsVideoView.isLoaded() && !mLoadCalled){
            mLoadCalled = true;
            mTeadsVideoView.init(getActivity(), getPid(), this);
            mTeadsVideoView.requestLayout();
            mTeadsVideoView.load();
        }

        if (!mIsOpen) {
            mTeadsVideoView.setCollapsed();
        }
    }

    /**
     * Create sample data for the ListView and attach the custom adapter to it with the correct Ad position.
     *
     * @param listView the LsitView to be indlated
     */
    private void setListViewAdapter(ListView listView) {
        String values[] = new String[200];

        for (int i = 0; i < values.length; i++) {
            values[i] = "Teads " + i;
        }

        // Instantiate the custom adapter used to display VideoView in the ListView
        VideoViewCustomAdapter videoViewCustomAdapter = new VideoViewCustomAdapter(
                getActivity(),
                values,
                sRepeatableAdPosition,
                this);

        listView.setAdapter(videoViewCustomAdapter);
    }

    /*----------------------------------------
    * implements AbsListView.OnScrollListener
    */

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /**
     * Called on ListView scroll. we have to check the video visibility by :
     * - check if the displayed items contains the VideoView (the VideoView is placed every 20 items)
     * - if visible, requestResume if not already playing
     * - if not visible, requestPlay
     *
     * @param view             the visible view
     * @param firstVisibleItem firstVisibleItem
     * @param visibleItemCount number of item displayed
     * @param totalItemCount   total listview items
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mTeadsVideoView == null || mIsAnimating || mIsFullscreen || !mTeadsVideoView.isLoaded()) {
            return;
        }

        boolean isVisible = false;

        if ((visibleItemCount) >= sRepeatableAdPosition || firstVisibleItem % sRepeatableAdPosition == 0) {
            // Log.d(LOG_TAG, "vis1");
            isVisible = true;
        }

        if (!isVisible &&
                (firstVisibleItem % sRepeatableAdPosition) > ((firstVisibleItem + visibleItemCount) % sRepeatableAdPosition)
                && (firstVisibleItem + visibleItemCount) % 20 != 0
                && visibleItemCount > 1) {
            // Log.d(LOG_TAG, "vis2");
            isVisible = true;
        }

        if (isVisible && !mTeadsVideoView.isVisible()) {
            // Log.d(LOG_TAG, "not vis3");
            isVisible = false;
        }

        if (isVisible && !mTeadsVideoView.isPlaying()) {
            mTeadsVideoView.requestResume();
            mTeadsVideoView.setVisibility(View.VISIBLE);
        } else if (!isVisible && mTeadsVideoView.isPlaying()) {
            mTeadsVideoView.requestPause();
        }
    }
    
    
    /*----------------------------------------
    * implements TeadsNativeVideoEventListener
    */

    @Override
    public void nativeVideoDidFailLoading(TeadsError error) {
        mLoadCalled = false;
    }

    @Override
    public void nativeVideoWillLoad() {

    }

    @Override
    public void nativeVideoDidLoad() {
        Log.d(LOG_TAG, "nativeVideoDidLoad");

        mLoadCalled = false;

        if (mTeadsVideoView != null) {

            if (!mIsOpen) {
                openInRead();
            }
        }
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
    public void nativeVideoDidClickBrowserClose() {

    }

    @Override
    public void nativeVideoWillTakerOverFullScreen() {

    }

    @Override
    public void nativeVideoDidTakeOverFullScreen() {
        mIsFullscreen = true;
    }

    @Override
    public void nativeVideoWillDismissFullscreen() {

    }

    @Override
    public void nativeVideoDidDismissFullscreen() {
        mIsFullscreen = false;
    }

    @Override
    public void nativeVideoSkipButtonTapped() {
        if (mTeadsVideoView != null) {
            closeInRead();
        }
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
        if (mTeadsVideoView != null) {
            closeInRead();
        }
        mLoadCalled = false;
    }

    @Override
    public void nativeVideoDidClean() {

    }

    @Override
    public void nativeVideoWebViewNoSlotAvailable() {

    }

    /*----------------------------------------
    * implements DrawerLayout.DrawerListener
    */

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        if(mTeadsVideoView != null){
            mTeadsVideoView.requestPause();
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if(mTeadsVideoView != null){
            mTeadsVideoView.requestResume();
        }
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

}