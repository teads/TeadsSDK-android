package tv.teads.teadssdkdemo.format.inreadTop;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import tv.teads.sdk.publisher.TeadsContainerType;
import tv.teads.sdk.publisher.TeadsError;
import tv.teads.sdk.publisher.TeadsVideo;
import tv.teads.sdk.publisher.TeadsVideoEventListener;
import tv.teads.teadssdkdemo.MainActivity;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;

/**
 * InReadTop format within a ScrollView
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class InReadTopScrollViewFragment extends BaseFragment implements TeadsVideoEventListener,
        DrawerLayout.DrawerListener {

    /**
     * Teads Video instance
     */
    private TeadsVideo mTeadsVideo;

    /**
     * Your FrameLayout used to display video in
     */
    private FrameLayout mFrameLayout;

    /**
     * The inReadTop ad view
     */
    private ViewGroup mInReadTopAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inreadtop_scrollview, container, false);

        mFrameLayout = (FrameLayout) rootView.findViewById(R.id.ad_framelayout);

        // Retrieve ad view
        mInReadTopAdView = (ViewGroup) rootView.findViewById(R.id.teads_adview);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Instanciate Teads Video in inReadTop format
        mTeadsVideo = new TeadsVideo.TeadsVideoBuilder(
                getActivity(),
                getPid())
                .viewGroup(mInReadTopAdView)
                .eventListener(this)
                .containerType(TeadsContainerType.inReadTop)
                .build();

        // Load the Ad
        mTeadsVideo.load();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Attach listener to MainActivity to be notified when drawer is opened
        ((MainActivity) getActivity()).setDrawerListener(this);
        mTeadsVideo.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).setDrawerListener(null);
        mTeadsVideo.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mTeadsVideo != null) {
            mTeadsVideo.clean();
        }
    }


    /*----------------------------------------
    * implements TeadsVideoEventListener
    */

    @Override
    public void teadsVideoDidFailLoading(TeadsError teadsError) {
        try {
            Toast.makeText(this.getActivity(), getString(R.string.didfail), Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException ignored) {

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
        mTeadsVideo.requestPause();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mTeadsVideo.requestResume();
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
