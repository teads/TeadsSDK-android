package tv.teads.teadssdkdemo.utils;

import android.support.v4.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import tv.teads.teadssdkdemo.MainActivity;

/**
 * The base fragment
 * Created by Hugo Gresse on 03/04/15.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * Return the pid from activity
     *
     * @return the pid
     */
    protected int getPid() {
        MainActivity activity = (MainActivity) getActivity();
        if(activity == null) return 0;
        return activity.getPid(this.getActivity());
    }

    /**
     * Return the webview url to display
     *
     * @return an url
     */
    public String getWebViewUrl() {
        MainActivity activity = (MainActivity) getActivity();
        if(activity == null) return "";
        return activity.getWebViewUrl(this.getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
