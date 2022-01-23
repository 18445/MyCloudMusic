package com.example.mycloudmusic.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.CheckBox
import android.widget.TextView
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
        mMaterialButton.setOnClickListener{
            val loginIntent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    /**
     * SpannableString对textview的初始化
     * 完成对TextView的点击等相关操作
     */
    private fun initSpannableString(){
        val spannableString = SpannableStringBuilder()
        spannableString.append("同意 服务条款、隐私条款、儿童隐私政策")
        val colorSpanGray = ForegroundColorSpan(Color.BLACK)
        val colorSpanWhite = ForegroundColorSpan(Color.WHITE)
        spannableString.setSpan(colorSpanGray,0,2,Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        spannableString.setSpan(colorSpanWhite,2,19,Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
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