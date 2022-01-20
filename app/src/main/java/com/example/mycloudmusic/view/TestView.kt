package com.example.mycloudmusic.view

import android.view.View
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.animation.AccelerateDecelerateInterpolator
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import com.example.mycloudmusic.util.dpToPx

/**
 * 此View为自定义View的测试类
 * 可以忽略
 */

class Zhview(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    val mBorderWidthArc = dpToPx(getContext(), 5f)
    private val mPaintArc = Paint()
    private val rectFArc: RectF by lazy {
        LazyThreadSafetyMode.NONE
        RectF(
            (width - mBorderWidthArc / 2 - dpToPx(getContext(), 8f)).toFloat(),
            (width - mBorderWidthArc / 2 - dpToPx(getContext(), 8f)).toFloat(),
            (mBorderWidthArc / 2 + dpToPx(getContext(), 8f)).toFloat(),
            (mBorderWidthArc / 2 + dpToPx(getContext(), 8f)).toFloat(),
        )
    }
            @SuppressLint("DrawAllocation")
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
                //颜色设置
                mPaintArc.color = Color.RED
                //画笔样式
//        mPaintArc.strokeCap = Paint.Cap.ROUND
                //填充样式
                mPaintArc.style = Paint.Style.STROKE
                //抗锯齿设置
                mPaintArc.isAntiAlias = true
                //画笔宽度
                mPaintArc.strokeWidth = mBorderWidthArc.toFloat()
                canvas.drawRect(rectFArc,mPaintArc)
                canvas.drawArc(rectFArc.left,rectFArc.top,rectFArc.right,rectFArc.bottom,0f,180f,true,mPaintArc)
                Log.d("aaa",rectFArc.toString()+rectFArc.toShortString())
        }
    }