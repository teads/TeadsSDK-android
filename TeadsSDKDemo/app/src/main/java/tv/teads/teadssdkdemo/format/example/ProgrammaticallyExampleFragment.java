package tv.teads.teadssdkdemo.format.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import tv.teads.sdk.android.AdFailedReason;
import tv.teads.sdk.android.CustomAdView;
import tv.teads.sdk.android.TeadsListener;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;

/**
 * Different integration example
 */
public class ProgrammaticallyExampleFragment extends BaseFragment {

    /**
     * Teads Ad view
     */
    private CustomAdView mAdView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_example_programmatically, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        FrameLayout container = view.findViewById(R.id.teadsContainer);

        mAdView = new CustomAdView(getContext());

        // Instanciate Teads Ad in inReadTop format
        mAdView.setPid(getPid());
        mAdView.setListener(mTeadsListener);
        mAdView.load();
        container.addView(mAdView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mAdView != null) {
            mAdView.clean();
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onReloadEvent(ReloadEvent event) {
        if (mAdView != null) {
            mAdView.load();
        }
    }

    private final TeadsListener mTeadsListener = new TeadsListener(){

        @Override
        public void onAdFailedToLoad(AdFailedReason adFailedReason) {
            Toast.makeText(ProgrammaticallyExampleFragment.this.getActivity(), getString(R.string.didfail), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(String s) {
            Toast.makeText(ProgrammaticallyExampleFragment.this.getActivity(), getString(R.string.didfail_playback), Toast.LENGTH_SHORT).show();
        }
    };

}
