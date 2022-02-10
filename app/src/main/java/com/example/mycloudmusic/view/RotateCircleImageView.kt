package com.example.mycloudmusic.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.content.res.TypedArray
import android.graphics.*
import com.example.mycloudmusic.R
import com.example.mycloudmusic.util.dpToPx
import android.graphics.PorterDuffXfermode
import android.graphics.Bitmap
import android.view.animation.DecelerateInterpolator


class RotateCircleImageView (context: Context?, private val attrs: AttributeSet?, private val defStyleAttr:Int) : View(context, attrs) {
    private lateinit var image: Bitmap
    private lateinit var tempImage: Bitmap

    private lateinit var bitmap: Bitmap
    private lateinit var pdf: PorterDuffXfermode
    private lateinit var bitmapPaint: Paint
    private lateinit var canvas: Canvas
    private var isCreateBitmap = false





    private var blackWidth //黑色圆边框的宽度
            = 0
    private var rotateDirection = 0 //旋转方向 0=顺时针 1=逆时针

    private var rotateAngle = 0.8f //每次旋转的角度(0.1f - 1f)

    private var rotateX  = 0f //控制选择的变量
    private var isRotate = false //控制是否旋转

    private lateinit var mPaint: Paint

    init {
        initData()
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //测量模式
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        //大小
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        //需要设置的大小
        val mWidth: Int
        val mHeight: Int
        //预测宽度
        var assumeWidth = widthSize
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize//指定大小或填充窗体，直接设置最终宽度
        } else {
            assumeWidth = tempImage.width;//预测宽度等于图片宽度
            mWidth = assumeWidth + paddingLeft + paddingRight//最终宽度等于预测宽度加左右Padding宽度
        }

        mHeight = if (heightMode == MeasureSpec.EXACTLY) {
            heightSize
        } else {
            //控件等宽高
            paddingTop + paddingBottom + assumeWidth;
        }

        //控制图片大小并缩放
        image = if (tempImage.height < tempImage.width) {
            //如果图片高度比宽度小，则强制拉伸
            Bitmap.createScaledBitmap(
                tempImage, assumeWidth - blackWidth,
                assumeWidth - blackWidth, false
            )
        } else {
            //缩放方法把图片缩放成指定大小（宽度等于预测的宽度，高度按比例缩放）
            //该方法根据参数的宽高强制缩放图片，所以这里根据宽度算出缩放后的高度
            Bitmap.createScaledBitmap(
                tempImage, assumeWidth - blackWidth, (tempImage.height /
                        (tempImage.width.toFloat() / assumeWidth) - blackWidth).toInt(), false
            )
        }
        //设置View的宽高
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制黑色圆
        canvas?.drawCircle(
            (width / 2).toFloat(),
            (width / 2).toFloat(), (width / 2).toFloat(), mPaint
        )
        //绘制图片
        canvas?.drawBitmap(
            getCircleBitmap(image, image.width, rotateAngle),
            (width / 2 - image.width / 2).toFloat(),
            (height / 2 - image.width / 2).toFloat(), mPaint
        )
        if (isRotate) {
            //16毫秒后启动子线程刷新界面
            handler.postDelayed({
                invalidate() //刷新界面
            }, 16)
        }
    }

    private fun getCircleBitmap(image:Bitmap,width:Int,rotate:Float):Bitmap{
        //只需要执行一次的代码
        if (!isCreateBitmap) {
            bitmapPaint = Paint()
            bitmapPaint.isAntiAlias = true
            bitmapPaint.isDither = true
            //创建一个指定宽高的空白bitmap
            bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
            isCreateBitmap = true
            //用那个空白bitmap创建一个画布
            canvas = Canvas (bitmap)
            //在画布上画个圆
            canvas.drawCircle((width / 2).toFloat(), (width / 2).toFloat(),
                (width / 2).toFloat(), bitmapPaint)
            //混合模式为保留后者相交的部分
            pdf = PorterDuffXfermode (PorterDuff.Mode.SRC_IN)
        }
        //设置混合模式
        bitmapPaint.xfermode = pdf
        //旋转画布
        if (rotateDirection == 0) {
            canvas.rotate(rotate, (width / 2).toFloat(), (width / 2).toFloat())//顺时针
        } else {
            canvas.rotate(-rotate, (width / 2).toFloat(), (width / 2).toFloat())//逆时针
        }
        canvas.drawBitmap(image, 0f, 0f, bitmapPaint);//绘制图片，（图片会被旋转）
        bitmapPaint.xfermode = null
        //这个bitmap在画布中被旋转，画圆，返回后就是一个圆形的bitmap
        return bitmap
    }

    private fun initData(){
        mPaint = Paint()
        //抗锯齿
        mPaint.isAntiAlias = true
        //抖动设置
        mPaint.isDither = true

        //用这个类获得自定义的属性
        val typedArray:TypedArray = context.obtainStyledAttributes(R.styleable.RotateCircleImageView)
        //画笔颜色
        mPaint.color = typedArray.getColor(R.styleable.RotateCircleImageView_circle_back_color,
            Color.BLACK)
        //ImageView图片
        tempImage = BitmapFactory.decodeResource(
            resources, typedArray.getResourceId(
            R.styleable.RotateCircleImageView_image, R.mipmap.ic_launcher))
        //黑色边框
        blackWidth = typedArray.getDimensionPixelOffset(R.styleable.
        RotateCircleImageView_circle_back_width,
            dpToPx(context, 100f))
        //选择角度
        rotateAngle = typedArray.getFloat(R.styleable.RotateCircleImageView_rotate_sd, 0.8f)
        rotateX = rotateAngle
        //选择方向
        rotateDirection = typedArray.getInt(R.styleable.RotateCircleImageView_rotate_fx, 0)
        //选择设置
        isRotate = typedArray.getBoolean(R.styleable.RotateCircleImageView_isRotate, true)
        typedArray.recycle()
    }
    fun startRotate() { //开始旋转
        if (!isRotate) {

            isRotate = true
            invalidate()
        }
    }

    fun stopRotate() { //暂停旋转
        val valueAnimator = ValueAnimator.ofFloat(rotateAngle, 0F)
        valueAnimator.duration = 1000
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.addUpdateListener {
            rotateAngle = it.animatedValue as Float
            invalidate()
        }
        valueAnimator.start()
        handler.postDelayed({
            rotateAngle = rotateX
            isRotate = false
        }, 1000)

    }
}


