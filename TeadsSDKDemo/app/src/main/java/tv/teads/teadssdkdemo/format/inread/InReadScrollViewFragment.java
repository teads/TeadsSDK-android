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
import tv.teads.sdk.android.TeadsAd;
import tv.teads.sdk.android.TeadsListener;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;

/**
 * InRead format within a ScrollView
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class InReadScrollViewFragment extends BaseFragment implements TeadsListener {

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

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
    * implements TeadsAdListener
    *//////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onAdFailedToLoad(TeadsAd teadsAd, AdFailedReason adFailedReason) {
        Toast.makeText(this.getActivity(), getString(R.string.didfail), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdLoaded(TeadsAd teadsAd, float v) {

    }

    @Override
    public void closeAd(TeadsAd teadsAd, boolean b) {

    }

    @Override
    public void onError(TeadsAd teadsAd, String s) {
        Toast.makeText(this.getActivity(), getString(R.string.didfail_playback), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdDisplayed(TeadsAd teadsAd) {

    }
}
