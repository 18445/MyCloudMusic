package com.example.mycloudmusic.userdata

/**
 * 用户喜欢列表
 */
data class UserLikeList(
    val checkPoint: Long,
    val code: Int,
    val ids: List<Int>
)