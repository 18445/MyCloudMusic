package com.example.mycloudmusic.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.mycloudmusic.R
import com.example.mycloudmusic.adapter.FragmentPagerAdapter
import com.example.mycloudmusic.base.BaseFragment

/**
 * 在外层Viewpager中显示的fragment
 * 里面镶嵌一个viewpager
 * mPosition:传入页面的位置
 * isVisibility : 背景是否可见
 * isAllowed:判断是否运行滑动，如果是在歌词界面就不允许滑动
 */
class SongInnerFragment(private val mPosition: Int,private val id:String,private val setOnPlayer:(Long)->Unit,private val isVisibility : (Int) -> Unit,private val isAllowedMove :((Int) -> Unit)) : BaseFragment(),View.OnClickListener{
    private lateinit var mVp2Inner: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_songinner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initPage()
        setClick()
    }

    private fun initView() {
        mVp2Inner = requireView().findViewById(R.id.vp2_song_music)
    }

    private fun initPage() {
        mVp2Inner.adapter = FragmentPagerAdapter(requireActivity(),mPosition,id, setOnPlayer )
        mVp2Inner.offscreenPageLimit = 2
        mVp2Inner.isUserInputEnabled = false
        mVp2Inner.setCurrentItem(0,false)
    }


    override fun onClick(v: View?) {
        when (v) {
            //ViewPager2切换
            mVp2Inner->{
                Log.d("ViewPager2:","be clicked")
                val currentItem =
                    // 0 :  光盘界面
                    // 1 ： 歌词界面
                    when(mVp2Inner.currentItem){
                        0 -> {
                            isVisibility(1)
                            1
                        }
                        1 -> {
                            isVisibility(0)
                            0
                        }
                        else -> 0
                    }
                //取消动画效果
                isAllowedMove(currentItem)
                mVp2Inner.setCurrentItem(currentItem,false)
            }
        }
    }

    private fun setClick() {
        mVp2Inner.setOnClickListener(this)
        //长按事件屏蔽单击事件
        mVp2Inner.setOnLongClickListener {
            true
        }
    }
}