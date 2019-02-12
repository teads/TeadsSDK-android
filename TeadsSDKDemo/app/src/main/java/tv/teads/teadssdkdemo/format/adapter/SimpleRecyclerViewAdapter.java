package tv.teads.teadssdkdemo.format.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

    /**
     *  Your ad container
     */
    private ViewGroup mAdContainer;

    public SimpleRecyclerViewAdapter(Context context, List<String> dataset, int pid, int adPosition) {
        mPid = pid;
        mAdPosition = adPosition;
        mDataset = dataset;
        mAdContainer = new FrameLayout(context);
        mAdView = new InReadAdView(context);
        mAdView.setPid(mPid);
        mAdView.enableDebug();
        mAdView.load();
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
                mAdContainer = parent;
                mAdView.setAdContainerView(mAdContainer);
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
                // loading is already done before hand
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
            mAdView.setAdContainerView(mAdContainer);
            mAdView.load();
        }
    }

    private class ViewHolderTeadsAd extends RecyclerView.ViewHolder {

        private ViewHolderTeadsAd(View view) {
            super(view);
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
