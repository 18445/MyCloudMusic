package com.example.mycloudmusic.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mycloudmusic.R
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.userdata.*
import com.example.mycloudmusic.util.LoggingInterceptor
import com.example.mycloudmusic.viewmodel.SongViewModel
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 歌曲界面转动的磁盘类
 */
class SongDiskFragment(private val mPosition:Int,private val id:String) : BaseFragment() {

    private val mCurrentUserSong : OneSong = OneSong(null,null,null)
    private var mSongUrl : SongUrl? = null
    private var mSongLyric : Lyric? = null
    private var mSong : Song? = null
    private lateinit var mSongModel : SongViewModel
    private lateinit var cookieList : List<String>
    private lateinit var mDisk : ImageView
    private val client = OkHttpClient.Builder()
        .readTimeout(10000, TimeUnit.MILLISECONDS)
        .writeTimeout(20000, TimeUnit.MILLISECONDS)
        .addInterceptor(LoggingInterceptor())
        .build()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_disk, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSongModel = ViewModelProvider(requireActivity()).get(SongViewModel::class.java)
        cookieList = mSongModel.cookieList
        initOneSong()
        initView()

    }

    private fun initView(){
        mDisk = requireView().findViewById(R.id.iv_song_disk)

    }

    /**
     * 更新Ui的操作
     */
    private fun updateUi(){
//        mCurrentUserSong.song?.al?.picUrl.let {
        mSong?.al?.picUrl.let {
            if (it != null) {
                loadImage(it)
            }
        }
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
     *初始化歌曲
     */
    private fun initOneSong(){
//        initSongUrl(id)
//        initSongLyric(id)
        initSongDetail(id)
//        mCurrentUserSong.lyric = mSongLyric
//        mCurrentUserSong.song = mSong
//        mCurrentUserSong.songUrl = mSongUrl
//        Log.d("MyOneSong",mCurrentUserSong.toString())
    }

    private fun initSongDetail(id:String) {
        val requestBody = FormBody.Builder()
            .add("ids",id)
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
                Toast.makeText(requireActivity(), "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                val mGson = Gson()
                val mSongDetail  = mGson.fromJson(userData, SongDetail::class.java)
                mSong = mSongDetail.songs[0]
                Log.d("SongDetail",mSongDetail.toString())
                requireActivity().runOnUiThread {
                    updateUi()
                }
            }
        })
    }

    private fun initSongLyric(id:String) {
        val requestBody = FormBody.Builder()
            .add("id",id)
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/lyric")
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
                Toast.makeText(requireActivity(), "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                val mGson = Gson()
                mSongLyric  = mGson.fromJson(userData, Lyric::class.java)
                Log.d("temp song item",mSongLyric.toString())
            }
        })

    }

    private fun initSongUrl(id:String) {
        val requestBody = FormBody.Builder()
            .add("id",id)
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
                Toast.makeText(requireActivity(), "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                val mGson = Gson()
                mSongUrl  = mGson.fromJson(userData, SongUrl::class.java)
                Log.d("SongUrl",mSongUrl.toString())
            }
        })
    }


    /**
     * 设置图片动画
     */
    private fun setAnimation(){

    }



}