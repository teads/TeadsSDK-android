package tv.teads.teadssdkdemo.utils;

import android.support.v4.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import tv.teads.sdk.publisher.TeadsConfiguration;
import tv.teads.teadssdkdemo.MainActivity;

/**
 *
 * Created by Hugo Gresse on 03/04/15.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * Return the pid from activity
     *
     * @return the pid
     */
    public String getPid() {
        return ((MainActivity) getActivity()).getPid(this.getActivity());
    }

    /**
     * Return the webview url to display
     *
     * @return an url
     */
    public String getWebViewUrl() {
        return ((MainActivity) getActivity()).getWebViewUrl(this.getActivity());
    }

    /**
     * Return the end screen mode
     *
     * @return {@link tv.teads.sdk.publisher.TeadsConfiguration#DARK_MODE} or
     * {@link tv.teads.sdk.publisher.TeadsConfiguration#LIGHT_MODE}
     */
    public int getEndScreenMode() {
        if (((MainActivity) getActivity()).isEndScreenLightMode(this.getActivity())) {
            return TeadsConfiguration.LIGHT_MODE;
        }

        return TeadsConfiguration.DARK_MODE;
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
