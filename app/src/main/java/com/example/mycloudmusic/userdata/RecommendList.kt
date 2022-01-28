package com.example.mycloudmusic.userdata

/**
 * 每日推荐歌单类
 */
data class RecommendList(
    val code: Int,
    val featureFirst: Boolean,
    val haveRcmdSongs: Boolean,
    val recommend: List<Recommend>
)

data class Recommend(
    val alg: String,
    val copywriter: String,
    val createTime: Long,
    val creator: CreatorComment,
    val id: Long,
    val name: String,
    val picUrl: String,
    val playcount: Long,
    val trackCount: Int,
    val type: Int,
    val userId: Int
)

data class CreatorComment(
    val accountStatus: Int,
    val authStatus: Int,
    val authority: Int,
    val avatarImgId: Long,
    val avatarImgIdStr: String,
    val avatarUrl: String,
    val backgroundImgId: Long,
    val backgroundImgIdStr: String,
    val backgroundUrl: String,
    val birthday: Int,
    val city: Int,
    val defaultAvatar: Boolean,
    val description: String,
    val detailDescription: String,
    val djStatus: Int,
    val expertTags: Any,
    val followed: Boolean,
    val gender: Int,
    val mutual: Boolean,
    val nickname: String,
    val province: Int,
    val remarkName: Any,
    val signature: String,
    val userId: Int,
    val userType: Int,
    val vipType: Int
)