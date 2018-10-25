package tv.teads.teadssdkdemo.format.mediation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.greenrobot.eventbus.Subscribe;

import tv.teads.adapter.admob.TeadsAdNetworkExtras;
import tv.teads.adapter.admob.TeadsAdapter;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;

/**
 * Display inRead as Banner within a ScrollView using AdMob Mediation.
 */
public class AdMobBannerFragment extends BaseFragment {

    // This is is should be replaced by your own AdMob application and ad block ids
    public static final String ADMOB_TEADS_APP_ID = "ca-app-pub-3570580224725271~3869556230";
    public static final String ADMOB_TEADS_BANNER_ID = "ca-app-pub-3570580224725271/1481793511";

    private FrameLayout mBannerAdFrame;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admob_banner, container, false);
        mBannerAdFrame = rootView.findViewById(R.id.banner_ad_frame);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // 1. Init AdMob (could be done in your Application class)
        MobileAds.initialize(getContext(), ADMOB_TEADS_APP_ID);

        // 2.Create AdMob viewa nd add it to hierarchy
        AdView adView = new AdView(view.getContext());
        adView.setAdUnitId(ADMOB_TEADS_BANNER_ID);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        mBannerAdFrame.addView(adView);

        // 3. Attach listener (will include Teads callbacks)
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // maybe track it on GA?
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getContext(), "Ad loading failed: onAdFailedToLoad(" + errorCode + ")", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Nothing to do
            }

            @Override
            public void onAdLeftApplication() {
                // Nothing to do
            }

            @Override

            public void onAdClosed() {
                // Nothing to do
            }
        });

        // 4. Load a new ad (this will call AdMob that will and Teads afterward)
        TeadsAdNetworkExtras extras = new TeadsAdNetworkExtras.Builder()
                                              // Needed by european regulation
                                              // See https://mobile.teads.tv/sdk/documentation/android/gdpr-consent
                                              .userConsent("1", "0001")
                                              // The article url if you are a news publisher
                                              .pageUrl("https://page.com/article1/")
                                              .build();
        AdRequest adRequest = new AdRequest.Builder()
                                      .addCustomEventExtrasBundle(TeadsAdapter.class, extras.getExtras())
                                      .build();

        adView.loadAd(adRequest);
    }


    @Subscribe
    @SuppressWarnings("unused")
    public void onReloadEvent(ReloadEvent event) {
        // not used
    }

}
