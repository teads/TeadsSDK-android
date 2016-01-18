package tv.teads.teadssdkdemo.format.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import tv.teads.sdk.publisher.TeadsVideoView;
import tv.teads.teadssdkdemo.R;

/**
 * A ListView adapter that display the same {@link TeadsVideoView} each X items.
 * <p/>
 * Created by Hugo Gresse on 09/06/15.
 */
public class VideoViewCustomAdapter extends BaseAdapter {

    public static final String LOG_TAG = "CustomAdapter";

    /**
     * ListView view types
     */
    public static final int TYPE_INREAD   = 1;
    public static final int TYPE_TEXTVIEW = 0;

    /**
     * LayoutInflater instance to inflate view in {@link #getView(int, View, ViewGroup)}
     */
    private LayoutInflater mInflater;

    /**
     * Data displayed in the ListView
     */
    private String mValues[];

    /**
     * Each inReadPosition, the Ad will be repeated/displayed
     */
    private int inReadPosition;

    /**
     * To listen when view is attached
     */
    private TeadsViewAttachListener mTeadsVideoViewAttachListener;

    /**
     * Instantiate the custom adapter with required data
     *
     * @param activity   activity to be used on VideoVIew
     * @param val        datas
     * @param adPosition ad position
     * @param listener   external adapter to be notify on VideoView is attached
     */
    public VideoViewCustomAdapter(Activity activity,
                                  String[] val,
                                  int adPosition,
                                  TeadsViewAttachListener listener) {
        inReadPosition = adPosition;
        mInflater = LayoutInflater.from(activity.getApplicationContext());
        mValues = val;
        mTeadsVideoViewAttachListener = listener;
    }

    @Override
    public int getCount() {
        return mValues.length + 1;
    }

    @Override
    public Object getItem(int position) {

        if (position == inReadPosition) {
            return -1;
        } else if (position > inReadPosition) {
            return mValues[position + 1];
        } else {
            return mValues[position];
        }
    }

    @Override
    public long getItemId(int position) {
        if (position > inReadPosition) {
            return position + 1;
        } else {
            return position;
        }
    }

    @Override
    public int getItemViewType(final int position) {
        if (position % inReadPosition == 0) {
            return TYPE_INREAD;
        }
        return TYPE_TEXTVIEW;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        ViewHolder holder;
        int type = getItemViewType(position);


        /**
         * Check if the given convertView already contains a View inside or if a new view should be inflated.
         */
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case TYPE_TEXTVIEW:
                    convertView = mInflater.inflate(R.layout.list_row, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.listViewText);
                    break;
                case TYPE_INREAD:
                    convertView = mInflater.inflate(R.layout.list_row_videoview, null);
                    holder.videoView = (TeadsVideoView) convertView.findViewById(R.id.videoview);
                    mTeadsVideoViewAttachListener.onAttachTeadsVideoView(holder.videoView);
                    break;
            }

            if (convertView != null) {
                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();

            if (type == TYPE_INREAD) {
                //Notify when the ad view is attached
                mTeadsVideoViewAttachListener.onAttachTeadsVideoView(holder.videoView);
            }
        }
        return convertView;
    }

    /**
     * The ViewHolder used to recycle views
     */
    class ViewHolder {
        public TeadsVideoView videoView;
        public TextView       textView;
    }

    public interface TeadsViewAttachListener {
        void onAttachTeadsVideoView(TeadsVideoView teadsVideoView);
    }


}
