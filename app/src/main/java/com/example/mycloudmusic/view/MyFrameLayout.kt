package com.example.mycloudmusic.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import kotlin.math.abs

class MyFrameLayout : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mInitialTouchX = 0F
    private var mInitialTouchY = 0F
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.e("dispatchTouchEvent", "dispatchTouchEvent ${ev?.action}")
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                mInitialTouchX = ev.x
                mInitialTouchY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = ev.x - mInitialTouchX
                val dy = ev.y - mInitialTouchY
                var hasScrollView = false
                //子类中要有能滑动的项
                for (i in 0 until childCount) {
                    val child = getChildAt(i)
                    //能够左右滑动
                    if (child.canScrollHorizontally(-1) && dx > 0) {
                        hasScrollView = true
                    }
                    if (child.canScrollHorizontally(1) && dx < 0) {
                        hasScrollView = true
                    }
                }
                //判断是否是左右滑动
                val angle = abs(dy) / abs(dx)
                if (angle < 0.6f && hasScrollView) {
                    //拦截父类
                    requestDisallowInterceptTouchEvent(true)
                }
            }
            MotionEvent.ACTION_UP -> {
                requestDisallowInterceptTouchEvent(false)
            }
            MotionEvent.ACTION_CANCEL -> {
                requestDisallowInterceptTouchEvent(false)
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}