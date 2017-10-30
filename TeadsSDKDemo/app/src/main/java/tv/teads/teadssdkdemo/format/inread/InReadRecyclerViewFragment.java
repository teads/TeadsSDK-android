package tv.teads.teadssdkdemo.format.inread;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.format.adapter.SimpleRecyclerViewAdapter;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;

/**
 * InRead format within a RecyclerView
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class InReadRecyclerViewFragment extends BaseFragment {

    /**
     * The RecyclerView used in the application
     */
    private RecyclerView mRecyclerView;

    private SimpleRecyclerViewAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inread_recyclerview, container, false);

        // Retrieve recyclerView from layout
        mRecyclerView = rootView.findViewById(R.id.recyclerView);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set RecyclerView basic adapter
        setRecyclerViewAdapter(mRecyclerView);

    }

    private void setRecyclerViewAdapter(RecyclerView recyclerView) {
        ArrayList<String> data = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            data.add("Teads " + i);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new SimpleRecyclerViewAdapter(data, getPid(), 10);
        recyclerView.setAdapter(mAdapter);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onReloadEvent(ReloadEvent event) {
        if (mAdapter != null) {
            mAdapter.reloadAd();
        }
    }

}
