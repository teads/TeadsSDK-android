package tv.teads.teadssdkdemo.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Listen to lock screen event : on device lock and when user unlock the device and comme back to
 * app
 *
 * Created by Hugo Gresse on 30/12/14.
 */
public class ApplicationVisibility extends BroadcastReceiver {

    protected Context mContext;
    protected VisibilityListener mVisibilityListener;

    public ApplicationVisibility(Context context, VisibilityListener visibilityListener) {
        mVisibilityListener = visibilityListener;
        mContext = context;

        // Register the broadcast receiver
        mContext.registerReceiver(this, new IntentFilter(Intent.ACTION_USER_PRESENT));
        mContext.registerReceiver(this, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    public void clear(){
        mVisibilityListener = null;

        try {
            mContext.unregisterReceiver(this);
        } catch (IllegalArgumentException e){
            // Already unregistered, no nothing
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            mVisibilityListener.onVisible();
        } else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            mVisibilityListener.onHidden();
        }

    }

    public interface VisibilityListener{
        void onVisible();
        void onHidden();
    }
}
