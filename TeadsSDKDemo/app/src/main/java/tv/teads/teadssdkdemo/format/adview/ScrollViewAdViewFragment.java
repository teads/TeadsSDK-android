package tv.teads.teadssdkdemo.format.adview;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.ScrollView;

import org.greenrobot.eventbus.Subscribe;

import tv.teads.sdk.publisher.TeadsAd;
import tv.teads.sdk.publisher.TeadsAdListener;
import tv.teads.sdk.publisher.TeadsConfiguration;
import tv.teads.sdk.publisher.TeadsContainerType;
import tv.teads.sdk.publisher.TeadsView;
import tv.teads.teadssdkdemo.MainActivity;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.ApplicationVisibility;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;
import tv.teads.utils.TeadsError;

/**
 * Simple sample which display a {@link TeadsView} between thow TextView in a ScrollView.
 * It manage visibility check, lock events, fragment lifecycle.
 * <p/>
 * <p/>
 * Created by Hugo Gresse on 06/08/15.
 */
public class ScrollViewAdViewFragment extends BaseFragment implements
        TeadsAdListener,
        DrawerLayout.DrawerListener, ViewTreeObserver.OnScrollChangedListener,
        ApplicationVisibility.VisibilityListener {

    public static final String LOG_TAG = "ScrollViewAdViewFrag";

    /**
     * ScrollView used as the root layout in this fragment
     */
    private ScrollView mScrollView;

    /**
     * Teads Ad instance
     */
    private TeadsAd mTeadsAd;

    /**
     * A TeadsView displaying the ad
     */
    private TeadsView mTeadsView;

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
        View rootView = inflater.inflate(R.layout.fragment_adview_scrollview, container, false);
        mScrollView = (ScrollView) rootView.findViewById(R.id.scrollViewDemoAdView);
        mTeadsView = (TeadsView) rootView.findViewById(R.id.adview);
        mIsAnimating = false;

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TeadsConfiguration teadsConfig = new TeadsConfiguration();
        teadsConfig.endScreenMode = getEndScreenMode();

        // Init TeadsAd and load the Ad
        mTeadsAd = new TeadsAd.TeadsAdBuilder(getActivity().getApplicationContext(), getPid())
                .containerType(TeadsContainerType.custom)
                .eventListener(this)
                .configuration(teadsConfig)
                .build();
        mTeadsAd.load();

        mTeadsAd.attachView(mTeadsView);
        mTeadsAd.teadsVideoViewAdded();
        mTeadsView.setCollapsed();

        // Register an application visibility that will receive lock/unlock broadcast event
        mApplicationVisibility = new ApplicationVisibility(getActivity(), this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mTeadsView != null) {
            // reset views and flags
            mIsAnimating = false;
            mTeadsView.cleanView();
        }
        mApplicationVisibility.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(this);
        // Attach listener to MainActivity to be notified when drawer is opened
        ((MainActivity) getActivity()).setDrawerListener(this);
        if (mTeadsAd != null) {
            mTeadsAd.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScrollView.getViewTreeObserver().removeOnScrollChangedListener(this);
        ((MainActivity) getActivity()).setDrawerListener(null);
        if (mTeadsAd != null) {
            mTeadsAd.onPause();
        }
    }

    /**
     * Open the AdView with expand animation
     */
    private void openInRead() {

        mTeadsView.updateSize(mScrollView);
        mTeadsView.setCollapsed();

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
                mTeadsAd.adViewDidExpand();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    /**
     * Close AdView with collapse animation
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
                mTeadsAd.adViewDidClose();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    @Subscribe
    public void onReloadEvent(ReloadEvent event) {
        if (mTeadsAd != null && !mTeadsAd.isLoaded()) {
            mTeadsAd.reset();
            mTeadsAd.load();
        }
    }

    /*----------------------------------------
    * implements ScrollViewAdViewFragment
    */

    /**
     * When the ScrollView scroll
     */
    @Override
    public void onScrollChanged() {
        if (!mIsAnimating && mTeadsView != null && mTeadsAd != null){
            mTeadsAd.containerDidMove();
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
        Log.e("teads#Teads", "teadsAdDidExpand");
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
        //have to play collapse animation
        //In end of animation prevent animation finish
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


    /*----------------------------------------
    * implements ApplicationVisibility.VisibilityListener
    */

    @Override
    public void onVisible() {
        if (mTeadsAd != null) {
            mTeadsAd.containerDidMove();
        }
    }

    @Override
    public void onHidden() {
        if (mTeadsAd != null) {
            mTeadsAd.requestPause();
        }
    }


}