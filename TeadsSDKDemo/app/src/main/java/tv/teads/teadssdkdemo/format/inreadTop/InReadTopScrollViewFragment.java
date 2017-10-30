package tv.teads.teadssdkdemo.format.inreadTop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import tv.teads.sdk.android.AdResponse;
import tv.teads.sdk.android.PublicInterface;
import tv.teads.sdk.android.TeadsAd;
import tv.teads.sdk.android.TeadsAdListener;
import tv.teads.sdk.android.TeadsAdView;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;

/**
 * InReadTop format within a ScrollView
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class InReadTopScrollViewFragment extends BaseFragment implements TeadsAdListener {

    /**
     * The inReadTop ad view
     */
    private TeadsAdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inreadtop_scrollview, container, false);

        // Retrieve ad view
        mAdView = rootView.findViewById(R.id.teads_ad_view);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Instanciate Teads Ad in inReadTop format
        mAdView.setPid(getPid());
        mAdView.debug();
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
        if (mAdView != null && mAdView.getState() == PublicInterface.IDLE) {
            mAdView.load();
        }
    }

    /*//////////////////////////////////////////////////////////////////////////////////////////////////
    * implements TeadsAdListener
    *//////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onAdResponse(TeadsAd teadsAd, AdResponse adResponse) {
        if (!adResponse.isSuccessful()) {
            Toast.makeText(this.getActivity(), getString(R.string.didfail), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void displayAd(TeadsAd teadsAd, int i) {

    }

    @Override
    public void closeAd(TeadsAd teadsAd, boolean b) {

    }
}
