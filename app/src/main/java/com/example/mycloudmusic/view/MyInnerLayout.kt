package com.example.mycloudmusic.view
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class MyInnerLayout : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr
    )

    private var mInitialTouchX = 0F
    private var mInitialTouchY = 0F
        override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
            when (ev?.action) {
                MotionEvent.ACTION_DOWN -> {
                    mInitialTouchX = ev.x
                    mInitialTouchY = ev.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = ev.x - mInitialTouchX
                    val dy = ev.y - mInitialTouchY
                    //判断是否是左右滑动
                    if (dx < 20||dy < 50) {
                        //拦截子类
                        return true
                    }
                }
            }
        return super.onInterceptTouchEvent(ev)
    }
}