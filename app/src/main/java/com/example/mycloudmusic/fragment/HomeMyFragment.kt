package com.example.mycloudmusic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.mycloudmusic.MyViewModel
import com.example.mycloudmusic.R
import com.example.mycloudmusic.base.BaseFragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

import com.google.android.material.tabs.TabLayout

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener

import com.google.android.material.appbar.CollapsingToolbarLayout


/**
 * 个人主界面
 */
class HomeMyFragment : BaseFragment() {
    private lateinit var userModel : MyViewModel
    private lateinit var mUserNickname : TextView
    private lateinit var mUserNote : TextView
    private lateinit var mIvBackground: ImageView
    private lateinit var mCollapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var mAppBarLayout: AppBarLayout
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager2: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_homemy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView(){
        userModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        mIvBackground = requireView().findViewById(R.id.iv_home_background)
        mCollapsingToolbarLayout = requireView().findViewById(R.id.toolbar_home_top)
        mAppBarLayout = requireView().findViewById(R.id.appbar_home_top)
        mTabLayout = requireView().findViewById(R.id.tly_home_middle)
        mViewPager2 = requireView().findViewById(R.id.tly_home_bottom)
        mUserNote = requireView().findViewById(R.id.tv_home_note)
        mUserNickname = requireView().findViewById(R.id.tv_home_nickname)
    }

    /**
     * 初始化用户
     */
    private fun initUser(){
        val follows = userModel.user.profile.follows
        val followeds = userModel.user.profile.followeds
        mUserNickname.text = userModel.user.profile.nickname
        mUserNote.text =" ${follows}关注 | ${followeds}粉丝"
    }

    /**
     * 设置监听
     */
    private fun setListener(){
        mAppBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) { //收缩时
                    mCollapsingToolbarLayout.title = userModel.user.profile.nickname
                    isShow = true
                } else if (isShow) { //展开时
                    mCollapsingToolbarLayout.title = ""
                    isShow = false
                }
            }
        })

    }

}

