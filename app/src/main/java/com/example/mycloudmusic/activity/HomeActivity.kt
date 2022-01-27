package com.example.mycloudmusic.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.mycloudmusic.MyViewModel
import com.example.mycloudmusic.R
import com.example.mycloudmusic.adapter.ViewPagerAdapter
import com.example.mycloudmusic.base.BaseActivity
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.data.User
import com.example.mycloudmusic.fragment.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator



lateinit var MYUSER: User
class HomeActivity : BaseActivity() {
    private val titles : List<String> = listOf("发现","播客","我的","关注","云村")
    private val titleIcons: List<Int> = listOf(R.drawable.ic_home_find,R.drawable.ic_home_podcast,R.drawable.ic_home_my,R.drawable.ic_home_follow,R.drawable.ic_home_village)
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager2: ViewPager2
    private lateinit var user:User
    private val mFragments : List<BaseFragment> = listOf(
        HomeFindFragment(),
        HomePodcastFragment(),
        HomeMyFragment(),
        HomeFollowFragment(),
        HomeVillageFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initUser(MYUSER)
        initView()
        initPage()
    }


    private fun initPage(){
        mViewPager2.adapter = ViewPagerAdapter(supportFragmentManager,lifecycle,mFragments)
        TabLayoutMediator(mTabLayout,mViewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = titles[0]
                1 -> tab.text = titles[1]
                2 -> tab.text = titles[2]
                3 -> tab.text = titles[3]
                4 -> tab.text = titles[4]
            }
            when (position) {
                0 -> tab.setIcon(titleIcons[0])
                1 -> tab.setIcon(titleIcons[1])
                2 -> tab.setIcon(titleIcons[2])
                3 -> tab.setIcon(titleIcons[3])
                4 -> tab.setIcon(titleIcons[4])
            }
        }.attach()
    }

    private fun initView(){
        mTabLayout = findViewById(R.id.tly_home_bottom)
        mViewPager2 = findViewById(R.id.vp2_home_top)
    }

    fun initUser(mUser:User){
        user = mUser
        val userModel = ViewModelProvider(this@HomeActivity).get(MyViewModel::class.java)
        userModel.initUser(user)
    }
}