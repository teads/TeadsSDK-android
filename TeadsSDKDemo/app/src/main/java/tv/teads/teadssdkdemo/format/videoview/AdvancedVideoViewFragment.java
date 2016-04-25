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

import tv.teads.sdk.publisher.TeadsContainerType;
import tv.teads.utils.TeadsError;
import tv.teads.sdk.publisher.TeadsVideo;
import tv.teads.sdk.publisher.TeadsVideoEventListener;
import tv.teads.sdk.publisher.TeadsVideoView;
import tv.teads.teadssdkdemo.MainActivity;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.format.adapter.VideoViewCustomAdapter;
import tv.teads.teadssdkdemo.utils.BaseFragment;

/**
 * Custom format base on {@link TeadsVideoView} that will display the ad every 20 items of the ListView.
 * This sample is supplied for a demostration of TeadsVideoView. It may contains cases that is not managed.
 * <p/>
 * <p/>
 * <p/>
 * Created by Hugo Gresse on 06/08/15.
 */
public class AdvancedVideoViewFragment extends BaseFragment implements
        TeadsVideoEventListener,
        AbsListView.OnScrollListener,
        DrawerLayout.DrawerListener,
        VideoViewCustomAdapter.TeadsViewAttachListener {

    public static final String LOG_TAG = "AdvancedVideoViewFrag";

    private static final int sRepeatableAdPosition = 20;

    /**
     * ListView used as the root layout in this fragment
     */
    private ListView mListView;

    /**
     * Teads Video instance
     */
    private TeadsVideo mTeadsVideo;

    /**
     * A VideoView displaying the ad as a part of ListView
     */
    private TeadsVideoView mTeadsVideoView;

    /**
     * Flag to manage Ad State
     */
    private boolean mIsAnimating;
    private boolean mAdViewHaveToBeOpen;
    private boolean mIsFullscreen;
    private boolean mIsOpen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inread_listview, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listView);

        mAdViewHaveToBeOpen = false;
        mIsAnimating = false;
        mIsOpen = false;

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

        // Attach scroll listener to know when we should detach or attach the TeadsVideoView
        mListView.setOnScrollListener(this);

        // Set a very custom listview adapter
        setListViewAdapter(mListView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mTeadsVideoView != null) {
            // reset views and flags
            mIsAnimating = mAdViewHaveToBeOpen = mIsFullscreen = mIsOpen = false;
            mTeadsVideoView.cleanView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Attach listener to MainActivity to be notified when drawer is opened
        ((MainActivity) getActivity()).setDrawerListener(this);

        if (mTeadsVideo != null) {
            //Notify TeadsVideo when the fragment will resume
            mTeadsVideo.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).setDrawerListener(null);
        if (mTeadsVideo != null) {
            //Notify TeadsVideo when the fragment will pause
            mTeadsVideo.onPause();
        }
    }

    /**
     * Open the VideoView with expand animation
     */
    private void openInRead() {
        if (mTeadsVideoView == null) {
            mAdViewHaveToBeOpen = true;
            return;
        }

        //Update the TeadsVideoView to match the ViewGroup parent
        mTeadsVideoView.updateSize(mListView);
        mTeadsVideoView.setCollapsed();


        if (!mAdViewHaveToBeOpen) {
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
                    mIsOpen = true;

                    //Prevent TeadsVideo that view did expand
                    mTeadsVideo.adViewDidExpand();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            mAdViewHaveToBeOpen = false;
            mTeadsVideoView.setVisibility(View.VISIBLE);
            mTeadsVideo.adViewDidExpand();
        }


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
                mIsOpen = false;
                mTeadsVideo.adViewDidClose();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
     *
     * @param view             the visible view
     * @param firstVisibleItem firstVisibleItem
     * @param visibleItemCount number of item displayed
     * @param totalItemCount   total listview items
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mTeadsVideo == null || mTeadsVideoView == null || mIsAnimating || mIsFullscreen) {
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

        if (!isVisible) {
            mTeadsVideo.requestPause();
            mTeadsVideo.detachView();
        } else {
            mTeadsVideo.requestResume();
        }

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


    @Override
    public void onAttachTeadsVideoView(TeadsVideoView teadsVideoView) {
        Log.d(LOG_TAG, "teadsVideoViewAttached");
        mTeadsVideoView = teadsVideoView;
        mTeadsVideo.attachView(mTeadsVideoView);

        if (!mIsOpen && !mIsAnimating) {
            mTeadsVideoView.setCollapsed();
        }

        mTeadsVideo.teadsVideoViewAdded();
        if (mTeadsVideoView.getRatio() == null) {
            return;
        }
        mTeadsVideoView.updateSize(mListView);

        if (mAdViewHaveToBeOpen) {
            openInRead();
        }
    }
}