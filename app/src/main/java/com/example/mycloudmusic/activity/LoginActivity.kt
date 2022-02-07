package com.example.mycloudmusic.activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.viewpager2.widget.ViewPager2
import com.example.mycloudmusic.R
import com.example.mycloudmusic.adapter.ViewPagerAdapter
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.fragment.LoginEmailFragment
import com.example.mycloudmusic.fragment.LoginPhoneFragment
import com.example.mycloudmusic.fragment.LoginPhonePasswordFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.example.mycloudmusic.base.BaseActivity
import com.example.mycloudmusic.util.FinishActivityManager


class LoginActivity : BaseActivity() {
    private var isLogin = true//判断是否登录
    private lateinit var mViewPager2: ViewPager2
    private lateinit var mTabLayout: TabLayout
    private val mList : List<BaseFragment> = listOf(LoginPhoneFragment(),LoginEmailFragment(),LoginPhonePasswordFragment())
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)//软键盘弹出画面不移动
        super.onCreate(savedInstanceState)
        FinishActivityManager.manager!!.addActivity(this@LoginActivity)
        setContentView(R.layout.activity_login)
        initView()
        initPage()
        initClick()
    }

    /**
     * 初始化点击事件
     */
    private fun initClick(){

    }

    /**
     * 初始化TabLayout与Viewpager
     */
    private fun initView(){
        mViewPager2 = findViewById(R.id.vp2_login_top)
        mTabLayout = findViewById(R.id.tly_login_bottom)
    }

    /**
     * ViewPager和TabLayout的绑定操作
     */

    private fun initPage(){
        mViewPager2.adapter = ViewPagerAdapter(supportFragmentManager,lifecycle,mList)
        TabLayoutMediator(mTabLayout,mViewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "手机登录"
                1 -> tab.text = "邮箱登录"
                2 -> tab.text = "手机密码登录"
            }
        }.attach()
    }

}