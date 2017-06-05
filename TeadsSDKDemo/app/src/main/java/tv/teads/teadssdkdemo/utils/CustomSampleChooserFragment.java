package tv.teads.teadssdkdemo.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.format.adview.RepeatedTeadsViewFragment;
import tv.teads.teadssdkdemo.format.adview.CustomRecyclerViewFragment;
import tv.teads.teadssdkdemo.format.adview.ScrollViewAdViewFragment;
import tv.teads.teadssdkdemo.utils.event.ChangeFragmentEvent;

/**
 * A chooser fragment to display the list of sample TeadsView integration.
 * <p/>
 * Created by Hugo Gresse on 07/08/15.
 */
public class CustomSampleChooserFragment extends BaseFragment {

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
        EventBus.getDefault().post(new ChangeFragmentEvent(new ScrollViewAdViewFragment()));
    }

    @OnClick(R.id.videoview_recyclerview)
    public void onRecyclerViewClick() {
        EventBus.getDefault().post(new ChangeFragmentEvent(new CustomRecyclerViewFragment()));
    }
    @OnClick(R.id.videoview_repeatedrecyclerview)
    public void onRepeatedRecyclerViewClick() {
        EventBus.getDefault().post(new ChangeFragmentEvent(new RepeatedTeadsViewFragment()));
    }

    @Subscribe
    public void onReloadEvent(ReloadEvent event){
        // do nothing
    }

}
