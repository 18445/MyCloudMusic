package com.example.mycloudmusic.viewmodel

import android.service.autofill.UserData
import androidx.lifecycle.ViewModel
import com.example.mycloudmusic.userdata.Lyric
import com.example.mycloudmusic.userdata.OneSong
import com.example.mycloudmusic.userdata.UserSong
import com.example.mycloudmusic.view.RotateCircleImageView
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference

/**
 * 在歌曲界面的ViewModel
 */
class SongViewModel : ViewModel(){
//    var userSongs : UserSong = mutableMapOf<Int,OneSong>()
    var userSongs :MutableMap<String,OneSong> = mutableMapOf()
    lateinit var cookieList : List<String>
//    lateinit var currentDisk : RotateCircleImageView
    var currentDisk: SoftReference<RotateCircleImageView>? = null
}

