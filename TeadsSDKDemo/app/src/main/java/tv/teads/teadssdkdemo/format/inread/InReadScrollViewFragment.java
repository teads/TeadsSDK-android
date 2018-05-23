package tv.teads.teadssdkdemo.format.inread;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import tv.teads.sdk.android.AdFailedReason;
import tv.teads.sdk.android.InReadAdView;
import tv.teads.sdk.android.TeadsListener;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;

/**
 * InRead format within a ScrollView
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class InReadScrollViewFragment extends BaseFragment {

    /**
     * Teads Ad view
     */
    private InReadAdView mAdView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inread_scrollview, container, false);
        mAdView = rootView.findViewById(R.id.teads_ad_view);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        // Instanciate Teads Ad in inReadTop format
        mAdView.setPid(getPid());
        mAdView.setListener(mTeadsListener);
        mAdView.load();
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

    private TeadsListener mTeadsListener = new TeadsListener(){

        @Override
        public void onAdFailedToLoad(AdFailedReason adFailedReason) {
            Toast.makeText(InReadScrollViewFragment.this.getActivity(), getString(R.string.didfail), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(String s) {
            Toast.makeText(InReadScrollViewFragment.this.getActivity(), getString(R.string.didfail_playback), Toast.LENGTH_SHORT).show();
        }
    };

}
