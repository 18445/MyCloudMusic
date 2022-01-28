package com.example.mycloudmusic.userdata

/**
 * 个人私信
 */
data class PrivateMessage(
    val code: Int,
    val more: Boolean,
    val msgs: List<Msg>,
    val newMsgCount: Int
)

data class Msg(
    val fromUser: FromUser,
    val lastMsg: String,
    val lastMsgTime: Long,
    val newMsgCount: Int,
    val noticeAccount: Any,
    val noticeAccountFlag: Boolean,
    val toUser: ToUser
)

data class FromUser(
    val accountStatus: Int,
    val artistName: Any,
    val authStatus: Int,
    val authority: Int,
    val avatarDetail: AvatarDetail,
    val avatarImgId: Long,
    val avatarImgIdStr: String,
    val avatarUrl: String,
    val backgroundImgId: Long,
    val backgroundImgIdStr: String,
    val backgroundUrl: String,
    val birthday: Long,
    val blacklist: Boolean,
    val city: Int,
    val defaultAvatar: Boolean,
    val description: String,
    val detailDescription: String,
    val djStatus: Int,
    val expertTags: Any,
    val experts: Any,
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

data class ToUser(
    val accountStatus: Int,
    val authStatus: Int,
    val authority: Int,
    val avatarDetail: Any,
    val avatarImgId: Long,
    val avatarImgIdStr: String,
    val avatarUrl: String,
    val backgroundImgId: Long,
    val backgroundImgIdStr: String,
    val backgroundUrl: String,
    val birthday: Long,
    val city: Int,
    val defaultAvatar: Boolean,
    val description: String,
    val detailDescription: String,
    val djStatus: Int,
    val expertTags: Any,
    val experts: Any,
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
//
//data class AvatarDetail(
//    val identityIconUrl: String,
//    val identityLevel: Int,
//    val userType: Int
//)