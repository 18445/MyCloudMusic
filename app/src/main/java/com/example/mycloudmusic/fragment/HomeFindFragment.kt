package com.example.mycloudmusic.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.mycloudmusic.R
import com.example.mycloudmusic.adapter.BannerAdapter
import com.example.mycloudmusic.base.BaseFragment




class HomeFindFragment : BaseFragment() {


    private lateinit var mImages : List<Drawable>
    private lateinit var mViewPager2: ViewPager2
    private lateinit var mLinearLayout: LinearLayout
    private var lastPosition:Int=0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_homefind, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initIndicator()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initView(){
        mImages = listOf(
            resources.getDrawable(R.drawable.banner1, null),
            resources.getDrawable(R.drawable.banner2, null),
            resources.getDrawable(R.drawable.banner3, null),
            resources.getDrawable(R.drawable.banner4, null),
            resources.getDrawable(R.drawable.banner5, null),
        )

        mLinearLayout = requireView().findViewById(R.id.container_indicator)
        mViewPager2 = requireView().findViewById(R.id.vp2_find_banner)
        mViewPager2.adapter = BannerAdapter(mImages)
        mViewPager2.currentItem = 500
        lastPosition = 500
        mViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val current = position % 5
                val last = lastPosition % 5
//                mLinearLayout.getChildAt(current).isSelected = true
                mLinearLayout.getChildAt(current).background = resources.getDrawable(R.drawable.indicator_selected,null)
//                mLinearLayout.getChildAt(last).isSelected = false
                mLinearLayout.getChildAt(last).background = resources.getDrawable(R.drawable.indicator_normal,null)
                lastPosition = position
            }
        })

    }

    /**
     * 动态添加下方Indicator
     */
    private lateinit var mView : View
    private lateinit var params : LinearLayout.LayoutParams
    private fun initIndicator(){
        for(i in mImages.indices){
            mView = View(activity)
            params = LinearLayout.LayoutParams(30, 15)
            params.leftMargin = 10
//            mView.setBackgroundResource(R.drawable.indicator_background)
            mView.setBackgroundResource(R.drawable.indicator_normal)
            mView.layoutParams = params
            mView.isEnabled = false
            mLinearLayout.addView(mView)
        }
        mLinearLayout.gravity = Gravity.CENTER
    }

    val mHandler = Handler(Looper.myLooper()!!)
    override fun onResume() {
        super.onResume()
        mHandler.postDelayed(runnable, 5000)
    }


    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(runnable)
    }

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            //获得轮播图当前的位置
            var currentPosition: Int = mViewPager2.getCurrentItem()
            currentPosition++
            mViewPager2.setCurrentItem(currentPosition, true)
            mHandler.postDelayed(this, 5000)
        }
    }
}