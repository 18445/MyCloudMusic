package com.example.mycloudmusic.userdata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * 用户歌单信息
 */

@Parcelize
data class UserPlaylist (
    val code: Int,
    val more: Boolean,
    val playlist: List<Playlist>,
    val version: String
) : Parcelable

@Parcelize
data class Playlist(
//    val tracks: List<Track>,
//    val adType: Int,
//    val anonimous: Boolean,
//    val artists: Any,
//    val backgroundCoverId: Int,
//    val backgroundCoverUrl: Any,
//    val cloudTrackCount: Int,
//    val commentThreadId: String,
//    val coverImgId: Long,
//    val coverImgId_str: String,
    val coverImgUrl: String,
//    val createTime: Long,
    val creator: Creator,
    val description: String?,
//    val englishTitle: Any,
//    val highQuality: Boolean,
    val id: String,
    val name: String,
//    val newImported: Boolean,
//    val opRecommend: Boolean,
//    val ordered: Boolean,
//    val playCount: Int,
//    val privacy: Int,
//    val recommendInfo: Any,
//    val shareStatus: Any,
//    val sharedUsers: Any,
//    val specialType: Int,
//    val status: Int,
//    val subscribed: Boolean,
//    val subscribedCount: Int,
//    val subscribers: List<Any>,
//    val tags: List<String>,
//    val titleImage: Int,
//    val titleImageUrl: Any,
//    val totalDuration: Int,
    val trackCount: String,
//    val trackNumberUpdateTime: Long,
//    val trackUpdateTime: Long,
//    val tracks: Any,
//    val updateFrequency: Any,
//    val updateTime: Long,
//    val userId: Int
) : Parcelable

@Parcelize
data class Creator(
//    val accountStatus: Int,
//    val anchor: Boolean,
//    val authStatus: Int,
//    val authenticationTypes: Int,
//    val authority: Int,
//    val avatarDetail: Any,
//    val avatarImgId: Long,
//    val avatarImgIdStr: String,
//    val avatarImgId_str: String,
    val avatarUrl: String,
//    val backgroundImgId: Long,
//    val backgroundImgIdStr: String,
    val backgroundUrl: String,
//    val birthday: Int,
//    val city: Int,
//    val defaultAvatar: Boolean,
//    val description: String,
//    val detailDescription: String,
//    val djStatus: Int,
//    val expertTags: Any,
//    val experts: Any,
//    val followed: Boolean,
    val gender: Int,
//    val mutual: Boolean,
    val nickname: String,
//    val province: Int,
//    val remarkName: Any,
//    val signature: String,
    val userId: String,
//    val userType: Int,
//    val vipType: Int
) : Parcelable