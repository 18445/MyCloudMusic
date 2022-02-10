package com.example.mycloudmusic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.mycloudmusic.R
import com.example.mycloudmusic.base.BaseFragment

/**
 * 歌曲界面转动的磁盘类
 */
class SongDiskFragment(private val mPosition:Int) : BaseFragment() {

    private lateinit var mDisk : ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_disk, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
//        loadImage()
    }

    private fun initView(){
        mDisk = requireView().findViewById(R.id.iv_song_disk)

    }

    /**
     * 加载图片
     */
    private fun loadImage(url:String){
        Glide.with(requireActivity())
            .load(url)
            .centerCrop()
            .into(mDisk)

    }

    /**
     * 设置图片动画
     */
    private fun setAnimation(){

    }



}