package com.example.mycloudmusic.view

import android.R.attr
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
import android.util.TypedValue
import com.example.mycloudmusic.util.dpToPx
import org.jetbrains.annotations.Nullable
import java.security.AccessController.getContext
import kotlin.properties.Delegates
import android.R.attr.animation


/**
 * 完成开场动画的自定义VIEW
 * 效果为转动的圆弧
 */


class InitCoopenView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var mBorderWidthArc:Int = 0
    var mBorderWidthSquare:Int = 0
    var mCurrentRadian = 0 //当前位置圆弧扫过的地方
    lateinit var rectFArc: RectF
    lateinit var rectFSquare: RectF
    lateinit var valueAnimator: ValueAnimator
    private val mPaintArc  = Paint()
    private val mPaintSquare  = Paint()
    private val mDuration = 1500L

    init {
        initPaint(getContext())
        initRect()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.WHITE)
        //画圆弧或者是矩形，根据基准矩形的的大小和位置确定内部元素的大小和位置
        canvas?.drawArc(rectFArc,90f, mCurrentRadian.toFloat(),false, mPaintArc)
        canvas?.drawRoundRect(rectFSquare, dpToPx(context,8f).toFloat(),dpToPx(context,8f).toFloat(), mPaintSquare)
    }


    /**
     * 圆弧绘画过程
     * 设置时间
     * 用AccelerateDecelerateInterpolator方式更新
     *
     */
    private fun startAnimationDraw() {
        valueAnimator.setFloatValues(270f)
        valueAnimator.duration = mDuration
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener {
            mCurrentRadian = it.animatedValue as Int
            invalidate()
        }
        valueAnimator.start()
    }


    /**
     * 完成基准矩形的自定义
     */
    private fun initRect(){
        //画圆弧的基准矩形
        rectFArc = RectF(
            (mBorderWidthArc / 2 + dpToPx(getContext(),8f)).toFloat(),
            (mBorderWidthArc / 2 + dpToPx(getContext(),8f)).toFloat(),
            (width - mBorderWidthArc / 2 - dpToPx(getContext(),8f)).toFloat(),
            (width - mBorderWidthArc / 2 - dpToPx(getContext(),8f)).toFloat()
        )
        //画矩形的基准矩形
        rectFSquare = RectF(
            (mBorderWidthSquare / 2).toFloat(),
            (mBorderWidthSquare / 2).toFloat(),
            (width - mBorderWidthSquare / 2).toFloat(),
            (width - mBorderWidthSquare / 2).toFloat()
        )
    }

    /**
     * 完成笔触的自定义
     */
    private fun initPaint(context: Context){
        //圆弧宽度
        mBorderWidthArc = dpToPx(context,5f)
        //外框宽度
        mBorderWidthSquare = dpToPx(context,1.5f)


        //颜色设置
        mPaintArc.color = Color.WHITE
        //画笔样式
        mPaintArc.strokeCap = Paint.Cap.ROUND
        //填充样式
        mPaintArc.style = Paint.Style.STROKE
        //抗锯齿设置
        mPaintArc.isAntiAlias = true
        //画笔宽度
        mPaintArc.strokeWidth = mBorderWidthArc.toFloat()

        mPaintSquare.color = Color.WHITE
        mPaintSquare.style = Paint.Style.STROKE
        mPaintSquare.isAntiAlias = true
        mPaintSquare.strokeWidth = mBorderWidthSquare.toFloat()

    }

}






