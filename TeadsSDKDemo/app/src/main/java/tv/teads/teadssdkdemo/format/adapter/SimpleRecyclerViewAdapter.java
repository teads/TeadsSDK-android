package tv.teads.teadssdkdemo.format.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tv.teads.sdk.adContainer.adapter.TeadsNotify;
import tv.teads.sdk.publisher.TeadsRecyclerViewAdapter;
import tv.teads.teadssdkdemo.R;

/**
 * Simple RecyclerView adapter
 * <p/>
 * Created by Hugo Gresse on 08/07/15.
 */
public class SimpleRecyclerViewAdapter extends TeadsRecyclerViewAdapter<SimpleRecyclerViewAdapter.ViewHolderDemo> {

    private List<String> mDataset;
    private RecyclerView mRecyclerView;

    public SimpleRecyclerViewAdapter(List<String> dataset, RecyclerView recyclerView) {
        mDataset = dataset;
        mRecyclerView = recyclerView;
    }

    @Override
    public ViewHolderDemo teadsOnCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
        return new ViewHolderDemo(v);
    }

    @Override
    public void teadsOnBindViewHolder(ViewHolderDemo viewHolderDemo, int position) {
        final String name = mDataset.get(position);

        viewHolderDemo.textView.setText(name);
        viewHolderDemo.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(name);
            }
        });
    }

    @Override
    public int teadsGetItemCount() {
        return mDataset.size();
    }

    public void remove(String name) {
        int index = mDataset.indexOf(name);
        mDataset.remove(mDataset.indexOf(name));
        ((TeadsNotify) mRecyclerView.getAdapter()).teadsNotifyItemRemoved(index);
    }

    /**
     * This is the base RecyclerView Adapter methods that TeadsRecyclerViewAdapter is replacing
     */
//    @Override
//    public ViewHolderDemo onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
//        return new ViewHolderDemo(v);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolderDemo holder, int position) {
//        final String name = mDataset.get(position);
//
//        holder.textView.setText(name);
//        holder.textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                remove(name);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mDataset.size();
//    }

    public class ViewHolderDemo extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolderDemo(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.listViewText);
        }
    }
}
