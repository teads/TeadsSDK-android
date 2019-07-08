/*
 * Copyright (c) Teads 2018.
 */

package tv.teads.webviewhelper.baseView

import android.content.Context
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * Wrap any view to intercept scroll event and notify the given listener.
 * Useful to move any view based on move/drag event on this view.
 */
class ObservableWrapperView(context: Context, viewGroup: ViewGroup) : FrameLayout(context) {

    private var listener: Listener? = null
    private var startNativeY: Float = 0.toFloat()

    init {
        this.addView(viewGroup)
    }

    fun clean() {
        listener = null
    }

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> startNativeY = event.y
            MotionEvent.ACTION_MOVE -> if (listener != null) {
                val movY = (startNativeY.toInt() - event.y).toInt()
                listener!!.onActionMove(0, movY)
            }
        }

        return super.onInterceptTouchEvent(event)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (listener != null) {
            listener!!.onSizeChanged(w, h, oldw, oldh)
        }
    }

    interface Listener {
        fun onActionMove(moveX: Int, moveY: Int)
        fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int)
    }
}
