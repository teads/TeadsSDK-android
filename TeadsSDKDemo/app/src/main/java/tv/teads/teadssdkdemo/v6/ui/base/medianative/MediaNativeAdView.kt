package tv.teads.teadssdkdemo.v6.ui.base.medianative

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import tv.teads.sdk.renderer.NativeAdView
import tv.teads.teadssdkdemo.R

/**
 * Custom view for displaying Teads media native ads.
 * Extends FrameLayout and encapsulates the media native ad layout.
 */
class MediaNativeAdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val nativeAdView: NativeAdView
    private val rootView: ViewGroup

    init {
        // Inflate the media native ad layout
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.media_native_ad_view, this, true)
        
        // Find the NativeAdView
        nativeAdView = findViewById(R.id.native_ad_view)
        rootView = findViewById(R.id.native_root_ad_view)
    }

    /**
     * Returns the NativeAdView for binding ads.
     * 
     * @return The NativeAdView instance
     */
    fun getNativeAdView(): NativeAdView = nativeAdView

    fun isVisible(state: Boolean) {
        rootView.visibility = if (state) View.VISIBLE else View.GONE
    }
}
