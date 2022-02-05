package com.example.mycloudmusic.activity

import android.os.Bundle
import android.util.Log
import com.example.mycloudmusic.R
import com.example.mycloudmusic.base.BaseActivity
import com.example.mycloudmusic.userdata.UserPlaylist

/**
 * 点击开启的歌单类
 */
class PlayerListActivity : BaseActivity(){

    private lateinit var mBundle: Bundle
    private lateinit var mPlayerList:UserPlaylist
    private var mPosition:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playerlist)
        initData()
        Log.d("mPlayerList",mPlayerList.toString())
        Log.d("mPosition",mPosition.toString())
    }

    /**
     * 初始化数据
     */
    private fun initData(){
        mBundle = intent.extras!!
        mPlayerList = mBundle.getParcelable<UserPlaylist>("UserPlayerList") as UserPlaylist
        mPosition = intent.getIntExtra("position",-1)
    }
}