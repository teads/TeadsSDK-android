package tv.teads.teadssdkdemo.format;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tv.teads.teadssdkdemo.R;

/**
 * InSwipe format within a ViewPager
 *
 * Created by Hugo Gresse on 30/03/15.
 */
public class InSwipeViewPagerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_native_inswipe, container, false);

        return rootView;
    }



}
