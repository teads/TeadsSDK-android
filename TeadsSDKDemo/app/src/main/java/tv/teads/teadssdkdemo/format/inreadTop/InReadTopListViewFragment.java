package tv.teads.teadssdkdemo.format.inreadTop;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import tv.teads.sdk.publisher.TeadsAd;
import tv.teads.sdk.publisher.TeadsAdListener;
import tv.teads.sdk.publisher.TeadsConfiguration;
import tv.teads.sdk.publisher.TeadsContainerType;
import tv.teads.teadssdkdemo.MainActivity;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;
import tv.teads.utils.TeadsError;

/**
 * InReadTop format within a ListView
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class InReadTopListViewFragment extends BaseFragment implements TeadsAdListener,
        DrawerLayout.DrawerListener {

    /**
     * Teads Ad instance
     */
    private TeadsAd mTeadsAd;

    /**
     * The ListView used in the application
     */
    private ListView mListView;

    /**
     * The inReadTop ad view
     */
    private ViewGroup mInReadTopAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inreadtop_listview, container, false);

        // Retrieve ListView from layout
        mListView = (ListView) rootView.findViewById(R.id.listView);

        // Retrieve ad view
        mInReadTopAdView = (ViewGroup) rootView.findViewById(R.id.teads_adview);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set ListView basic adapter
        setListViewAdapter(mListView);

        TeadsConfiguration teadsConfig = new TeadsConfiguration();
        teadsConfig.endScreenMode = getEndScreenMode();

        // Instanciate Teads Ad in inReadTop format
        mTeadsAd = new TeadsAd.TeadsAdBuilder(
                getActivity().getApplicationContext(),
                getPid())
                .viewGroup(mInReadTopAdView)
                .eventListener(this)
                .containerType(TeadsContainerType.inReadTop)
                .configuration(teadsConfig)
                .build();

        // Load the Ad
        mTeadsAd.load();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Attach listener to MainActivity to be notified when drawer is opened
        ((MainActivity) getActivity()).setDrawerListener(this);
        mTeadsAd.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).setDrawerListener(null);
        mTeadsAd.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mTeadsAd != null) {
            mTeadsAd.clean();
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

    @Subscribe
    public void onReloadEvent(ReloadEvent event) {
        if (mTeadsAd != null && !mTeadsAd.isLoaded()) {
            mTeadsAd.reset();
            mTeadsAd.load();
        }
    }
    /*----------------------------------------
    * implements TeadsAdEventListener
    */

    @Override
    public void teadsAdDidFailLoading(TeadsError teadsError) {
        try {
            Toast.makeText(this.getActivity(), getString(R.string.didfail), Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException ignored) {

        }
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

    }

    @Override
    public void teadsAdSkipButtonDidShow() {

    }

    @Override
    public void teadsAdWillExpand() {

    }

    @Override
    public void teadsAdDidExpand() {

    }

    @Override
    public void teadsAdWillCollapse() {

    }

    @Override
    public void teadsAdDidCollapse() {

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
        mTeadsAd.requestPause();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mTeadsAd.requestResume();
    }


    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
