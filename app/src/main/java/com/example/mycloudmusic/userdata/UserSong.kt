package com.example.mycloudmusic.userdata

/**
 * 自定义的Song类
 * 用于将数据接受并显示在fragment上
 */
data class UserSong (
//    val userSongs : List<OneSong>
    //歌单中位置-歌曲
    val userSongs : Map<Int,OneSong>
)

data class OneSong(
    var song:Song?,
    var songUrl: SongUrl?,
    var lyric: Lyric?
)