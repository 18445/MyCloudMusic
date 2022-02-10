package com.example.mycloudmusic.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.mycloudmusic.util.dpToPx

class RoundBackgroundView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val mPaint = Paint()
    private val rectF: RectF by lazy{
        LazyThreadSafetyMode.NONE
        val bia = dpToPx(context!!,2f).toFloat()
        RectF(
            (width - bia).toFloat(),
            (width - bia ).toFloat(),
            bia,
            bia,
        )
    }

    init {
        initPaint(getContext())
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawArc(rectF,0f,360f,true,mPaint)
    }


    /**
     * 完成笔触的自定义
     */
    private fun initPaint(context: Context) {

        //颜色设置
        mPaint.color = Color.GRAY
        //画笔样式
        mPaint.strokeCap = Paint.Cap.ROUND
        //抗锯齿设置
        mPaint.isAntiAlias = true

    }
}