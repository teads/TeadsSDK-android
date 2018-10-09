package tv.teads.teadssdkdemo.utils.event;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Event posted when an inner activity's fragment wan't to change the displayed fragment
 *
 * Created by Hugo Gresse on 07/08/15.
 */
public class ChangeFragmentEvent {

    @NonNull
    public final Fragment mFragment;

    public ChangeFragmentEvent(@NonNull Fragment fragment) {
        mFragment = fragment;
    }
}
