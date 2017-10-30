package tv.teads.teadssdkdemo.format.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tv.teads.sdk.android.PublicInterface;
import tv.teads.sdk.android.TeadsAdView;
import tv.teads.teadssdkdemo.R;

/**
 * Simple RecyclerView adapter
 * <p/>
 * Created by Hugo Gresse on 08/07/15.
 */
public class SimpleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TEADS = 0;
    private static final int TYPE_TEXT  = 1;
    private int mPid;
    private int mAdPosition;

    private List<String> mDataset;

    private TeadsAdView mAdView;

    public SimpleRecyclerViewAdapter(List<String> dataset, int pid, int adPosition) {
        mPid = pid;
        mAdPosition = adPosition;
        mDataset = dataset;
    }

    @Override
    public int getItemViewType(int position) {
        return position == mAdPosition ? TYPE_TEADS : TYPE_TEXT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TEADS:
                mAdView = new TeadsAdView(parent.getContext());
                mAdView.setPid(mPid);
                mAdView.debug();
                return new ViewHolderTeadsAd(mAdView);
            case TYPE_TEXT:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
                return new ViewHolderDemo(v);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
        if (mAdView != null && mAdView.getState() == PublicInterface.IDLE) {
            mAdView.load();
        }
    }

    private class ViewHolderTeadsAd extends RecyclerView.ViewHolder {
        private TeadsAdView adView;

        private ViewHolderTeadsAd(View view) {
            super(view);
            adView = (TeadsAdView) view;
        }
    }

    private class ViewHolderDemo extends RecyclerView.ViewHolder {
        private TextView textView;

        private ViewHolderDemo(View view) {
            super(view);
            textView = view.findViewById(R.id.listViewText);
        }
    }
}
