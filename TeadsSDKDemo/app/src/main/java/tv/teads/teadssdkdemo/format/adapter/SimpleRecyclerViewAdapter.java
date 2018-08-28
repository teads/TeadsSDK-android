package tv.teads.teadssdkdemo.format.adapter;

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
 * Simple RecyclerView adapter
 * <p/>
 * Created by Hugo Gresse on 08/07/15.
 */
public class SimpleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TEADS = 0;
    private static final int TYPE_TEXT  = 1;
    private final int mPid;
    private final int mAdPosition;

    private final List<String> mDataset;

    private InReadAdView mAdView;

    public SimpleRecyclerViewAdapter(List<String> dataset, int pid, int adPosition) {
        mPid = pid;
        mAdPosition = adPosition;
        mDataset = dataset;
    }

    @Override
    public int getItemViewType(int position) {
        return position == mAdPosition ? TYPE_TEADS : TYPE_TEXT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TEADS:
                mAdView = new InReadAdView(parent.getContext());
                mAdView.setPid(mPid);
                return new ViewHolderTeadsAd(mAdView);
            case TYPE_TEXT:
            default:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
                return new ViewHolderDemo(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_TEADS:
                ViewHolderTeadsAd vh = (ViewHolderTeadsAd) holder;
                vh.adView.load();
                break;
            case TYPE_TEXT:
                ((ViewHolderDemo) holder).textView.setText(mDataset.get(position > mAdPosition && mAdPosition > 0 ?
                                                                                position - 1 :
                                                                                position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size() + (mAdPosition >= 0 ? 1 : 0);
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
