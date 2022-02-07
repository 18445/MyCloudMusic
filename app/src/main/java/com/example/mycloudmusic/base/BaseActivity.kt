package com.example.mycloudmusic.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

import android.content.pm.ActivityInfo

import android.os.Bundle
import android.util.Log
import android.content.Intent

import android.content.BroadcastReceiver
import android.content.IntentFilter





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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        supportActionBar?.hide();

        //将Activity 中的Window 的背景图设置为空,解决Android Activity切换时出现白屏问题
//        window.setBackgroundDrawable(null)

        /**
         * 是否允许屏幕旋转
         *
         */
        fun setAllowScreenRoate(allowScreenRoate:Boolean) {
            if (allowScreenRoate) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }

        //打印当前活动的activity
        Log.d(TAG, javaClass.simpleName) // 知晓当前是在哪一个活动

        //获取屏幕宽和高
        val displayMetrics = resources.displayMetrics
        screenWidth = displayMetrics.widthPixels // 屏幕宽度（像素）
        screenHeight = displayMetrics.heightPixels //屏幕高度（像素）
        screenDendity = displayMetrics.density.toInt() //屏幕密度比例
        screenDendityDpi = displayMetrics.densityDpi //屏幕密度
    }



}