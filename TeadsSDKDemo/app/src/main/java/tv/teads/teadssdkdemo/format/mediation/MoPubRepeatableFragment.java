package tv.teads.teadssdkdemo.format.mediation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;

/**
 * Created by Benjamin Volland on 15/03/2019.
 */
public class MoPubRepeatableFragment extends BaseFragment {
    /**
     * The RecyclerView used in the application
     */
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inread_recyclerview, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setRecyclerViewAdapter(mRecyclerView);

    }

    private void setRecyclerViewAdapter(RecyclerView recyclerView) {
        ArrayList<String> data = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            data.add("Teads " + i);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        MoPubRepeatableRecyclerViewAdapter mAdapter = new MoPubRepeatableRecyclerViewAdapter(getContext(), data, "d6f99ffee8f245329f2fb4954cb8b477");
        mAdapter.loadBanner();
        recyclerView.setAdapter(mAdapter);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onReloadEvent(ReloadEvent event) {
    }
}
