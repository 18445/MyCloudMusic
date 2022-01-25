package com.example.mycloudmusic.data class UnInitUser(
    val UnusedAccount: UnusedAccount,
    val bindings: List<Any>,
    val code: Int,
    val cookie: String,
    val loginType: Int,
    val token: String
)

data class UnusedAccount(
    val anonimousUser: Boolean,
    val ban: Int,
    val baoyueVersion: Int,
    val createTime: Long,
    val donateVersion: Int,
    val id: Long,
    val salt: String,
    val status: Int,
    val tokenVersion: Int,
    val type: Int,
    val uninitialized: Boolean,
    val userName: String,
    val vipType: Int,
    val viptypeVersion: Int,
    val whitelistAuthority: Int
)