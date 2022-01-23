package com.example.mycloudmusic.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Adapter
import android.widget.Switch
import android.widget.TableLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.mycloudmusic.R
import com.example.mycloudmusic.adapter.ViewPagerAdapter
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.fragment.LoginEmailFragment
import com.example.mycloudmusic.fragment.LoginPhoneFragment
import com.example.mycloudmusic.fragment.LoginPhonePasswordFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.annotation.NonNull
import com.example.mycloudmusic.base.BaseActivity
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy


class LoginActivity : BaseActivity() {

    private lateinit var mViewPager2: ViewPager2
    private lateinit var mTabLayout: TabLayout
    private val mList : List<BaseFragment> = listOf(LoginPhoneFragment(),LoginEmailFragment(),LoginPhonePasswordFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)//软键盘弹出画面不移动
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        initPage()
    }

    /**
     * 初始化TabLayout与Viewpager
     */
    private fun initView(){
        mViewPager2 = findViewById(R.id.vp2_login_top)
        mTabLayout = findViewById(R.id.tly2_login_bottom)
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