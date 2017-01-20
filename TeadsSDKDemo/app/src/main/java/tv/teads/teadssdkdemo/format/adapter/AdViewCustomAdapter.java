package tv.teads.teadssdkdemo.format.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tv.teads.sdk.publisher.TeadsView;
import tv.teads.teadssdkdemo.R;

/**
 * A RecyclerView adapter that display the same {@link TeadsView} each X items.
 * <p/>
 * Created by Hugo Gresse on 09/06/15.
 */
public class AdViewCustomAdapter extends RecyclerView.Adapter<AdViewCustomAdapter.ViewHolder> {

    /**
     * View types
     */
    private static final int TYPE_INREAD   = 1;
    private static final int TYPE_TEXTVIEW = 0;

    /**
     * Data displayed in the RecyclerView
     */
    private String mValues[];

    /**
     * Each inReadPosition, the Ad will be repeated/displayed
     */
    private int inReadPosition;

    /**
     * To listen when view is attached
     */
    private TeadsViewAttachListener mTeadsAdViewAttachListener;

    /**
     * Instantiate the custom adapter with required data
     *
     * @param val        datas
     * @param adPosition ad position
     * @param listener   external adapter to be notify on TeadsView is attached
     */
    public AdViewCustomAdapter(String[] val,
                               int adPosition,
                               TeadsViewAttachListener listener) {
        inReadPosition = adPosition;
        mValues = val;
        mTeadsAdViewAttachListener = listener;
    }

    @Override
    public int getItemCount() {
        return mValues.length + 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
                return new TextViewHolder(v);
            case TYPE_INREAD:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_adview, parent, false);
                return new AdViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position > inReadPosition) {
            holder.setData(mValues[position -1]);
        } else {
            holder.setData(mValues[position]);
        }
    }

    @Override
    public int getItemViewType(final int position) {
        if (position % inReadPosition == 0 && position != 0) {
            return TYPE_INREAD;
        }
        return TYPE_TEXTVIEW;
    }

    /**
     * The ViewHolder used to recycle views
     */
    abstract class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(Object object){

        }
    }

    /**
     * The ViewHolder for TextView
     */
    private class TextViewHolder extends ViewHolder {
        private TextView mTextView;

        TextViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.listViewText);
        }

        @Override
        public void setData(Object object) {
            super.setData(object);
            mTextView.setText(object.toString());
        }
    }

    /**
     * The ViewHolder to display ads
     */
    private class AdViewHolder extends ViewHolder{
        private TeadsView mAdView;

        AdViewHolder(View itemView) {
            super(itemView);

            mAdView = (TeadsView) itemView.findViewById(R.id.adview);
        }

        @Override
        public void setData(Object object) {
            super.setData(object);
            mTeadsAdViewAttachListener.onAttachTeadsAdView(mAdView);
        }
    }

    public interface TeadsViewAttachListener {
        void onAttachTeadsAdView(TeadsView teadsAdView);
    }


}
