package tv.teads.teadssdkdemo.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.format.videoview.AdvancedVideoViewFragment;
import tv.teads.teadssdkdemo.format.videoview.ScrollViewVideoViewFragment;
import tv.teads.teadssdkdemo.utils.event.ChangeFragmentEvent;

/**
 * A chooser fragment to display the list of sample TeadsVideoView integration.
 * <p/>
 * Created by Hugo Gresse on 07/08/15.
 */
public class VideoViewSampleChooserFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_videoview_chooser, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.videoview_simple)
    public void onSimpleVideoViewClick() {
        BusProvider.getInstance().post(new ChangeFragmentEvent(new ScrollViewVideoViewFragment()));
    }

    @OnClick(R.id.videoview_listview)
    public void onListViewVideoViewClick() {
        BusProvider.getInstance().post(new ChangeFragmentEvent(new AdvancedVideoViewFragment()));
    }

}
