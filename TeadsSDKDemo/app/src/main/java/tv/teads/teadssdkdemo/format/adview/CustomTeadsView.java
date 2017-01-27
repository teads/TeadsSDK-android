package tv.teads.teadssdkdemo.format.adview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import tv.teads.sdk.publisher.TeadsView;

/**
 * Created by Hugo Gresse on 23/01/2017.
 */
public class CustomTeadsView extends TeadsView {

    @Nullable
    private Listener mListener;

    public CustomTeadsView(Context context) {
        super(context);
    }

    public CustomTeadsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomTeadsView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setListener(@Nullable Listener listener){
        mListener = listener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mListener != null) {
            mListener.onAttachToWindow();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mListener != null) {
            mListener.onDetachedFromWindow();
        }
    }

    public interface Listener {
        void onAttachToWindow();
        void onDetachedFromWindow();
    }
}
