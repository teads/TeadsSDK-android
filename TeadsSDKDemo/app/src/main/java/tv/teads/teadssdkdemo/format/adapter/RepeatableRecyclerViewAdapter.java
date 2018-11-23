package tv.teads.teadssdkdemo.format.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tv.teads.sdk.android.InReadAdView;
import tv.teads.teadssdkdemo.R;

/**
 * Created by Benjamin Volland on 22/11/2018.
 */
public class RepeatableRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TEADS = 0;
    private static final int TYPE_TEXT  = 1;

    private final List<String> mDataset;

    private InReadAdView mAdView;

    public RepeatableRecyclerViewAdapter(Context context, List<String> dataset, int pid) {
        mDataset = dataset;
        mAdView = new InReadAdView(context);
        mAdView.setPid(pid);
        mAdView.enableDebug();
        mAdView.load();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 10 || position == 20 || position == 30 ? TYPE_TEADS : TYPE_TEXT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TEADS:
                return new RepeatableRecyclerViewAdapter.ViewHolderTeadsAd(mAdView);
            case TYPE_TEXT:
            default:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
                return new RepeatableRecyclerViewAdapter.ViewHolderDemo(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_TEADS:
                // loading is already done before hand
                break;
            case TYPE_TEXT:
                ((RepeatableRecyclerViewAdapter.ViewHolderDemo) holder).textView.setText(String.valueOf(position - position / 10));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size() + (mDataset.size() > 30 ? 3 : mDataset.size() > 20 ? 2 : mDataset.size() > 10 ? 1 : 0);
    }

    public void reloadAd() {
        if (mAdView != null) {
            mAdView.load();
        }
    }

    private class ViewHolderTeadsAd extends RecyclerView.ViewHolder {
        private final InReadAdView adView;

        private ViewHolderTeadsAd(View view) {
            super(view);
            adView = (InReadAdView) view;
        }
    }

    private class ViewHolderDemo extends RecyclerView.ViewHolder {
        private final TextView textView;

        private ViewHolderDemo(View view) {
            super(view);
            textView = view.findViewById(R.id.listViewText);
        }
    }
}