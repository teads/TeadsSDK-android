package tv.teads.teadssdkdemo.format.inread;

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
import tv.teads.teadssdkdemo.format.adapter.RepeatableRecyclerViewAdapter;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;

/**
 * Display the same inRead in different position inside a same recycler view thanks to
 * {@link RepeatableRecyclerViewAdapter}
 * Created by Benjamin Volland on 22/11/2018.
 */
public class InReadRepeatableRecyclerViewFragment extends BaseFragment {

    /**
     * The RecyclerView used in the application
     */
    private RecyclerView mRecyclerView;

    private RepeatableRecyclerViewAdapter mAdapter;

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

        mAdapter = new RepeatableRecyclerViewAdapter(getContext(), data, getPid());
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
