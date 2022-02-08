package com.example.mycloudmusic.activity

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mycloudmusic.R
import com.example.mycloudmusic.`interface`.RecyclerItemClickListener
import com.example.mycloudmusic.adapter.SongListAdapter
import com.example.mycloudmusic.base.BaseActivity
import com.example.mycloudmusic.item.SongItem
import com.example.mycloudmusic.userdata.ListDetail
import com.example.mycloudmusic.userdata.Playlist
import com.example.mycloudmusic.userdata.UserPlaylist
import com.example.mycloudmusic.util.LoggingInterceptor
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 点击开启的歌单类
 */
class PlayerListActivity : BaseActivity(),RecyclerItemClickListener{

    private var mPosition:Int = -1
    private lateinit var mBundle: Bundle
    private lateinit var mPlayerList:UserPlaylist
    private lateinit var mCurrentUsr:Playlist
    private lateinit var mIvList: ImageView
    private lateinit var mTvListName : TextView
    private lateinit var mIvUsr : ImageView
    private lateinit var mTvUsrName : TextView
    private lateinit var mTvSongNum : TextView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mListDetail : ListDetail
    private lateinit var cookieList : List<String>
    private lateinit var mSongItems : List<SongItem>
    private lateinit var mCookie : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playerlist)
        initData()
        initView()
        initTopUi()
        initListDetail()

    }

    private fun initSongItems(){
        val tempList = mutableListOf<SongItem>()
        for(i in mListDetail.SongList.tracks.indices){
            val mCurrentItem = mListDetail.SongList.tracks[i]
            //歌手名字
            var arName = ""
            for(n in mCurrentItem.ar.indices){
                if(n != mCurrentItem.ar.size-1){
                    arName += mCurrentItem.ar[n].name+"/"
                }else{
                    arName += mCurrentItem.ar[n].name
                }
            }
            val tempItem = SongItem(mCurrentItem.name,arName+" - "+mCurrentItem.al.name,(i+1).toString())
            tempList.add(tempItem)
        }
        mSongItems = tempList
    }

    private fun initView(){
        mIvList = findViewById(R.id.iv_playerList_user)
        mTvListName = findViewById(R.id.tv_playerList_listName)
        mIvUsr = findViewById(R.id.siv_playerList_user)
        mTvUsrName = findViewById(R.id.tv_playerList_user)
        mTvSongNum= findViewById(R.id.tv_playerList_songNum)
        mRecyclerView = findViewById(R.id.rv_playerList_song)
    }

    override fun onRecyclerViewItemClick(view: View, position: Int) {
        //Item的点击事件
        //开启歌单的界面
        val intent = Intent(this,SongActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("mListDetail",mListDetail)
        intent.putExtras(bundle)
        intent.putExtra("position",position)
        intent.putExtra("cookie",mCookie)
        startActivity(intent)
    }

    /**
     * 设置RecycleView的Adapter
     */
    private fun setAdapter(){
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = SongListAdapter(this, mSongItems)
    }

    /**
     * 网络请求获得ListDetail
     * 并却换到主线程更新UI操作
     */
    private fun initListDetail(){
        val client = OkHttpClient.Builder()
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .writeTimeout(20000, TimeUnit.MILLISECONDS)
            .addInterceptor(LoggingInterceptor())
            .build()

        val requestBody = FormBody.Builder()
            .add("id",mCurrentUsr.id)
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/playlist/detail")
            .apply {
                val length = cookieList.count()
                for(i in 0..length-5 step 1){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
            }
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@PlayerListActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                val mGson = Gson()
                mListDetail  = mGson.fromJson(userData,ListDetail::class.java)
                Log.d("mListDetail",mListDetail.toString())
                Log.d("vvv",mListDetail.toString())
                //切换主线程更新UI
                runOnUiThread {
                    run() {
                        initSongItems()
                        setAdapter()
                    }
                }
            }
        })
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
        mCookie = intent.getStringExtra("cookie").toString()
        cookieList = mCookie.split(";")
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