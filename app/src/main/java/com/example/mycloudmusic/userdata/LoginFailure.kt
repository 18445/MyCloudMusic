package com.example.mycloudmusic.userdata

/**
 * 登录失败的数据
 */
data class LoginFailure(
    val code: Int,
    val message: String,
    val msg: String
)