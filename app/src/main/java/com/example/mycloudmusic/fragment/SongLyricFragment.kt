package com.example.mycloudmusic.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.mycloudmusic.R
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.userdata.OneSong
import com.example.mycloudmusic.userdata.UserSong
import com.example.mycloudmusic.util.LyricUtil
import com.example.mycloudmusic.viewmodel.SongViewModel
import kotlin.math.log

/**
 * 歌曲界面歌词类
 */
class SongLyricFragment (private val mPosition:Int): BaseFragment (){

    private lateinit var currentSong : OneSong
    private lateinit var mSongModel : SongViewModel
    private lateinit var lyric:String
    private lateinit var lyricDetail : List<String>
    private lateinit var lyricTime : List<String>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lyric, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSongModel = ViewModelProvider(requireActivity()).get(SongViewModel::class.java)
        Handler(Looper.myLooper()!!).postDelayed(
            { getLyric()}
            ,3500
        )


    }

    private fun getLyric(){
//        Log.d("LyricFragment"," position :$mPosition")
//        Log.d("LyricFragment",mSongModel.userSongs.toString())
        currentSong = mSongModel.userSongs[mPosition.toString()]!!
        //英文
        lyric = currentSong.lyric?.lrc?.lyric.toString()
        if(lyric == "null"){
            //中文
            lyric = currentSong.lyric?.tlyric?.lyric.toString()
        }

        lyricDetail = LyricUtil(lyric).lyricDetail()
        lyricTime = LyricUtil(lyric).lyricTime()
        Log.d("lyricDetail",lyricDetail.toString())
        Log.d("lyricTime",lyricTime.toString())
    }


}