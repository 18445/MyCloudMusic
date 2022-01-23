package com.example.mycloudmusic.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.text.set
import com.example.mycloudmusic.R
import com.example.mycloudmusic.base.BaseActivity
import com.google.android.material.button.MaterialButton
import org.w3c.dom.Text

class MainActivity : BaseActivity() {
    private lateinit var mMaterialButton: MaterialButton
    private lateinit var mTvStartTop:TextView
    private lateinit var mTvStartBottom:TextView
    private lateinit var mCbStartBottom : CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initSpannableString()
        initClick()
    }

    /**
     * 点击操作初始化
     */
    private fun initClick(){
        //按钮点击效果
        mMaterialButton.setOnClickListener{
            if (!mCbStartBottom.isChecked){
                //设置Toast的显示位置
                val mToast = Toast.makeText(this@MainActivity,"请先勾选同意！",Toast.LENGTH_SHORT)
                mToast.setGravity(Gravity.CENTER,0,0)
                mToast.show()
                //文字抖动动画
                shake()
            }else{
                val loginIntent = Intent(this@MainActivity,LoginActivity::class.java)
                startActivity(loginIntent)
            }
        }

        mTvStartTop.setOnClickListener {
            val mToast = Toast.makeText(this@MainActivity,"手机号登录自动注册",Toast.LENGTH_SHORT)
            mToast.setGravity(Gravity.CENTER,0,0)
            mToast.show()
        }


    }

    /**
     * 底部文字的移动动画
     */
    private fun shake(){
        val shake = AnimationUtils.loadAnimation(this@MainActivity,R.anim.shake)
        mTvStartBottom.startAnimation(shake)
    }
    /**
     * SpannableString对textview的初始化
     * 完成对TextView的点击等相关操作
     */
    private fun initSpannableString(){
        val spannableString = SpannableStringBuilder()
        spannableString.append("同意 服务条款、隐私条款、儿童隐私政策")
        //文字颜色效果
        val colorSpanGray = ForegroundColorSpan(Color.BLACK)
        val colorSpanWhite = ForegroundColorSpan(Color.WHITE)
        spannableString.setSpan(colorSpanGray,0,2,Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        spannableString.setSpan(colorSpanWhite,2,19,Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        //网页跳转效果
        val urlSpan = URLSpan("https://st.music.163.com/official-terms/service")
        spannableString.setSpan(urlSpan,2,19,Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        mTvStartBottom.movementMethod = LinkMovementMethod.getInstance();
        mTvStartBottom.text = spannableString
    }

    /**
     * View的初始化
     */
    private fun initView(){
        mMaterialButton = findViewById(R.id.mbtn_start_bottom)
        mTvStartBottom = findViewById(R.id.tv_start_bottom)
        mTvStartTop = findViewById(R.id.tv_start_top)
        mCbStartBottom = findViewById(R.id.cb_start_bottom)
    }
}