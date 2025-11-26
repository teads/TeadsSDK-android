package tv.teads.webviewhelper.baseView

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

open class ObservableContainerAdView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var moveListener: ActionMoveListener? = null
    private var startNativeY: Float = 0f

    fun setMoveListener(moveListener: ActionMoveListener) {
        this.moveListener = moveListener
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> startNativeY = event.y
            MotionEvent.ACTION_MOVE -> {
                if (moveListener != null) {
                    val movY = (startNativeY - event.y).toInt()
                    moveListener?.onActionMove(0, movY)
                }
            }
        }

        return super.onInterceptTouchEvent(event)
    }

    interface ActionMoveListener {
        fun onActionMove(moveX: Int, moveY: Int)
    }
}