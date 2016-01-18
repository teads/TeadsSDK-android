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

import tv.teads.sdk.publisher.TeadsContainerType;
import tv.teads.sdk.publisher.TeadsError;
import tv.teads.sdk.publisher.TeadsVideo;
import tv.teads.sdk.publisher.TeadsVideoEventListener;
import tv.teads.sdk.publisher.TeadsVideoView;
import tv.teads.teadssdkdemo.MainActivity;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.ApplicationVisibility;
import tv.teads.teadssdkdemo.utils.BaseFragment;

/**
 * Simple sample which display a {@link TeadsVideoView} between thow TextView in a ScrollView.
 * It manage visibility check, lock events, fragment lifecycle.
 * <p/>
 * <p/>
 * Created by Hugo Gresse on 06/08/15.
 */
public class ScrollViewVideoViewFragment extends BaseFragment implements
        TeadsVideoEventListener,
        DrawerLayout.DrawerListener, ViewTreeObserver.OnScrollChangedListener,
        ApplicationVisibility.VisibilityListener {

    public static final String LOG_TAG = "ScrollViewVideoViewFrag";

    /**
     * ScrollView used as the root layout in this fragment
     */
    private ScrollView mScrollView;

    /**
     * Teads Video instance
     */
    private TeadsVideo mTeadsVideo;

    /**
     * A VideoView displaying the ad
     */
    private TeadsVideoView mTeadsVideoView;

    /**
     * Flag to manage Ad State
     */
    private boolean mIsAnimating;

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
        mIsAnimating = false;

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init TeadsVideo and load the Ad
        mTeadsVideo = new TeadsVideo.TeadsVideoBuilder(getActivity(), getPid())
                .containerType(TeadsContainerType.custom)
                .eventListener(this)
                .build();
        mTeadsVideo.load();

        mTeadsVideo.attachView(mTeadsVideoView);
        mTeadsVideo.teadsVideoViewAdded();
        mTeadsVideoView.setCollapsed();
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(this);

        // Register an application visibility that will receive lock/unlock broadcast event
        mApplicationVisibility = new ApplicationVisibility(getActivity(), this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mTeadsVideoView != null) {
            // reset views and flags
            mIsAnimating = false;
            mTeadsVideoView.clean();
        }
        mApplicationVisibility.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Attach listener to MainActivity to be notified when drawer is opened
        ((MainActivity) getActivity()).setDrawerListener(this);
        if (mTeadsVideo != null) {
            mTeadsVideo.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).setDrawerListener(null);
        if (mTeadsVideo != null) {
            mTeadsVideo.onPause();
        }
    }

    /**
     * Open the VideoView with expand animation
     */
    private void openInRead() {

        mTeadsVideoView.updateSize(mScrollView);
        mTeadsVideoView.setCollapsed();

        mIsAnimating = true;
        mTeadsVideoView.expand(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(LOG_TAG, "onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(LOG_TAG, "onAnimationEnd");
                mIsAnimating = false;
                mTeadsVideo.adViewDidExpand();
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

        mIsAnimating = true;
        mTeadsVideoView.collapse(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mIsAnimating = false;
                mTeadsVideo.adViewDidClose();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }




    /*----------------------------------------
    * implements ScrollViewVideoViewFragment
    */

    /**
     * When the ScrollView scroll
     */
    @Override
    public void onScrollChanged() {
        if (!mIsAnimating)
            mTeadsVideo.containerDidMove();
    }


    /*----------------------------------------
    * implements TeadsVideoEventListener
    */
    @Override
    public void teadsVideoDidFailLoading(TeadsError teadsError) {

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
        if (mTeadsVideoView != null) {
            closeInRead();
        }
    }

    @Override
    public void teadsVideoSkipButtonDidShow() {

    }

    @Override
    public void teadsVideoWillExpand() {
        //have to play animation
        //In end of animation prevent animation finish
        Log.e("teads#Teads", "teadsVideoDidExpand");
        openInRead();
    }

    @Override
    public void teadsVideoDidExpand() {
    }

    @Override
    public void teadsVideoWillCollapse() {

    }

    @Override
    public void teadsVideoDidCollapse() {
        //have to play collapse animation
        //In end of animation prevent animation finish
        if (mTeadsVideoView != null) {
            closeInRead();
        }
    }

    @Override
    public void teadsVideoDidClean() {

    }

    @Override
    public void teadsVideoNoSlotAvailable() {

    }



    /*----------------------------------------
    * implements DrawerLayout.DrawerListener
    */

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        if (mTeadsVideo != null) {
            mTeadsVideo.requestPause();
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if (mTeadsVideo != null) {
            mTeadsVideo.requestResume();
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
        if (mTeadsVideo != null) {
            mTeadsVideo.containerDidMove();
        }
    }

    @Override
    public void onHidden() {
        if (mTeadsVideo != null) {
            mTeadsVideo.requestPause();
        }
    }


}