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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.google.android.material.tabs.TabLayout

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener

import com.google.android.material.appbar.CollapsingToolbarLayout


/**
 * 个人主界面
 */
class HomeMyFragment : BaseFragment() {
    private lateinit var mIvUser:ImageView
    private lateinit var mIvBackground: ImageView
    private lateinit var userModel : MyViewModel
    private lateinit var mUserNickname : TextView
    private lateinit var mUserNote : TextView
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
        initUser()
        setListener()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView(){
        userModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        mIvBackground = requireView().findViewById(R.id.iv_home_background)
        mIvUser = requireView().findViewById(R.id.iv_home_user)
        mCollapsingToolbarLayout = requireView().findViewById(R.id.toolbar_home_top)
        mAppBarLayout = requireView().findViewById(R.id.appbar_home_top)
        mTabLayout = requireView().findViewById(R.id.tly_home_middle)
        mViewPager2 = requireView().findViewById(R.id.vp2_home_bottom)
        mUserNote = requireView().findViewById(R.id.tv_home_note)
        mUserNickname = requireView().findViewById(R.id.tv_home_nickname)
    }

    /**
     * 初始化用户
     * 将用户的信息显示出来
     */
    private fun initUser(){
        val follows = userModel.getUser().profile.follows
        val followeds = userModel.getUser().profile.followeds
        //头像URL
        val mUrlUser = userModel.getUser().profile.avatarUrl
        //背景URL
        val mUrlBackground = userModel.getUser().profile.backgroundUrl
        //设置基本属性
        mUserNickname.text = userModel.getUser().profile.nickname
        mUserNote.text =" ${follows}关注 | ${followeds}粉丝"
        //设置头像和背景
        setUserIv(mUrlUser,mUrlBackground)
    }

    /**
     * 设置头像
     */
    private fun setUserIv(UserUrl:String,BgUrl:String){
        Glide.with(requireActivity())
            .load(UserUrl)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(mIvUser)

        Glide.with(requireActivity())
            .load(BgUrl)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(mIvBackground)
    }

    /**
     * 设置监听
     * 滑动时状态栏的变化设置
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
                    mCollapsingToolbarLayout.title = userModel.getUser().profile.nickname
                    isShow = true
                } else if (isShow) { //展开时
                    mCollapsingToolbarLayout.title = ""
                    isShow = false
                }

            }
        })

    }

}

