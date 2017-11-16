package tv.teads.teadssdkdemo.format.inreadTop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import tv.teads.sdk.android.AdResponse;
import tv.teads.sdk.android.PublicInterface;
import tv.teads.sdk.android.TeadsAd;
import tv.teads.sdk.android.TeadsAdListener;
import tv.teads.sdk.android.TeadsAdView;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;

/**
 * InReadTop format within a ListView
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class InReadTopListViewFragment extends BaseFragment implements TeadsAdListener {

    /**
     * The ListView used in the application
     */
    private ListView mListView;

    /**
     * The inReadTop ad view
     */
    private TeadsAdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inreadtop_listview, container, false);

        // Retrieve ListView from layout
        mListView = rootView.findViewById(R.id.listView);

        // Retrieve ad view
        mAdView = rootView.findViewById(R.id.teads_ad_view);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set ListView basic adapter
        setListViewAdapter(mListView);

        // Instanciate Teads Ad in inReadTop format
        mAdView.setPid(getPid());
        mAdView.debug();
        mAdView.load();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mAdView != null) {
            mAdView.clean();
        }
    }


    private void setListViewAdapter(ListView listView) {
        int    size     = 50;
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
    @SuppressWarnings("unused")
    public void onReloadEvent(ReloadEvent event) {
        if (mAdView != null && mAdView.getState() == PublicInterface.IDLE) {
            mAdView.load();
        }
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
    * implements TeadsAdListener
    *//////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onAdResponse(TeadsAd teadsAd, AdResponse adResponse) {
        if (!adResponse.isSuccessful()) {
            Toast.makeText(this.getActivity(), getString(R.string.didfail), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void displayAd(TeadsAd teadsAd, int i) {

    }

    @Override
    public void closeAd(TeadsAd teadsAd, boolean b) {

    }

    @Override
    public void onError(TeadsAd teadsAd, String s) {
        Toast.makeText(this.getActivity(), getString(R.string.didfail_playback), Toast.LENGTH_SHORT).show();
    }
}
