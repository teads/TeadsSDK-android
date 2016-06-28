package tv.teads.teadssdkdemo.format.adview;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.ListView;

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
 * Custom format base on {@link TeadsView} that will display the ad every 20 items of the ListView.
 * This sample is supplied for a demostration of TeadsAdView. It may contains cases that is not managed.
 * <p/>
 * <p/>
 * <p/>
 * Created by Hugo Gresse on 06/08/15.
 */
public class AdvancedAdViewFragment extends BaseFragment implements
        TeadsAdListener,
        AbsListView.OnScrollListener,
        DrawerLayout.DrawerListener,
        AdViewCustomAdapter.TeadsViewAttachListener {

    public static final String LOG_TAG = "AdvancedAdViewFragment";

    private static final int sRepeatableAdPosition = 20;

    /**
     * ListView used as the root layout in this fragment
     */
    private ListView mListView;

    /**
     * Teads Ad instance
     */
    private TeadsAd mTeadsAd;

    /**
     * A View displaying the ad as a part of ListView
     */
    private TeadsView mTeadsView;

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

        TeadsConfiguration teadsConfig = new TeadsConfiguration();
        teadsConfig.endScreenMode = getEndScreenMode();

        // Init TeadsAd and load the Ad
        mTeadsAd = new TeadsAd.TeadsAdBuilder(getActivity(), getPid())
                .containerType(TeadsContainerType.custom)
                .eventListener(this)
                .configuration(teadsConfig)
                .build();
        mTeadsAd.load();

        // Attach scroll listener to know when we should detach or attach the TeadsAdView
        mListView.setOnScrollListener(this);

        // Set a very custom listview adapter
        setListViewAdapter(mListView);
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
        mTeadsView.updateSize(mListView);
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
     * Create sample data for the ListView and attach the custom adapter to it with the correct Ad position.
     *
     * @param listView the LsitView to be indlated
     */
    private void setListViewAdapter(ListView listView) {
        String values[] = new String[200];

        for (int i = 0; i < values.length; i++) {
            values[i] = "Teads " + i;
        }

        // Instantiate the custom adapter used to display TeadsView in the ListView
        AdViewCustomAdapter adViewCustomAdapter = new AdViewCustomAdapter(
                getActivity(),
                values,
                sRepeatableAdPosition,
                this);

        listView.setAdapter(adViewCustomAdapter);
    }

    @Subscribe
    public void onEvent(ReloadEvent event) {
        if (mTeadsAd != null) {
            mTeadsAd.reset();
            mTeadsAd.load();
        }
    }

    /*----------------------------------------
    * implements AbsListView.OnScrollListener
    */

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /**
     * Called on ListView scroll. we have to check the ad visibility by :
     * - check if the displayed items contains the AdView (the AdView is placed every 20 items)
     *
     * @param view             the visible view
     * @param firstVisibleItem firstVisibleItem
     * @param visibleItemCount number of item displayed
     * @param totalItemCount   total listview items
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mTeadsAd == null || mTeadsView == null || mIsAnimating || mIsFullscreen) {
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
            mTeadsAd.requestPause();
            mTeadsAd.detachView();
        } else {
            mTeadsAd.requestResume();
        }

        mTeadsAd.containerDidMove();
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


    @Override
    public void onAttachTeadsAdView(TeadsView teadsAdView) {
        Log.d(LOG_TAG, "teadsAdViewAttached");
        mTeadsView = teadsAdView;
        mTeadsAd.attachView(mTeadsView);

        if (!mIsOpen && !mIsAnimating) {
            mTeadsView.setCollapsed();
        }

        mTeadsAd.teadsVideoViewAdded();
        if (mTeadsView.getRatio() == null) {
            return;
        }
        mTeadsView.updateSize(mListView);

        if (mAdViewHaveToBeOpen) {
            openInRead();
        }
    }
}