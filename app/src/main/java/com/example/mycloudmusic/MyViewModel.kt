package com.example.mycloudmusic

import androidx.lifecycle.ViewModel
import com.example.mycloudmusic.userdata.*

class MyViewModel: ViewModel() {
    private lateinit var user:User

    fun initUser(mUser:User){
        user = mUser
    }

    fun getUser():User{
        return user
    }

    lateinit var userLevel: UserLevel
    lateinit var userPlaylist: UserPlaylist
    lateinit var recentSong: RecentSong
    lateinit var recentVideo: RecentVideo
    lateinit var userLikeList: UserLikeList
    lateinit var lyric: lyric
    lateinit var personalizeList: PersonalizeList
    lateinit var personalizeSong: PersonalizeSong
    lateinit var recommendList: RecommendList
    lateinit var recommendSong: RecommendSong
}