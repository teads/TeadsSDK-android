package tv.teads.teadssdkdemo.utils;

import android.support.v4.app.Fragment;

import tv.teads.teadssdkdemo.MainActivity;

/**
 * Created by Hugo Gresse on 03/04/15.
 */
public class BaseFragment extends Fragment {

    /**
     * Return the pid from activity
     * @return the pid
     */
    public String getPid(){
        return ((MainActivity)getActivity()).getPid(this.getActivity());
    }

    /**
     * Return the webview url to display
     * @return an url
     */
    public String getWebViewUrl(){
        return ((MainActivity)getActivity()).getWebViewUrl(this.getActivity());
    }

}
