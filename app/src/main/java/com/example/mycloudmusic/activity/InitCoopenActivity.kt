package com.example.mycloudmusic.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.LAYER_TYPE_SOFTWARE
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mycloudmusic.R
import com.example.mycloudmusic.base.BaseActivity
import com.example.mycloudmusic.view.InitCoopenView
import java.util.*
import kotlin.concurrent.timerTask


class InitCoopenActivity: BaseActivity() {
    private lateinit var mRelativeLayout: RelativeLayout
    private lateinit var mInitCoopenView: InitCoopenView
    private lateinit var mImageView: ImageView
    private lateinit var translationAnimator: ObjectAnimator
    private lateinit var alphaAnimator:ObjectAnimator
    private lateinit var mTvCoopen:TextView
    private lateinit var mRunnable: Runnable
    private val mHandler = Handler(Looper.myLooper()!!)
    private var mHeight = 0f
    private var recLen = 5
    private var beClicked = false

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coopen)
        initView()
        mRelativeLayout.post { //获得底部的高度
        mHeight = mRelativeLayout.height.toFloat()
//      开始动画(开屏动画只会启动一次，不会重复创建对象)
        translationAnimator= ObjectAnimator.ofFloat(mRelativeLayout,"translationY",mHeight,0f)
        alphaAnimator= ObjectAnimator.ofFloat(mRelativeLayout,"alpha",0f,1f)
        startAnimation()
//            延时加载图片
            Handler(Looper.getMainLooper()).postDelayed({
                Glide.with(this@InitCoopenActivity)
                    .load(R.drawable.ic_coopen_main2).centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mImageView)
            }, 2000)
        }
        //启动右上角倒计时
        Handler(Looper.myLooper()!!).postDelayed(
            {
                topTextView()
            }
            ,3000
        )
    }

    /**
     * 启动界面时间跳转
     */
    private fun topTextView(){
        val mIntent = Intent(this@InitCoopenActivity, MainActivity::class.java)

        mRunnable = Runnable {
            if( ! beClicked){
                startActivity(mIntent)
                finish()
            }
        }

        mHandler.postDelayed(
            mRunnable
            ,
            5000)


        val task: TimerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    recLen--
                    mTvCoopen.text = "跳过 $recLen"
                    if (recLen < 0) {
                        mTvCoopen.visibility = View.GONE //倒计时到0隐藏字体
                    }
                }
            }
        }
        Timer().schedule(task,1000,1000)
    }

    private fun startAnimation(){
        translationAnimator.duration = 1000L
        alphaAnimator.duration = 2500
        translationAnimator.doOnEnd {
//            关闭硬件加速
//            位移动画结束时开启下一个动画
            mInitCoopenView.setLayerType(LAYER_TYPE_SOFTWARE, null)
            mInitCoopenView.startAnimation()
        }
        val set = AnimatorSet()
        set.play(translationAnimator).with(alphaAnimator)
        set.start()
    }

    private fun initView(){
        val mIntent = Intent(this@InitCoopenActivity, MainActivity::class.java)
        mRelativeLayout = findViewById(R.id.rv_coopen_bottom)
        mInitCoopenView = findViewById(R.id.icv_coopen_bottom)
        mImageView = findViewById(R.id.image_coopen_bottom)
        mTvCoopen = findViewById(R.id.textview_coopen_top)
        mTvCoopen.setOnClickListener {
            startActivity(mIntent)
            finish()
            beClicked = true
        }
    }

}