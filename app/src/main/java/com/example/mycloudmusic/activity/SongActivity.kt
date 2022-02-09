package com.example.mycloudmusic.activity

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.example.mycloudmusic.R
import com.example.mycloudmusic.base.BaseActivity
import com.example.mycloudmusic.userdata.ListDetail
import com.example.mycloudmusic.userdata.SongUrl
import com.example.mycloudmusic.userdata.Track
import com.example.mycloudmusic.util.LoggingInterceptor
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import android.media.MediaPlayer
import android.view.View
import android.widget.SeekBar.OnSeekBarChangeListener
import com.example.mycloudmusic.util.Player


/**
 * 歌曲界面
 */
class SongActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mIvPlay:ImageView
    private lateinit var mIvPlayLast:ImageView
    private lateinit var mIvPlayNext:ImageView
    private lateinit var mTvTimeLeft :TextView
    private lateinit var mTvTimeRight :TextView
    private lateinit var mSeekBar: SeekBar

    private lateinit var mPlayer : Player
    private lateinit var mCookie : String
    private lateinit var cookieList : List<String>
    private lateinit var mListDetail : ListDetail
    private lateinit var mBundle: Bundle
    private lateinit var mTrack:Track
    private var mPosition = -1
    private lateinit var mSongUrl: SongUrl
    private var mTime = 0

    override fun onClick(v: View?) {
        when (v) {
            mIvPlay -> {mPlayer.playUrl(mSongUrl.data[0].url)
                Log.d("music player","is playing")}
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)
        initView()
        setClick()
        initData()
        getSongUrl()
        initSeekBar()
    }

    /**
     * 设置点击操作
     */
    private fun setClick(){
        mIvPlay.setOnClickListener(this)
        mSeekBar.thumb.transparentRegion
    }

    /**
     *初始化进度条的操作
     */
    private fun initSeekBar(){
        mSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {//改变进度条
                if(mTime != 0 ){
                    val percent : Double = (progress.toDouble()/100000)
                    Log.d("bar change",percent.toString())
                    val currentTime = mTime * percent
                    Log.d("bar change",currentTime.toString())
                    val leftTime = setTime(currentTime.toInt())
                    mTvTimeLeft.text = leftTime
                }


            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {//触碰进度条

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {//松开进度条

            }
        })

    }

    /**
     * 获得音频的长度
     */
    private fun getTime(){
        val mediaPlayer = MediaPlayer()
        Log.d("url",mSongUrl.data[0].url)
        mediaPlayer.setDataSource(mSongUrl.data[0].url)
        mediaPlayer.prepare()

        mTime = mediaPlayer.duration
        Log.d("time",mTime.toString())
    }

    /**
     * 给定的毫秒时间转化为 xx:xx 类型的分钟
     */
    private fun setTime(mTime : Int) : String{
        val minTemp = (mTime/60/1000) //分钟
        val secTemp = (mTime - minTemp*60*1000)
        Log.d("SongTime",minTemp.toString())
        Log.d("SongTime",secTemp.toString())
        var min = "0"
        if (minTemp < 10 ){
            min += minTemp.toString()
        }
//        if (secTemp > 99){
//            val sec = secTemp.toString().subSequence(0,2).toString()
//        }else if (secTemp > 10){
//            val sec = secTemp.toString()
//        }else {
//            val sec = "0${secTemp}"
//        }
        val sec : String = when(secTemp){
            in 100..Int.MAX_VALUE -> {secTemp.toString().subSequence(0,2).toString()}
            in 10..99 -> {secTemp.toString()}
            in 0..10 -> {"0${secTemp}"}
            else -> {"00:00"}
        }

        Log.d("SongTime", sec)
            "${min}.${sec}".also { return it }
    }


    private fun initView(){
        mIvPlay = findViewById(R.id.iv_song_play)
        mIvPlayLast = findViewById(R.id.iv_song_playLast)
        mIvPlayNext = findViewById(R.id.iv_song_playNext)
        mTvTimeLeft = findViewById(R.id.tv_songTime_left)
        mTvTimeRight = findViewById(R.id.tv_songTime_right)
        mSeekBar = findViewById(R.id.sb_song_below)
        mPlayer =  Player(mSeekBar)
    }

    /**
     * 获得activity传过来的数据
     */
    private fun initData(){
        mBundle = intent.extras!!
        mListDetail = mBundle.getParcelable<ListDetail>("mListDetail") as ListDetail
        mCookie = intent.getStringExtra("cookie").toString()
        mPosition = intent.getIntExtra("position",-1)
        cookieList = mCookie.split(";")
        mTrack = mListDetail.SongList.tracks[mPosition]
    }

    /**
     * 获得歌曲的Url
     */
    private fun getSongUrl(){
        val client = OkHttpClient.Builder()
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .writeTimeout(20000, TimeUnit.MILLISECONDS)
            .addInterceptor(LoggingInterceptor())
            .build()

        val requestBody = FormBody.Builder()
            .add("id",mTrack.id)
            .add("br", 320000.toString())
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/song/url")
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
                Toast.makeText(this@SongActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                val mGson = Gson()
                mSongUrl  = mGson.fromJson(userData,SongUrl::class.java)
                Log.d("SongUrl",mSongUrl.toString())
                getTime()
                runOnUiThread {
                    setTime(mTime).also { mTvTimeRight.text = it }
                }

            }
        })
    }

}