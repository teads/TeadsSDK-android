package tv.teads.teadssdkdemo.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tv.teads.teadssdkdemo.R;

/**
 * AN empty fragment used for ViewPager demo
 *
 * Created by Hugo Gresse on 31/03/15.
 */
public class EmptyFragment extends Fragment {

    @InjectView(R.id.numberTextView)
    public TextView mTextView;
    @InjectView(R.id.leftImageView)
    public ImageView mLeftImage;
    @InjectView(R.id.rightImageView)
    public ImageView mRightImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_viewpager_blank, container, false);
        ButterKnife.inject(this, rootView);

        Bundle bundle = getArguments();

        mTextView.setText(String.valueOf(bundle.getInt("number")));

        if(bundle.getBoolean("isLeft")){
            mLeftImage.setVisibility(View.GONE);
        } else {
            mRightImage.setVisibility(View.GONE);
        }

        return rootView;
    }
}
