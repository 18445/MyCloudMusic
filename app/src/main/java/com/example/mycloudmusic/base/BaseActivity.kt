package com.example.mycloudmusic.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.util.DisplayMetrics

import android.content.pm.ActivityInfo

import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.core.view.ViewCompat.getDisplay


open class BaseActivity: AppCompatActivity() {
    private val TAG = BaseActivity::class.java.simpleName

    var screenWidth //屏幕宽度
            = 0
    var screenHeight //屏幕高度
            = 0
    var screenDendity //屏幕密度比例
            = 0
    var screenDendityDpi //屏幕密度
            = 0

    override fun onStart() {
        super.onStart()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        // 去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide();

        //保持屏幕为横屏或者竖屏，禁止旋转
        fun setOrientationPortrait(){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //竖屏
        }
        fun setOrientationLandscape(){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;//横屏
        }

        //将Activity 中的Window 的背景图设置为空,解决Android Activity切换时出现白屏问题
        window.setBackgroundDrawable(null)

        //打印当前活动的activity
        Log.d(TAG, javaClass.simpleName) // 知晓当前是在哪一个活动

        //3、获取屏幕宽和高
        val displayMetrics = resources.displayMetrics
        screenWidth = displayMetrics.widthPixels // 屏幕宽度（像素）
        screenHeight = displayMetrics.heightPixels //屏幕高度（像素）
        screenDendity = displayMetrics.density.toInt() //屏幕密度比例
        screenDendityDpi = displayMetrics.densityDpi //屏幕密度
        super.onCreate(savedInstanceState)
    }
}