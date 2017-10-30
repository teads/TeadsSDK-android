package tv.teads.teadssdkdemo.format.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Locale;

import tv.teads.sdk.android.PublicInterface;
import tv.teads.sdk.android.TeadsAdView;
import tv.teads.teadssdkdemo.R;

/**
 * Display ad in a ListView
 * TODO Doesn't works after a rattach
 * Created by Benjamin Volland on 10/08/2017.
 */

public class ListViewAdapter extends BaseAdapter {

    private static final int TYPE_TEADS = 0;
    private static final int TYPE_TEXT  = 1;
    private int         mPid;
    private TeadsAdView mAdView;
    private Context     mContext;


    public ListViewAdapter(Context context, int pid) {
        mPid = pid;
        mContext = context;
    }

    public int getItemViewType(int position) {
        return position == 10 ? TYPE_TEADS : TYPE_TEXT;
    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        switch (getItemViewType(position)) {
            case TYPE_TEADS:
                if (view == null || !(view instanceof TeadsAdView)) {
                    if (mAdView == null) {
                        view = mAdView = new TeadsAdView(mContext);
                        mAdView.setPid(mPid);
                        mAdView.debug();
                        mAdView.load();
                    } else {
                        view = mAdView;
                    }
                }
                break;
            case TYPE_TEXT:
                if (view == null || (view instanceof TeadsAdView)) {
                    view = LayoutInflater.from(mContext).inflate(R.layout.list_row, parent, false);
                }
                ((TextView) view.findViewById(R.id.listViewText)).setText(String.format(Locale.ENGLISH,
                                                                                        "Position: %d",
                                                                                        position));
                break;
        }
        return view;
    }

    public void reloadAd() {
        if (mAdView != null && mAdView.getState() == PublicInterface.IDLE) {
            mAdView.load();
        }
    }

    public void cleanAdView() {
        if (mAdView != null) {
            mAdView.clean();
        }
    }
}
