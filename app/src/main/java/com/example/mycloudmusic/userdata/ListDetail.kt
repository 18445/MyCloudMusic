package com.example.mycloudmusic.userdata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName





/**
 * 获得歌单的详情
 */
data class ListDetail(
    val code: Int,
    @SerializedName("playlist")
    val SongList: SongList,
    val privileges: List<Privilege>,
    val relatedVideos: Any,
    val resEntrance: Any,
    val sharedPrivilege: Any,
    val urls: Any
)

//

data class TrackId(
    val alg: Any,
    val at: Long,
    val id: Int,
    val rcmdReason: String,
    val t: Int,
    val uid: Int,
    val v: Int
)

data class Track(
//    val a: Any,
    val al: Al,
    val alia: List<String>,
    val ar: List<Ar>,
    val cd: String,
    val cf: String,
    val copyright: String,
    val cp: String,
//    val crbt: Any,
    val djId: String,
    val dt: String,
    val fee: String,
    val ftype: String,
//    val h: H,
    val id: String,
//    val l: L,
//    val m: M,
    val mark: String,
    val mst: String,
    val mv: String,
    val name: String,
    val no: String,
//    val noCopyrightRcmd: Any,
    val originCoverType: Int,
//    val originSongSimpleData: Any,
    val pop: String,
    val pst: String,
    val publishTime: Long,
    val rt: String,
//    val rtUrl: Any,
//    val rtUrls: List<Any>,
//    val rtype: Int,
//    val rurl: Any,
    val s_id: String,
    val single: String,
    val st: String,
    val t: String,
    val tns: List<String>,
    val v: String
)



data class SongList(
    val adType: Int,
    val backgroundCoverId: Int,
    val backgroundCoverUrl: Any,
    val cloudTrackCount: Int,
    val commentCount: Int,
    val commentThreadId: String,
    val coverImgId: Long,
    val coverImgId_str: String,
    val coverImgUrl: String,
    val createTime: Long,
    val creator: Creator,
    val description: Any,
    val englishTitle: Any,
    val highQuality: Boolean,
    val historySharedUsers: Any,
    val id: String,
    val name: String,
    val newImported: Boolean,
    val officialPlaylistType: Any,
    val opRecommend: Boolean,
    val ordered: Boolean,
    val playCount: String,
    val privacy: String,
    val remixVideo: Any,
    val shareCount: Int,
    val sharedUsers: Any,
    val specialType: Int,
    val status: Int,
    val subscribed: Boolean,
    val subscribedCount: Int,
    val subscribers: List<Any>,
    val tags: List<Any>,
    val titleImage: Int,
    val titleImageUrl: Any,
    val trackCount: Int,
    val trackIds: List<TrackId>,
    val trackNumberUpdateTime: Long,
    val trackUpdateTime: Long,
    val tracks: List<Track>,
    val updateFrequency: Any,
    val updateTime: Long,
    val userId: String,
    val videoIds: Any,
    val videos: Any
)
//data class Privilege(
//    val chargeInfoList: List<ChargeInfo>,
//    val cp: Int,
//    val cs: Boolean,
//    val dl: Int,
//    val downloadMaxbr: Int,
//    val fee: Int,
//    val fl: Int,
//    val flag: Int,
//    val freeTrialPrivilege: FreeTrialPrivilege,
//    val id: Int,
//    val maxbr: Int,
//    val paidBigBang: Boolean,
//    val payed: Int,
//    val pc: Any,
//    val pl: Int,
//    val playMaxbr: Int,
//    val preSell: Boolean,
//    val realPayed: Int,
//    val rscl: Any,
//    val sp: Int,
//    val st: Int,
//    val subp: Int,
//    val toast: Boolean
//)

//data class Creator(
//    val accountStatus: Int,
//    val anchor: Boolean,
//    val authStatus: Int,
//    val authenticationTypes: Int,
//    val authority: Int,
//    val avatarDetail: Any,
//    val avatarImgId: Long,
//    val avatarImgIdStr: String,
//    val avatarImgId_str: String,
//    val avatarUrl: String,
//    val backgroundImgId: Long,
//    val backgroundImgIdStr: String,
//    val backgroundUrl: String,
//    val birthday: Int,
//    val city: Int,
//    val defaultAvatar: Boolean,
//    val description: String,
//    val detailDescription: String,
//    val djStatus: Int,
//    val expertTags: Any,
//    val experts: Any,
//    val followed: Boolean,
//    val gender: Int,
//    val mutual: Boolean,
//    val nickname: String,
//    val province: Int,
//    val remarkName: Any,
//    val signature: String,
//    val userId: Int,
//    val userType: Int,
//    val vipType: Int
//)

//
//data class Al(
//    val id: Int,
//    val name: String,
//    val pic: Long,
//    val picUrl: String,
//    val pic_str: String,
//    val tns: List<String>
//)
//
//data class Ar(
//    val alias: List<Any>,
//    val id: Int,
//    val name: String,
//    val tns: List<Any>
//)
//
//data class H(
//    val br: Int,
//    val fid: Int,
//    val size: Int,
//    val vd: Int
//)
//
//data class L(
//    val br: Int,
//    val fid: Int,
//    val size: Int,
//    val vd: Int
//)
//
//data class M(
//    val br: Int,
//    val fid: Int,
//    val size: Int,
//    val vd: Int
//)
//
//data class ChargeInfo(
//    val chargeMessage: Any,
//    val chargeType: Int,
//    val chargeUrl: Any,
//    val rate: Int
//)
//
//data class FreeTrialPrivilege(
//    val resConsumable: Boolean,
//    val userConsumable: Boolean
//)