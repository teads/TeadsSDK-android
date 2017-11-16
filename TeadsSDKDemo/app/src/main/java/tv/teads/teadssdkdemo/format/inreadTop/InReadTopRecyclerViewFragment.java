package tv.teads.teadssdkdemo.format.inreadTop;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import tv.teads.sdk.android.AdResponse;
import tv.teads.sdk.android.PublicInterface;
import tv.teads.sdk.android.TeadsAd;
import tv.teads.sdk.android.TeadsAdListener;
import tv.teads.sdk.android.TeadsAdView;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.format.adapter.SimpleRecyclerViewAdapter;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;

/**
 * InReadTop format within a RecyclerView
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class InReadTopRecyclerViewFragment extends BaseFragment implements TeadsAdListener {

    /**
     * The RecyclerView used in the application
     */
    private RecyclerView mRecyclerView;

    /**
     * The inReadTop ad view
     */
    private TeadsAdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inreadtop_recyclerview, container, false);

        // Retrieve recyclerView from layout
        mRecyclerView = rootView.findViewById(R.id.recyclerView);

        // Retrieve ad view
        mAdView = rootView.findViewById(R.id.teads_ad_view);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set RecyclerView basic adapter
        setRecyclerViewAdapter(mRecyclerView);

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

    private void setRecyclerViewAdapter(RecyclerView recyclerView) {
        ArrayList<String> data = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            data.add("Teads " + i);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SimpleRecyclerViewAdapter(data, getPid(), -1));
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
