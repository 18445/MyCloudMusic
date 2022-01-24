package com.example.mycloudmusic.data

data class LoginVerification(
    val code: Int,
    val exist: Int,
    val hasPassword: Boolean,
    val nickname: String
)