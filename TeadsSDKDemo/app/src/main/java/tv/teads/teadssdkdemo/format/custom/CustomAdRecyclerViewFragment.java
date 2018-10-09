package tv.teads.teadssdkdemo.format.custom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import tv.teads.sdk.android.AdFailedReason;
import tv.teads.sdk.android.CustomAdView;
import tv.teads.sdk.android.TeadsListener;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.format.adapter.SimpleRecyclerViewAdapter;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;

/**
 * Custom ad format within a RecyclerView
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class CustomAdRecyclerViewFragment extends BaseFragment {

    /**
     * The RecyclerView used in the application
     */
    private RecyclerView mRecyclerView;

    /**
     * The custom ad view
     */
    private CustomAdView mAdView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_custom_ad_recyclerview, container, false);

        // Retrieve recyclerView from layout
        mRecyclerView = rootView.findViewById(R.id.recyclerView);

        // Retrieve ad view
        mAdView = rootView.findViewById(R.id.teads_ad_view);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Set RecyclerView basic adapter
        setRecyclerViewAdapter(mRecyclerView);

        // Instanciate Teads Ad in custom format
        mAdView.setPid(getPid());
        mAdView.setListener(mTeadsListener);
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
        recyclerView.setAdapter(new SimpleRecyclerViewAdapter(getActivity(), data, getPid(), -1));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onReloadEvent(ReloadEvent event) {
        if (mAdView != null) {
            mAdView.load();
        }
    }

    private final TeadsListener mTeadsListener = new TeadsListener(){

        @Override
        public void onAdFailedToLoad(AdFailedReason adFailedReason) {
            Toast.makeText(CustomAdRecyclerViewFragment.this.getActivity(), getString(R.string.didfail), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(String s) {
            Toast.makeText(CustomAdRecyclerViewFragment.this.getActivity(), getString(R.string.didfail_playback), Toast.LENGTH_SHORT).show();
        }
    };
}
