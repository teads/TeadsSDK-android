package tv.teads.teadssdkdemo.format.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tv.teads.sdk.android.TeadsAdView;
import tv.teads.teadssdkdemo.R;

/**
 * A RecyclerView adapter that display the same {@link TeadsAdView} each X items.
 * <p/>
 * Created by Hugo Gresse on 09/06/15.
 */
public class RepeatedTeadsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TEADS = 0;
    private static final int TYPE_TEXT  = 1;
    private int mPid;


    public RepeatedTeadsViewAdapter(int pid) {
        mPid = pid;
    }

    @Override
    public int getItemViewType(int position) {
        return (position + 1) % 9 == 0 ? TYPE_TEADS : TYPE_TEXT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TEADS:
                TeadsAdView teadsAdView = new TeadsAdView(parent.getContext());
                teadsAdView.setPid(mPid);
                return new ViewHolderTeadsAd(teadsAdView);
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
                ((ViewHolderDemo) holder).textView.setText("test");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 50;
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
