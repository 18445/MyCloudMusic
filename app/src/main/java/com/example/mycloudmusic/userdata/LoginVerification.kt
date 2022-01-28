package com.example.mycloudmusic.userdata

data class LoginVerification(
    val code: Int,
    val exist: Int,
    val hasPassword: Boolean,
    val nickname: String
)