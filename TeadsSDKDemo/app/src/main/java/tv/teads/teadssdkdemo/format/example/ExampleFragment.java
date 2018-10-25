package tv.teads.teadssdkdemo.format.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.format.mediation.AdMobBannerFragment;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;
import tv.teads.teadssdkdemo.utils.event.ChangeFragmentEvent;

/**
 * Different integration example
 */
public class ExampleFragment extends BaseFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_example, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.programmaticallyExampleButton)
    public void programmaticalyExample() {
        EventBus.getDefault().post(new ChangeFragmentEvent(new ProgrammaticallyExampleFragment()));
    }

    @OnClick(R.id.admobButton)
    public void adMobExample() {
        EventBus.getDefault().post(new ChangeFragmentEvent(new AdMobBannerFragment()));
    }

    @Subscribe
    public void onReloadEvent(ReloadEvent event){
        // do nothing
    }
}
