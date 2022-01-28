package com.example.mycloudmusic.userdata

/**
 * 用户等级信息类
 */
class UserLevel(
    val code: Int,
    val `data`: Data,
    val full: Boolean
)

data class Data(
    val info: String,
    val level: Int,
    val nextLoginCount: Int,
    val nextPlayCount: Int,
    val nowLoginCount: Int,
    val nowPlayCount: Int,
    val progress: Double,
    val userId: Int
)