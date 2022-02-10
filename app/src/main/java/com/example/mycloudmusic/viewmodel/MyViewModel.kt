package com.example.mycloudmusic.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mycloudmusic.userdata.*

/**
 * 在主页面的ViewModel
 */
class MyViewModel: ViewModel() {
    private lateinit var user:User

    fun initUser(mUser:User){
        user = mUser
    }

    fun getUser():User{
        return user
    }

    lateinit var userPlaylist : UserPlaylist
    lateinit var userLevel: UserLevel
    lateinit var recentSong: RecentSong
    lateinit var recentVideo: RecentVideo
    lateinit var userLikeList: UserLikeList
    lateinit var lyric: Lyric
    lateinit var personalizeList: PersonalizeList
    lateinit var personalizeSong: PersonalizeSong
    lateinit var recommendList: RecommendList
    lateinit var recommendSong: RecommendSong
}