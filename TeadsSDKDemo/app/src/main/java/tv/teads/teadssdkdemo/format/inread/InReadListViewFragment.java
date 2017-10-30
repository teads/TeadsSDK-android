package tv.teads.teadssdkdemo.format.inread;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.format.adapter.ListViewAdapter;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;

/**
 * InRead format within a RecyclerView
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class InReadListViewFragment extends BaseFragment {

    /**
     * The ListView used in the application
     */
    private ListView mListView;

    private ListViewAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inread_listview, container, false);
        mListView = rootView.findViewById(R.id.listView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set ListView basic adapter
        setListViewAdapter(mListView);
    }

    private void setListViewAdapter(final ListView listView) {
        int    size     = 50;
        String values[] = new String[size];

        for (int i = 0; i < values.length; i++) {
            values[i] = "Teads " + i;
        }

        // use your custom layout
        mAdapter = new ListViewAdapter(getContext(), getPid());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Id: " + Long.toString(id) + " item: " + listView.getAdapter().getItem
                                                                                                               (position), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanAdView();
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onReloadEvent(ReloadEvent event) {
        if (mAdapter != null) {
            mAdapter.reloadAd();
        }
    }
}
