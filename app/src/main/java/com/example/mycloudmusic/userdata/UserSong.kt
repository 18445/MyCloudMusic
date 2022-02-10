package com.example.mycloudmusic.userdata

/**
 * 自定义的Song类
 * 用于将数据接受并显示在fragment上
 */
data class UserSong (
    private val userSongs : List<OneSong>
)

data class OneSong(
    private val song:Song?,
    private val songUrl: SongUrl?,
    private val lyric: Lyric?
)