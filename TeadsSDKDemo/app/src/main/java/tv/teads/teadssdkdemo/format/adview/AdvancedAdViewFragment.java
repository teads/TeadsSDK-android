package tv.teads.teadssdkdemo.format.adview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import org.greenrobot.eventbus.Subscribe;

import tv.teads.sdk.publisher.TeadsAd;
import tv.teads.sdk.publisher.TeadsAdListener;
import tv.teads.sdk.publisher.TeadsConfiguration;
import tv.teads.sdk.publisher.TeadsContainerType;
import tv.teads.sdk.publisher.TeadsView;
import tv.teads.teadssdkdemo.MainActivity;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.format.adapter.AdViewCustomAdapter;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;
import tv.teads.utils.TeadsError;

/**
 * Custom format base on {@link TeadsView} that will display the ad every 20 items of the RecyclerView.
 * This sample is supplied for a demostration of TeadsAdView. It may contains cases that are not managed.
 * <p/>
 * Created by Hugo Gresse on 06/08/15.
 */
public class AdvancedAdViewFragment extends BaseFragment implements
        TeadsAdListener,
        DrawerLayout.DrawerListener,
        AdViewCustomAdapter.TeadsViewAttachListener {

    public static final String LOG_TAG = "AdvancedAdViewFragment";

    private static final int sRepeatableAdPosition = 6;

    /**
     * RecyclerView used as the root layout in this fragment
     */
    private RecyclerView mRecyclerView;

    /**
     * LayoutManager for the RecyclerView
     */
    private LinearLayoutManager mLayoutManager;

    /**
     * Teads Ad instance
     */
    private TeadsAd mTeadsAd;

    /**
     * A View displaying the ad as a part of a RecyclerView
     */
    @Nullable
    private TeadsView mTeadsView;

    /**
     * Flag to manage Ad State
     */
    private boolean mIsAnimating;
    private boolean mAdViewHaveToBeOpen;
    private boolean mIsFullscreen;
    private boolean mIsOpen;
    private Float   mVideoRatio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inread_recyclerview, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        mAdViewHaveToBeOpen = false;
        mIsAnimating = false;
        mIsOpen = false;

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TeadsConfiguration teadsConfig = new TeadsConfiguration();
        teadsConfig.endScreenMode = getEndScreenMode();

        // Init TeadsAd and load the Ad
        mTeadsAd = new TeadsAd.TeadsAdBuilder(getActivity(), getPid())
                .containerType(TeadsContainerType.custom)
                .eventListener(this)
                .configuration(teadsConfig)
                .build();
        mTeadsAd.load();
        mTeadsAd.onSlotAvailability(1);

        // Set a very custom recyclerView adapter
        setRecyclerViewAdapter(mRecyclerView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mTeadsView != null) {
            // reset views and flags
            mIsAnimating = mAdViewHaveToBeOpen = mIsFullscreen = mIsOpen = false;
            mTeadsView.cleanView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Attach listener to MainActivity to be notified when drawer is opened
        ((MainActivity) getActivity()).setDrawerListener(this);

        if (mTeadsAd != null) {
            //Notify TeadsAd when the fragment will resume
            mTeadsAd.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).setDrawerListener(null);
        if (mTeadsAd != null) {
            //Notify TeadsAd when the fragment will pause
            mTeadsAd.onPause();
        }
    }

    /**
     * Open the TeadsView with expand animation
     */
    private void openInRead() {
        if (mTeadsView == null) {
            mAdViewHaveToBeOpen = true;
            return;
        }

        //Update the TeadsAdView to match the ViewGroup parent
        //mTeadsView.updateSize(mRecyclerView);
        mTeadsView.setCollapsed();

        if (!mAdViewHaveToBeOpen) {
            mIsAnimating = true;
            mTeadsView.expand(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    Log.d(LOG_TAG, "onAnimationStart");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Log.d(LOG_TAG, "onAnimationEnd");
                    mIsAnimating = false;
                    mIsOpen = true;
                    //Prevent TeadsAd that view did expand
                    mTeadsAd.adViewDidExpand();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            mAdViewHaveToBeOpen = false;
            mTeadsView.setVisibility(View.VISIBLE);
            mTeadsAd.adViewDidExpand();
        }
    }

    /**
     * Close TeadsView with collapse animation
     */
    private void closeInRead() {
        mVideoRatio = null;
        mIsAnimating = true;
        mTeadsView.collapse(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mIsAnimating = false;
                mIsOpen = false;
                mTeadsAd.adViewDidClose();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * Create sample data for the RecyclerView and attach the custom adapter to it with the correct ad position.
     *
     * @param recyclerView the RecyclerView to be inflated
     */
    private void setRecyclerViewAdapter(RecyclerView recyclerView) {
        String values[] = new String[200];

        for (int i = 0; i < values.length; i++) {
            values[i] = "Teads " + i;
        }

        // Instantiate the custom adapter used to display TeadsView in the RecyclerView
        AdViewCustomAdapter adViewCustomAdapter = new AdViewCustomAdapter(
                values,
                sRepeatableAdPosition,
                this);

        mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adViewCustomAdapter);

        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mTeadsAd == null || mIsAnimating) {
                    return;
                }

                boolean isVisible = false;
                int visibleItemCount = (mLayoutManager).findLastVisibleItemPosition() - (mLayoutManager).findFirstVisibleItemPosition() + 1;
                int firstVisibleItem = (mLayoutManager).findFirstVisibleItemPosition();

                if ((visibleItemCount) >= sRepeatableAdPosition || firstVisibleItem % sRepeatableAdPosition == 0) {
                    // Log.d(LOG_TAG, "vis1");
                    isVisible = true;
                }

                if (!isVisible &&
                        (firstVisibleItem % sRepeatableAdPosition) > ((firstVisibleItem + visibleItemCount) % sRepeatableAdPosition)
                        && (firstVisibleItem + visibleItemCount) % sRepeatableAdPosition != 0
                        && visibleItemCount > 1) {
                    // Log.d(LOG_TAG, "vis2");
                    isVisible = true;
                }

                if (!isVisible) {
                    mTeadsAd.requestPause();
                    mTeadsAd.detachView();
                } else {
                    mTeadsAd.requestResume();
                }

                mTeadsAd.containerDidMove();
            }
        });
    }

    @Subscribe
    public void onReloadEvent(ReloadEvent event) {
        if (mTeadsAd != null && !mTeadsAd.isLoaded()) {
            mVideoRatio = null;
            mTeadsAd.reset();
            mTeadsAd.load();
        }
    }

    /*----------------------------------------
    * implements TeadsAdEventListener
    */

    @Override
    public void teadsAdDidFailLoading(TeadsError teadsError) {

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
        if(mTeadsView != null){
            mVideoRatio = mTeadsView.getRatio();
        }
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
        if (mTeadsView != null) {
            closeInRead();
        }
    }

    @Override
    public void teadsAdSkipButtonDidShow() {

    }

    @Override
    public void teadsAdWillExpand() {
        //have to play animation
        //In end of animation prevent animation finish
        openInRead();
    }

    @Override
    public void teadsAdDidExpand() {

    }

    @Override
    public void teadsAdWillCollapse() {
    }

    @Override
    public void teadsAdDidCollapse() {
        if (mTeadsView != null) {
            closeInRead();
        }
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
        if (mTeadsAd != null) {
            mTeadsAd.requestPause();
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if (mTeadsAd != null) {
            mTeadsAd.requestResume();
        }
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public void onAttachTeadsAdView(TeadsView teadsAdView) {
        String log =  "teadsAdViewAttached";
        mTeadsView = teadsAdView;
        mTeadsAd.attachView(mTeadsView);

        if (!mIsOpen && !mIsAnimating) {
            log += " setCollapsed";
            mTeadsView.setCollapsed();
        }

        mTeadsAd.teadsVideoViewAdded();

        if (mTeadsView.getRatio() == null) {
            if(mVideoRatio != null) {
                log += " setRatio";
                mTeadsView.setRatio(mVideoRatio);
            } else {
                log += " return null ratio";
                Log.d(LOG_TAG, log);
                return;
            }
        }

        mTeadsAd.onSlotAvailability(1);
        mTeadsView.updateSize(mRecyclerView);

        if (mAdViewHaveToBeOpen) {
            log += " openInRead";
            openInRead();
        }

        Log.d(LOG_TAG, log);
    }
}