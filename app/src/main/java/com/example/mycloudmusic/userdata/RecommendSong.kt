package com.example.mycloudmusic.userdata

/**
 * 每日推荐歌曲
 */
data class RecommendSong(
    val code: Int,
    val `data`: DataSong
)

data class DataSong(
    val dailySongs: List<DailySong>,
    val orderSongs: List<Any>,
    val recommendReasons: List<RecommendReason>
)

data class DailySong(
    val a: Any,
    val al: AlSong,
    val alg: String,
    val alia: List<Any>,
    val ar: List<ArSong>,
    val cd: String,
    val cf: String,
    val copyright: Int,
    val cp: Int,
    val crbt: Any,
    val djId: Int,
    val dt: Int,
    val fee: Int,
    val ftype: Int,
    val h: HSong,
    val id: Int,
    val l: LSong,
    val m: MSong,
    val mark: Int,
    val mst: Int,
    val mv: Int,
    val name: String,
    val no: Int,
    val noCopyrightRcmd: Any,
    val originCoverType: Int,
    val originSongSimpleData: Any,
    val pop: Int,
    val privilege: PrivilegeSong,
    val pst: Int,
    val publishTime: Long,
    val reason: String,
    val rt: String,
    val rtUrl: Any,
    val rtUrls: List<Any>,
    val rtype: Int,
    val rurl: Any,
    val s_id: Int,
    val single: Int,
    val st: Int,
    val t: Int,
    val v: Int
)

data class RecommendReason(
    val reason: String,
    val songId: Int
)

data class AlSong(
    val id: Int,
    val name: String,
    val pic: Long,
    val picUrl: String,
    val pic_str: String,
    val tns: List<Any>
)

data class ArSong(
    val alias: List<Any>,
    val id: Int,
    val name: String,
    val tns: List<Any>
)

data class HSong(
    val br: Int,
    val fid: Int,
    val size: Int,
    val vd: Int
)

data class LSong(
    val br: Int,
    val fid: Int,
    val size: Int,
    val vd: Int
)

data class MSong(
    val br: Int,
    val fid: Int,
    val size: Int,
    val vd: Int
)

data class PrivilegeSong(
    val chargeInfoList: List<ChargeInfoSong>,
    val cp: Int,
    val cs: Boolean,
    val dl: Int,
    val downloadMaxbr: Int,
    val fee: Int,
    val fl: Int,
    val flag: Int,
    val freeTrialPrivilege: FreeTrialPrivilegeSong,
    val id: Int,
    val maxbr: Int,
    val payed: Int,
    val pl: Int,
    val playMaxbr: Int,
    val preSell: Boolean,
    val rscl: Any,
    val sp: Int,
    val st: Int,
    val subp: Int,
    val toast: Boolean
)

data class ChargeInfoSong(
    val chargeMessage: Any,
    val chargeType: Int,
    val chargeUrl: Any,
    val rate: Int
)

data class FreeTrialPrivilegeSong(
    val resConsumable: Boolean,
    val userConsumable: Boolean
)