package tv.teads.teadssdkdemo.format;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tv.teads.sdk.publisher.TeadsConfiguration;
import tv.teads.sdk.publisher.TeadsError;
import tv.teads.sdk.publisher.TeadsLockableViewPager;
import tv.teads.sdk.publisher.TeadsNativeVideo;
import tv.teads.sdk.publisher.TeadsNativeVideoEventListener;
import tv.teads.teadssdkdemo.MainActivity;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.EmptyFragment;
import tv.teads.teadssdkdemo.utils.TabFragmentPagerAdapter;
import tv.teads.teadssdkdemo.utils.external.PagerSlidingTabStrip;

/**
 * InSwipe format within a ViewPager
 *
 * Created by Hugo Gresse on 30/03/15.
 */
public class InSwipeViewPagerFragment extends BaseFragment implements ViewPager.OnPageChangeListener,
        TeadsNativeVideoEventListener,
        DrawerLayout.DrawerListener {

    private PagerSlidingTabStrip mPagerSlidingTabStrip;

    /**
     * Teads Native Video instance
     */
    private TeadsNativeVideo        mTeadsNativeVideo;

    /**
     * The ViewPager used in the application
     */
    private TeadsLockableViewPager mViewPager;

    /**
     * Ad Position used for TeadsConfiguration and mPagerSlidingTabStrip
     */
    private int                    mAdPosition = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_native_inswipe, container, false);
        mViewPager = (TeadsLockableViewPager)rootView.findViewById(R.id.viewpager);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        // Set ViewPager data, tab and adapter
        initViewPager(view, createFragments());

        // Init Teads Config to display Ad after the second fragment
        TeadsConfiguration teadsConfig = new TeadsConfiguration();
        teadsConfig.adPosition = mAdPosition;

        // Instanciate Teads Native Video in inSwipe format
        mTeadsNativeVideo = new TeadsNativeVideo(
                this.getActivity(),
                mViewPager,
                this.getPid(),
                TeadsNativeVideo.NativeVideoContainerType.inSwipe,
                this,
                mPagerSlidingTabStrip.getOnPageChangeListener(),
                teadsConfig);

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

        if(mTeadsNativeVideo != null){
            mTeadsNativeVideo.clean();
        }
    }

    /*----------------------------------------
    * implements ViewPager.OnPageChangeListener
    */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /*----------------------------------------
    * implements TeadsNativeVideoEventListener
    */

    @Override
    public void nativeVideoDidFailLoading(TeadsError teadsError) {
        try {
            Toast.makeText(this.getActivity(), getString(R.string.didfail), Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException ignored){

        }
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
    public void nativeVideoDidClickBrowserClose() {

    }

    @Override
    public void nativeVideoWillTakerOverFullScreen() {

    }

    @Override
    public void nativeVideoDidTakeOverFullScreen() {

    }

    @Override
    public void nativeVideoWillDismissFullscreen() {

    }

    @Override
    public void nativeVideoDidDismissFullscreen() {

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

    @Override
    public void nativeVideoDidClean() {

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
    * Private methods
    */

    private void initViewPager(View view, List<Fragment> fragments){
        TabFragmentPagerAdapter pagerAdapter = new TabFragmentPagerAdapter(
                new String[]{"1", "2", "3", "4"},
                getChildFragmentManager(),
                createFragments());

        mViewPager.setAdapter(pagerAdapter);


        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.sliding_tabs);
        mPagerSlidingTabStrip.setAdPosition(mAdPosition);
        mPagerSlidingTabStrip.setViewPager(mViewPager);

        mPagerSlidingTabStrip.setBackgroundResource(R.color.primaryDef);
        mPagerSlidingTabStrip.setOnPageChangeListener(this);

        mPagerSlidingTabStrip.setOnTabReselectedListener(new PagerSlidingTabStrip.OnTabReselectedListener() {
            @Override
            public void onTabReselected(int position) {
                if(mViewPager.isAdOn()){
                    mViewPager.setCurrentItem(position);
                }
            }
        });
    }

    private List<Fragment> createFragments(){
        List<Fragment> list = new ArrayList<Fragment>();

        Bundle bundle1 = new Bundle();
        bundle1.putInt("number", 1);
        bundle1.putBoolean("isLeft", true);
        list.add(Fragment.instantiate(getActivity(), EmptyFragment.class.getName(), bundle1));

        Bundle bundle2 = new Bundle();
        bundle2.putInt("number", 2);
        bundle2.putBoolean("isLeft", true);
        list.add(Fragment.instantiate(getActivity(), EmptyFragment.class.getName(), bundle2));

        Bundle bundle3 = new Bundle();
        bundle3.putInt("number", 3);
        bundle3.putBoolean("isLeft", false);
        list.add(Fragment.instantiate(getActivity(), EmptyFragment.class.getName(), bundle3));

        Bundle bundle4 = new Bundle();
        bundle4.putInt("number", 4);
        bundle4.putBoolean("isLeft", false);
        list.add(Fragment.instantiate(getActivity(), EmptyFragment.class.getName(), bundle4));
        return list;
    }

}
