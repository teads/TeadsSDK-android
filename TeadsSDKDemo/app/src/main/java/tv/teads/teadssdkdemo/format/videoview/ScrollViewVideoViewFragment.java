package tv.teads.teadssdkdemo.format.videoview;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.ScrollView;

import tv.teads.sdk.publisher.TeadsError;
import tv.teads.sdk.publisher.TeadsNativeVideoEventListener;
import tv.teads.sdk.publisher.TeadsVideoView;
import tv.teads.teadssdkdemo.MainActivity;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.ApplicationVisibility;
import tv.teads.teadssdkdemo.utils.BaseFragment;

/**
 * Simple sample which display a {@link TeadsVideoView} between thow TextView in a ScrollView.
 * It manage visibility check, lock events, fragment lifecycle.
 *
 *
 * <p/>
 * Created by Hugo Gresse on 06/08/15.
 */
public class ScrollViewVideoViewFragment extends BaseFragment implements
        TeadsNativeVideoEventListener,
        DrawerLayout.DrawerListener, ViewTreeObserver.OnScrollChangedListener,
        ApplicationVisibility.VisibilityListener {

    public static final String LOG_TAG = "ScrollViewVideoViewFrag";

    /**
     * ScrollView used as the root layout in this fragment
     */
    private ScrollView mScrollView;

    /**
     * A VideoView displaying the ad
     */
    private TeadsVideoView mTeadsVideoView;

    /**
     * Flag to manage Ad State
     */
    private boolean mIsAnimating;
    private boolean mIsOpen;
    private boolean mIsFullscreen;

    /**
     * Application visibility to handle device lock/unlock
     */
    private ApplicationVisibility mApplicationVisibility;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_videoview_scrollview, container, false);
        mScrollView = (ScrollView) rootView.findViewById(R.id.scrollViewDemoVideoView);
        mTeadsVideoView = (TeadsVideoView) rootView.findViewById(R.id.videoview);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init TeadsVideoView and load the Ad
        mTeadsVideoView.init(getActivity(), getPid(), this);
        mTeadsVideoView.load();
        mTeadsVideoView.setCollapsed();

        // Register Scroll listener
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(this);

        // Register an application visibility that will receive lock/unlock broadcast event
        mApplicationVisibility = new ApplicationVisibility(getActivity(), this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mTeadsVideoView != null) {
            // reset views and flags
            mIsAnimating = mIsOpen = mIsFullscreen = false;
            mTeadsVideoView.clean();
        }
        mApplicationVisibility.clear();
    }

    @Override
    public void onResume(){
        super.onResume();
        // Attach listener to MainActivity to be notified when drawer is opened
        ((MainActivity)getActivity()).setDrawerListener(this);
        if(mTeadsVideoView != null && mTeadsVideoView.isLoaded()){
            checkVisibility();
        }
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
        Log.d(LOG_TAG, "openInRead");
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

                // Check visibility after expand finished
                checkVisibility();

                // Displayed the controls (mute button, skip button, progressBar), needed if you don't use TeadsVideoView
                // own expand/collapse
                // mTeadsVideoView.setControlVisibility(View.VISIBLE);
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
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * Check the state and the visibility of the TeadsVideoView.
     * It not open and visible, launch open animation
     * if open, check playing state and visibility to play or pause
     */
    private void checkVisibility(){
        if(mTeadsVideoView == null || !mTeadsVideoView.isLoaded()){
            return;
        }

        if(!mIsOpen){
            if(!mIsAnimating && mTeadsVideoView.isVisibleCollapsed()) {
                openInRead();
            }
            return;
        }

        if (mTeadsVideoView.isVisible() && !mTeadsVideoView.isPlaying()) {
            mTeadsVideoView.requestResume();
            mTeadsVideoView.setVisibility(View.VISIBLE);
        } else if (!mTeadsVideoView.isVisible() && mTeadsVideoView.isPlaying()) {
            mTeadsVideoView.requestPause();
        }
    }


    /*----------------------------------------
    * implements ScrollViewVideoViewFragment
    */

    /**
     * When the ScrollView scroll
     */
    @Override
    public void onScrollChanged() {
        checkVisibility();
    }


    /*----------------------------------------
    * implements TeadsNativeVideoEventListener
    */

    @Override
    public void nativeVideoDidFailLoading(TeadsError error) {

    }

    @Override
    public void nativeVideoWillLoad() {

    }

    @Override
    public void nativeVideoDidLoad() {
        Log.d(LOG_TAG, "nativeVideoDidLoad");

        mTeadsVideoView.updateSize();

        // Check visibility once the Ad is loaded and ready to show. If visible, open the format
        if (mTeadsVideoView != null && !mIsOpen && mTeadsVideoView.isVisibleCollapsed()) {
            openInRead();
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
        // Ad will no be resume on browser close, check it
        checkVisibility();
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
        checkVisibility();
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
        // sended when the format shoud close : on video finished
        if (mTeadsVideoView != null) {
            closeInRead();
        }
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


    /*----------------------------------------
    * implements ApplicationVisibility.VisibilityListener
    */

    @Override
    public void onVisible() {
        if(mTeadsVideoView != null){
            checkVisibility();
        }
    }

    @Override
    public void onHidden() {
        if(mTeadsVideoView != null && mTeadsVideoView.isPlaying()){
            mTeadsVideoView.requestPause();
        }
    }
}