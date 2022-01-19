package com.example.mycloudmusic.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mycloudmusic.R
import com.example.mycloudmusic.base.BaseActivity
import com.example.mycloudmusic.view.InitCoopenView





class InitCoopenActivity: BaseActivity() {
    lateinit var mRelativeLayout: RelativeLayout
    lateinit var mInitCoopenView: InitCoopenView
    lateinit var mImageView: ImageView
    private var mHeight = 0f
    lateinit var translationAnimator: ObjectAnimator
    lateinit var alphaAnimator:ObjectAnimator

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
                    .load(R.drawable.ic_coopen_main).centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mImageView)
            }, 2000)
        }
    }

    private fun startAnimation(){
        translationAnimator.duration = 1000L
        alphaAnimator.duration = 2500
        translationAnimator.doOnEnd {
//            位移动画结束时开启下一个动画
            mInitCoopenView.startAnimation()
        }
        val set = AnimatorSet()
        set.play(translationAnimator).with(alphaAnimator)
        set.start()
    }
    private fun initView(){
        mRelativeLayout = findViewById(R.id.rv_coopen_bottom)
        mInitCoopenView = findViewById(R.id.icv_coopen_bottom)
        mImageView = findViewById(R.id.image_coopen_bottom)
    }

}