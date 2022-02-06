package com.example.mycloudmusic.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mycloudmusic.R
import com.example.mycloudmusic.base.BaseActivity
import com.example.mycloudmusic.userdata.Playlist
import com.example.mycloudmusic.userdata.UserPlaylist

/**
 * 点击开启的歌单类
 */
class PlayerListActivity : BaseActivity(){

    private lateinit var mBundle: Bundle
    private lateinit var mPlayerList:UserPlaylist
    private var mPosition:Int = -1
    private lateinit var mCurrentUsr:Playlist
    private lateinit var mIvList: ImageView
    private lateinit var mTvListName : TextView
    private lateinit var mIvUsr : ImageView
    private lateinit var mTvUsrName : TextView
    private lateinit var mTvSongNum : TextView
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playerlist)
        initData()
        initView()
        initTopUi()
    }

    private fun initView(){
        mIvList = findViewById(R.id.iv_playerList_user)
        mTvListName = findViewById(R.id.tv_playerList_listName)
        mIvUsr = findViewById(R.id.siv_playerList_user)
        mTvUsrName = findViewById(R.id.tv_playerList_user)
        mTvSongNum= findViewById(R.id.tv_playerList_songNum)
        mRecyclerView = findViewById(R.id.rv_playerList_song)
    }


    /**
     * 初始化数据
     * 接受上一个activity中传递过来的对象
     * 初始化该activity的对象
     */
    private fun initData(){
        mBundle = intent.extras!!
        mPlayerList = mBundle.getParcelable<UserPlaylist>("UserPlayerList") as UserPlaylist
        mPosition = intent.getIntExtra("position",-1)
        mCurrentUsr = mPlayerList.playlist[mPosition]
    }

    /**
     * 更新歌单界面的顶部UI设置
     */
    private fun initTopUi(){

        val songUrl = mCurrentUsr.coverImgUrl
        val userUrl = mCurrentUsr.creator.avatarUrl
        Log.d("songUrl",songUrl)
        Log.d("userUrl",userUrl)

        //加载歌单图片
        Glide.with(this)
            .load(songUrl)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(mIvList)

        //加载头像
        Glide.with(this)
            .load(userUrl)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(mIvUsr)


        mTvListName.text = mCurrentUsr.name
        "${mCurrentUsr.creator.nickname} >".also { mTvUsrName.text = it }
        "(${mCurrentUsr.trackCount})".also { mTvSongNum.text = it }
    }
}