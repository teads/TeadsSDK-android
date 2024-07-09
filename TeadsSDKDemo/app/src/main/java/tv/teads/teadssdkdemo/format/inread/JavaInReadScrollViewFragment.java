package tv.teads.teadssdkdemo.format.inread;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import tv.teads.sdk.AdOpportunityTrackerView;
import tv.teads.sdk.AdPlacementSettings;
import tv.teads.sdk.AdRatio;
import tv.teads.sdk.AdRequestSettings;
import tv.teads.sdk.InReadAdPlacement;
import tv.teads.sdk.InReadAdViewListener;
import tv.teads.sdk.TeadsSDK;
import tv.teads.sdk.VideoPlaybackListener;
import tv.teads.sdk.renderer.InReadAdView;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.databinding.FragmentInreadScrollviewBinding;
import tv.teads.teadssdkdemo.utils.BaseFragment;

public class JavaInReadScrollViewFragment extends BaseFragment {
    private FragmentInreadScrollviewBinding binding;
    private InReadAdPlacement adPlacement;
    private InReadAdView inReadAdView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInreadScrollviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView integrationHeader = view.findViewById(R.id.integration_header);
        integrationHeader.setText(getTitle());

        // 1. Setup the settings
        AdPlacementSettings placementSettings = new AdPlacementSettings.Builder()
                .enableDebug()
                .build();

        // 2. Create the InReadAdPlacement
        adPlacement = TeadsSDK.INSTANCE.createInReadPlacement(requireActivity(), getPid(), placementSettings);

        // 3. Request the ad and listen to its events
        AdRequestSettings requestSettings = new AdRequestSettings.Builder()
                .pageSlotUrl("http://teads.com")
                .build();

        adPlacement.requestAd(requestSettings, new InReadAdViewListener() {
            @Override
            public void onAdReceived(@NonNull InReadAdView ad, @NonNull AdRatio adRatio) {
                ViewGroup.LayoutParams layoutParams = binding.adSlotView.getLayoutParams();

                binding.adSlotView.addView(ad);
                layoutParams.height = adRatio.calculateHeight(binding.adSlotView.getMeasuredWidth());
                binding.adSlotView.setLayoutParams(layoutParams);

                inReadAdView = ad;
            }

            @Override
            public void adOpportunityTrackerView(@NonNull AdOpportunityTrackerView trackerView) {
                binding.adSlotView.addView(trackerView);
            }

            @Override
            public void onAdRatioUpdate(@NonNull AdRatio adRatio) {
                if (inReadAdView != null) {
                    ViewGroup.LayoutParams layoutParams = inReadAdView.getLayoutParams();
                    layoutParams.height = adRatio.calculateHeight(binding.adSlotView.getMeasuredWidth());
                    binding.adSlotView.setLayoutParams(layoutParams);
                }
            }

            @Override
            public void onAdClicked() {
                Log.d("inRead listener event", "onAdClicked");
            }

            @Override
            public void onAdClosed() {
                Log.d("inRead listener event", "onAdClosed");
            }

            @Override
            public void onAdError(int code, @NonNull String description) {
                Log.d("inRead listener event", "onAdError");
            }

            @Override
            public void onAdImpression() {
                Log.d("inRead listener event", "onAdImpression");
            }

            @Override
            public void onAdExpandedToFullscreen() {
                Log.d("inRead listener event", "onAdExpandedToFullscreen");
            }

            @Override
            public void onAdCollapsedFromFullscreen() {
                Log.d("inRead listener event", "onAdCollapsedFromFullscreen");
            }

            @Override
            public void onFailToReceiveAd(@NonNull String failReason) {
                Log.d("inRead listener event", "onFailToReceiveAd");
            }
        }, new VideoPlaybackListener() {
            @Override
            public void onVideoComplete() {
                Log.d("PlaybackEvent", "complete");
            }

            @Override
            public void onVideoPause() {
                Log.d("PlaybackEvent", "pause");
            }

            @Override
            public void onVideoPlay() {
                Log.d("PlaybackEvent", "play");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (inReadAdView != null) {
            inReadAdView.clean();
        }
    }

    @NonNull
    @Override
    public String getTitle() {
        return "Java InRead Direct ScrollView";
    }
}
