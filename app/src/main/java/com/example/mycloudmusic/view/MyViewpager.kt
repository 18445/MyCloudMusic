package com.example.mycloudmusic.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager2.widget.ViewPager2

/**
 * ViewPager2是final的 无法被重写....
 */


//class MyViewpager : ViewPager2 {
//    constructor(context: Context) : super(context) {
//
//    }
//
//    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//
//    }
//
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
//        context,
//        attrs,
//        defStyleAttr
//    ) {
//
//    }
//
//
//    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
//        if (ev?.action == MotionEvent.ACTION_MOVE){
//            return false
//        }
//
//        return super.onInterceptTouchEvent(ev)
//    }
//
//}