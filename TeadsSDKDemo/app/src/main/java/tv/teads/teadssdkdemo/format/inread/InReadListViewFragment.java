package tv.teads.teadssdkdemo.format.inread;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tv.teads.teadssdkdemo.R;

/**
 * InRead format within a WebView
 *
 * Created by Hugo Gresse on 30/03/15.
 */
public class InReadListViewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_native_inread_webview, container, false);

        return rootView;
    }



}
