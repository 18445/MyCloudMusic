package com.example.mycloudmusic.activity

import GlideBlurTransformation
import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color.alpha
import android.graphics.drawable.Drawable
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.example.mycloudmusic.userdata.SongDetail
import com.example.mycloudmusic.util.Player
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.mycloudmusic.adapter.FragmentPagerAdapter
import jp.wasabeef.glide.transformations.BlurTransformation


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
    private lateinit var mTvTitle : TextView
    private lateinit var mTvArtist : TextView
    private lateinit var mViewPager2: ViewPager2

    private lateinit var mView : ConstraintLayout
    private lateinit var cookieList : List<String>
    private lateinit var mListDetail : ListDetail
    private lateinit var mSongDetail: SongDetail
    private lateinit var mSongUrl: SongUrl
    private lateinit var mPlayer : Player
    private lateinit var mCookie : String
    private lateinit var mBundle: Bundle
    private lateinit var mTrack:Track
    private var mPosition = -1
    private var isPlay = false
    private var mTime = 0

    //获得边界
    private lateinit var mBound : android.graphics.Rect
    lateinit var mDrawableNormal: Drawable
    lateinit var mDrawablePress: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)
        initView()
        setClick()
        initData()
        getSongDetail()
        getSongUrl()
        initSeekBar()
        initPage()
    }

    override fun onClick(v: View?) {
        when (v) {
            //播放按钮
            mIvPlay -> {
                isPlay = if(isPlay){
                    //暂停
                    mPlayer.pause()
                    mIvPlay.setImageResource(R.drawable.ic_song_play)
                    false
                }else{
                    //播放
                    mPlayer.play()
                    mIvPlay.setImageResource(R.drawable.ic_song_pause)
                    Log.d("music player","is playing")
                    true
                }
            }
            //ViewPager2切换
            mViewPager2->{
                Log.d("ViewPager2:","be clicked")
                val currentItem =
                    when(mViewPager2.currentItem){
                        0 -> 1
                        1 -> 0
                        else -> 0
                    }
                //取消动画效果
                mViewPager2.setCurrentItem(currentItem,false)
            }

        }


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initView(){
        mIvPlay = findViewById(R.id.iv_song_play)
        mIvPlayLast = findViewById(R.id.iv_song_playLast)
        mIvPlayNext = findViewById(R.id.iv_song_playNext)
        mTvTimeLeft = findViewById(R.id.tv_songTime_left)
        mTvTimeRight = findViewById(R.id.tv_songTime_right)
        mSeekBar = findViewById(R.id.sb_song_below)
        mTvTitle = findViewById(R.id.tv_song_titleName)
        mTvArtist = findViewById(R.id.tv_song_artistName)
        mView = findViewById(R.id.Layout_song_view)
        mViewPager2 = findViewById(R.id.vp2_song_music)
        mPlayer =  Player(mSeekBar)

        mBound = mSeekBar.thumb.bounds
        mDrawablePress = resources.getDrawable(R.drawable.ic_seekbar_thumb_pressed,null)
        mDrawableNormal = resources.getDrawable(R.drawable.ic_seekbar_thumb_normal,null)
        mDrawableNormal.bounds = mBound
        mDrawablePress.bounds = mBound

    }

    /**
     * 设置点击操作
     */
    private fun setClick(){
        mIvPlay.setOnClickListener(this)
        mViewPager2.setOnClickListener(this)
    }

    /**
     *完成歌曲界面的UI设置
     */
    private fun initPageUi(){
        val songName = mListDetail.SongList.tracks[mPosition].name
        var songArtist = ""
        for(n in mListDetail.SongList.tracks[mPosition].ar.indices){
            songArtist += if(n != mListDetail.SongList.tracks[mPosition].ar.size-1){
                mListDetail.SongList.tracks[mPosition].ar[n].name+"/"
            }else{
                mListDetail.SongList.tracks[mPosition].ar[n].name
            }
        }
        //顶部TextView部分
        mTvTitle.text = songName
        mTvArtist.text = songArtist
        Log.d("mSongDetail",mSongDetail.toString())
        val url = mSongDetail.songs[0].al.picUrl
        Log.d("picUrl",url)

        //自定义Glide的target
        val customTarget: CustomTarget<Drawable?> = object : CustomTarget<Drawable?>(1000,1000) {
            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable?>?
            ) {
                mView.background = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        }

        //加载背景图
        Glide.with(this)
            .load(url)
            .transform(BlurTransformation(25,12))
            .into(customTarget)
    }

    /**
     * 初始化ViewPager2的设置
     */
    private fun initPage(){
        mViewPager2.adapter = FragmentPagerAdapter(this)
        mViewPager2.offscreenPageLimit = 2
        mViewPager2.isUserInputEnabled = false
        mViewPager2.setCurrentItem(1,false)

    }

    /**
     *初始化进度条的操作
     * 设置SeekBar的点击事件
     */
    private fun initSeekBar(){
        mSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            var mSeekTime  = -1
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {//改变进度条
                if(mTime != 0 ){
                    val percent : Double = (progress.toDouble()/100000)//百分比
                    Log.d("bar change",percent.toString())
                    val currentTime = mTime * percent//到达进度条的时间（ms）
                    mSeekTime = currentTime.toInt()
                    Log.d("bar change",currentTime.toString())
                    val leftTime = setTime(currentTime.toInt())
                    mTvTimeLeft.text = leftTime
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {//触碰进度条
                if(mTime != 0){
                    mSeekBar.thumb = mDrawablePress
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {//松开进度条
                if(mTime != 0){
                    mSeekBar.thumb = mDrawableNormal
                }
                if(mSeekTime != -1){
                    mPlayer.mediaPlayer?.seekTo(mSeekTime);//歌曲到达的时间
                }
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
        val sec : String = when(secTemp){
            in 100..Int.MAX_VALUE -> {secTemp.toString().subSequence(0,2).toString()}
            in 10..99 -> {secTemp.toString()}
            in 0..10 -> {"0${secTemp}"}
            else -> {"00:00"}
        }

        Log.d("SongTime", sec)
            "${min}.${sec}".also { return it }
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
                mPlayer.playUrl(mSongUrl.data[0].url)
            }
        })
    }

    /**
     * 获得歌曲详细
     */
    private fun getSongDetail(){
        val client = OkHttpClient.Builder()
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .writeTimeout(20000, TimeUnit.MILLISECONDS)
            .addInterceptor(LoggingInterceptor())
            .build()

        val requestBody = FormBody.Builder()
            .add("ids",mTrack.id)
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/song/detail")
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
                mSongDetail  = mGson.fromJson(userData,SongDetail::class.java)
                Log.d("SongDetail",mSongDetail.toString())
                runOnUiThread {
                    //更新UI
                    initPageUi()
                }
            }
        })
    }

}