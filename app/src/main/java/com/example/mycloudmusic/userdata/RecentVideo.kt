package com.example.mycloudmusic.userdata

/**
 * 最近观看的视频
 */
data class RecentVideo(
    val code: Int,
    val `data`: DataInfo,
    val message: String
)

data class DataInfo(
    val list: List<BaseInfo>,
    val total: Int
)

data class BaseInfo(
    val `data`: DataY,
    val playTime: Long,
    val resourceId: String,
    val resourceType: String
)

data class DataY(
    val artists: List<Artist>,
    val coverUrl: String,
    val duration: Int,
    val id: Int,
    val name: String
)

data class Artist(
    val id: Int,
    val name: String
)