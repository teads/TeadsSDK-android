package tv.teads.teadssdkdemo.format.inboard;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import tv.teads.sdk.publisher.TeadsContainerType;
import tv.teads.sdk.publisher.TeadsError;
import tv.teads.sdk.publisher.TeadsVideo;
import tv.teads.sdk.publisher.TeadsVideoEventListener;
import tv.teads.teadssdkdemo.MainActivity;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.format.adapter.SimpleRecyclerViewAdapter;
import tv.teads.teadssdkdemo.utils.BaseFragment;

/**
 * InBoard format within a RecyclerView
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class InBoardRecyclerViewFragment extends BaseFragment implements TeadsVideoEventListener,
        DrawerLayout.DrawerListener {

    /**
     * Teads Video instance
     */
    private TeadsVideo mTeadsVideo;

    /**
     * The RecyclerView used in the application
     */
    private RecyclerView mRecyclerView;

    /**
     * The inBoard ad view
     */
    private ViewGroup mInBoardAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inboard_recyclerview, container, false);

        // Retrieve recyclerView from layout
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // Retrieve ad view
        mInBoardAdView = (ViewGroup) rootView.findViewById(R.id.teads_adview);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set RecyclerView basic adapter
        setRecyclerViewAdapter(mRecyclerView);

        // Instanciate Teads Video in inboard format
        mTeadsVideo = new TeadsVideo.TeadsVideoBuilder(
                getActivity(),
                getPid())
                .viewGroup(mInBoardAdView)
                .eventListener(this)
                .containerType(TeadsContainerType.inBoardSticky)
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

    private void setRecyclerViewAdapter(RecyclerView recyclerView) {
        ArrayList<String> data = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            data.add("Teads " + i);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SimpleRecyclerViewAdapter(data, mRecyclerView));
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
