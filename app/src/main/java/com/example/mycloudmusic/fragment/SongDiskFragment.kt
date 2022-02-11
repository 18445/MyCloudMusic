package com.example.mycloudmusic.fragment

import android.animation.ObjectAnimator
import android.content.ContentValues
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.mycloudmusic.R
import com.example.mycloudmusic.activity.SongActivity
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.userdata.*
import com.example.mycloudmusic.util.LoggingInterceptor
import com.example.mycloudmusic.view.RotateCircleImageView
import com.example.mycloudmusic.viewmodel.SongViewModel
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
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
    private lateinit var mRDisk : RotateCircleImageView
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
        Handler(Looper.myLooper()!!).postDelayed(
            {
//                val mPair = Pair(mPosition.toString(),mCurrentUserSong)
                mSongModel.userSongs[mPosition.toString()] = mCurrentUserSong
                Log.d("MyOneSong",mCurrentUserSong.toString())
                Log.d("model map","has plus ${mSongModel.userSongs.toString()}}")}
        ,2000
        )
    }

    private fun initView(){
        mDisk = requireView().findViewById(R.id.iv_song_disk)
        mRDisk = requireView().findViewById(R.id.rciv_song_disk)
    }

    /**
     * 更新Ui的操作
     */
    private fun updateUi(){
        mSong?.al?.picUrl.let {
            if (it != null) {
                loadImage(it)
            }
        }
    }


    //自定义Glide的target
    private val customTarget: CustomTarget<Drawable?> = object : CustomTarget<Drawable?>() {
        override fun onResourceReady(
            resource: Drawable,
            transition: Transition<in Drawable?>?
        ) {

            mRDisk.tempImage = resource.toBitmap(500,500)
//            mRDisk.image = resource.toBitmap(250,250)
            mRDisk.requestLayout()
            mRDisk.invalidate()
            mSongModel.currentDisk = SoftReference(mRDisk)
            sendAnimation()
            mRDisk.stopRotate()
            Handler(Looper.myLooper()!!).postDelayed({
                mRDisk.startRotate()
            },3500)
            Log.d("weak reference",mSongModel.currentDisk.toString())
        }

        override fun onLoadCleared(placeholder: Drawable?) {
        }
    }

    /**
     * 加载图片
     */
    private fun loadImage(url:String){
        Glide.with(requireActivity())
            .load(url)
            .into(customTarget)
        Log.d("Glide Url",url)
    }

    /**
     *初始化歌曲
     */
    private fun initOneSong(){
        initSongUrl(id)
        initSongLyric(id)
        initSongDetail(id)
//        mCurrentUserSong.lyric = mSongLyric
//        mCurrentUserSong.song = mSong
//        mCurrentUserSong.songUrl = mSongUrl

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
                mCurrentUserSong.song = mSong
//                Log.d("SongDetail",mSongDetail.toString())
//                Log.d("MyOneSong",mCurrentUserSong.toString())
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
                mCurrentUserSong.lyric = mSongLyric
//                Log.d("MyOneSong",mCurrentUserSong.toString())
//                Log.d("temp song item",mSongLyric.toString())
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
                mCurrentUserSong.songUrl = mSongUrl
//                Log.d("MyOneSong",mCurrentUserSong.toString())
//                Log.d("SongUrl",mSongUrl.toString())
            }
        })
    }


    private fun startAnimation(){
        mRDisk.startRotate()
    }
    private fun stopAnimation(){
        mRDisk.stopRotate()
    }

    fun sendAnimation(){
        val mActivity = activity as SongActivity
        mActivity.getRotateDisk({
            startAnimation()
        },{
            stopAnimation()
        })
    }
}