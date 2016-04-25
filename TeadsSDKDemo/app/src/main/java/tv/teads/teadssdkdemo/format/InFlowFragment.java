package tv.teads.teadssdkdemo.format;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tv.teads.sdk.publisher.TeadsInterstitial;
import tv.teads.sdk.publisher.TeadsInterstitialEventListener;
import tv.teads.teadssdkdemo.R;
import tv.teads.teadssdkdemo.utils.BaseFragment;
import tv.teads.utils.TeadsError;

/**
 * InFlow format (launch by developer)
 * <p/>
 * Created by Hugo Gresse on 30/03/15.
 */
public class InFlowFragment extends BaseFragment implements TeadsInterstitialEventListener {

    /**
     * Teads Interstitial
     */
    private TeadsInterstitial mTeadsInterstitial;

    /**
     * Button to load or show the interstitial
     */
    private Button mLoadShowButton;

    /**
     * Simple TextView to notify user of the Interstitial state
     */
    private TextView mTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_interstitial_inflow, container, false);
        mLoadShowButton = (Button) rootView.findViewById(R.id.load_show_button);
        mTextView = (TextView) rootView.findViewById(R.id.result);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mLoadShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTeadsInterstitial.show();
            }
        });

        // Instanciate Teads Video in inRead format
        mTeadsInterstitial = new TeadsInterstitial.TeadsInterstitialBuilder(
                this.getActivity(),
                this.getPid())
                .eventListener(this)
                .build();

    }

    @Override
    public void onResume() {
        super.onResume();

        // Load the Ad
        mTeadsInterstitial.load();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mTeadsInterstitial != null) {
            mTeadsInterstitial.clean();
        }
    }

    /*----------------------------------------
    * implements TeadsInterstitialEventListener
    */

    @Override
    public void teadsInterstitialDidFailLoading(TeadsError teadsError) {
        updateLogTextView("Interstitial fail to load. " + teadsError.toString());
    }

    @Override
    public void teadsInterstitialWillLoad() {
        updateLogTextView("Interstitial will load");
    }

    @Override
    public void teadsInterstitialDidLoad() {
        updateLogTextView("Interstitial is loaded, ready to show!");
    }

    @Override
    public void teadsInterstitialWillTakeOverFullScreen() {

    }

    @Override
    public void teadsInterstitialDidTakeOverFullScreen() {

    }

    @Override
    public void teadsInterstitialWillDismissFullScreen() {

    }

    @Override
    public void teadsInterstitialDidDismissFullScreen() {
        updateLogTextView("Interstitial did dismiss fullscreen");
    }


    @Override
    public void teadsInterstitialDidClean() {

    }


    private void updateLogTextView(String text) {
        mTextView.setText(mTextView.getText() + " \n" + text);
    }
}
