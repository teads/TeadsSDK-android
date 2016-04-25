package tv.teads.teadssdkdemo.format.inread;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import tv.teads.sdk.publisher.TeadsConfiguration;
import tv.teads.sdk.publisher.TeadsContainerType;
import tv.teads.utils.TeadsError;
import tv.teads.sdk.publisher.TeadsVideo;
import tv.teads.sdk.publisher.TeadsVideoEventListener;
import tv.teads.teadssdkdemo.MainActivity;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;

/**
 * InRead format within a RecyclerView
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class InReadListViewFragment extends BaseFragment implements TeadsVideoEventListener,
        DrawerLayout.DrawerListener {

    /**
     * Teads Video instance
     */
    private TeadsVideo mTeadsVideo;

    /**
     * The ListView used in the application
     */
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inread_listview, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set ListView basic adapter
        setListViewAdapter(mListView);

        TeadsConfiguration teadsConfig = new TeadsConfiguration();
        teadsConfig.adPosition = 12;

        // Instanciate Teads Video in inRead format
        mTeadsVideo = new TeadsVideo.TeadsVideoBuilder(
                getActivity(),
                getPid())
                .configuration(teadsConfig)
                .viewGroup(mListView)
                .eventListener(this)
                .containerType(TeadsContainerType.inRead)
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


    private void setListViewAdapter(ListView listView) {
        int size = 50;
        String values[] = new String[size];

        for (int i = 0; i < values.length; i++) {
            values[i] = "Teads " + i;
        }

        // use your custom layout
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),
                R.layout.list_row, R.id.listViewText, values);
        listView.setAdapter(adapter);
    }


    /*----------------------------------------
    * implements TeadsVideoEventListener
    */

    @Override
    public void teadsVideoDidFailLoading(TeadsError teadsError) {
        try {
            Log.e("fail", teadsError.toString());
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
