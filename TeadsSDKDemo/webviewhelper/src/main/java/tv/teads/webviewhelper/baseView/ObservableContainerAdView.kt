package tv.teads.webviewhelper.baseView

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

open class ObservableContainerAdView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var touchForwardTarget: View? = null

    fun setTouchForwardTarget(target: View) {
        this.touchForwardTarget = target
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        forwardTouchEvent(event)
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            performClick()
        }
        forwardTouchEvent(event)
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private fun forwardTouchEvent(event: MotionEvent) {
        touchForwardTarget?.let { target ->
            // Create a copy of the event
            val forwardEvent = MotionEvent.obtain(event)

            // Get screen positions of both views
            val thisLocation = IntArray(2)
            val targetLocation = IntArray(2)
            this.getLocationOnScreen(thisLocation)
            target.getLocationOnScreen(targetLocation)

            // Translate coordinates: from ad view space to WebView space
            forwardEvent.offsetLocation(
                (thisLocation[0] - targetLocation[0]).toFloat(),
                (thisLocation[1] - targetLocation[1]).toFloat()
            )

            target.dispatchTouchEvent(forwardEvent)
            forwardEvent.recycle()
        }
    }
}
