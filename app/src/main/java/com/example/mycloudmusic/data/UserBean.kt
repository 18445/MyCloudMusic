package com.example.mycloudmusic.data

/**
 * 网络请求返回的数据类型
 * User的内容
 */
data class UserBean(
    val id:String,
    val name:String,
    val vipType:String,
    val city:Int,
    val province:Int,
    val birthday:Int,
    val gender:Boolean,
    val ban: Boolean,
    val createTime:Int,
    val nickname:String,
    val followed:Int,
    val follows:Int,
)

data class ResultBean(
    val code:Int,
    val msg:String,
    val Usr:UserBean
)
