package tv.teads.teadssdkdemo.format.mediation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mopub.mobileads.MoPubView;

import java.util.List;

import tv.teads.adapter.mopub.TeadsLocalExtras;
import tv.teads.teadssdkdemo.R;

/**
 * Manage a repeatable ad for a Recycler view with the MoPub mediation,
 * It will display the same ad view every {@link MoPubRepeatableRecyclerViewAdapter#AD_INTERVAL} items
 * Created by Benjamin Volland on 15/03/2019.
 */
public class MoPubRepeatableRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_MOPUB_AD = 0;
    private static final int TYPE_TEXT     = 1;
    private static final int AD_INTERVAL   = 10;

    private final List<String> mDataset;

    private MoPubView mMoPubView;

    MoPubRepeatableRecyclerViewAdapter(Context context, List<String> dataset, String moPubId) {
        mDataset = dataset;
        mMoPubView = new MoPubView(context);
        mMoPubView.setAdUnitId(moPubId);
        TeadsLocalExtras teadsLocalExtras = new TeadsLocalExtras.Builder()
                                                    .enableDebug()
                                                    .userConsent("1", "11001")
                                                    .adContainerId(mMoPubView.getId())
                                                    .build();
        mMoPubView.setLocalExtras(teadsLocalExtras.getExtras());
    }

    @Override
    public int getItemViewType(int position) {
        return position % AD_INTERVAL == 9 ? TYPE_MOPUB_AD : TYPE_TEXT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_MOPUB_AD:
                return new MoPubRepeatableRecyclerViewAdapter.ViewHolderTeadsAd(mMoPubView);
            case TYPE_TEXT:
            default:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
                return new MoPubRepeatableRecyclerViewAdapter.ViewHolderDemo(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_MOPUB_AD:
                // loading is already done before hand
                break;
            case TYPE_TEXT:
                ((MoPubRepeatableRecyclerViewAdapter.ViewHolderDemo) holder).textView.setText(String.valueOf(position - position / 10));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size() + mDataset.size() / AD_INTERVAL;
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

    void loadBanner() {
        mMoPubView.loadAd();
    }
}