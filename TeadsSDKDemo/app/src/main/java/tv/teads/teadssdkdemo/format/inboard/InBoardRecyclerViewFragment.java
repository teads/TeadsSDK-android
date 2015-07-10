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

import tv.teads.sdk.publisher.TeadsError;
import tv.teads.sdk.publisher.TeadsNativeVideo;
import tv.teads.sdk.publisher.TeadsNativeVideoEventListener;
import tv.teads.teadssdkdemo.MainActivity;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.format.adapter.SimpleRecyclerViewAdapter;
import tv.teads.teadssdkdemo.utils.BaseFragment;

/**
 * InBoard format within a RecyclerView
 *
 * Created by Hugo Gresse on 30/03/15.
 */
public class InBoardRecyclerViewFragment extends BaseFragment implements TeadsNativeVideoEventListener,
        DrawerLayout.DrawerListener{

    /**
     * Teads Native Video instance
     */
    private TeadsNativeVideo    mTeadsNativeVideo;

    /**
     * The RecyclerView used in the application
     */
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_native_inboard_recyclerview, container, false);

        // Retrieve recyclerView from layout
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        // Set RecyclerView basic adapter
        setRecyclerViewAdapter(mRecyclerView);

        // Instanciate Teads Native Video in inboard format
        mTeadsNativeVideo = new TeadsNativeVideo(
                this.getActivity(),
                mRecyclerView,
                this.getPid(),
                TeadsNativeVideo.NativeVideoContainerType.inBoard,
                this,
                null);

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

    private void setRecyclerViewAdapter(RecyclerView recyclerView){
        ArrayList<String> data = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            data.add("Teads " + i);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SimpleRecyclerViewAdapter(data, mRecyclerView));
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
}
