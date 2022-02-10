package com.example.mycloudmusic.viewmodel

import android.service.autofill.UserData
import androidx.lifecycle.ViewModel
import com.example.mycloudmusic.userdata.UserSong

/**
 * 在歌曲界面的ViewModel
 */
class SongViewModel : ViewModel(){
    lateinit var userSongs : UserSong
}

