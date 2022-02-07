package com.example.mycloudmusic.userdata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 最近收听的音乐
 */
data class RecentSong(
    val code: Int,
    val `data`: DataZ,
    val message: String
)

data class DataZ(
    val list: List<Base>,
    val total: Int
)

data class Base(
    val `data`: DataX,
    val playTime: Long,
    val resourceId: String,
    val resourceType: String
)

data class DataX(
    val a: Any,
    val al: Al,
    val alia: List<String>,
    val ar: List<Ar>,
    val cd: String,
    val cf: String,
    val copyright: Int,
    val cp: Int,
    val crbt: Any,
    val djId: Int,
    val dt: Int,
    val fee: Int,
    val ftype: Int,
    val h: H,
    val id: String,
    val l: L,
    val m: M,
    val mark: String,
    val mst: Int,
    val mv: Int,
    val name: String,
    val no: Int,
    val noCopyrightRcmd: Any,
    val originCoverType: Int,
    val originSongSimpleData: Any,
    val pop: Int,
    val pst: Int,
    val publishTime: Long,
    val rt: Any,
    val rtUrl: Any,
    val rtUrls: List<Any>,
    val rtype: Int,
    val rurl: Any,
    val s_id: Int,
    val single: Int,
    val st: Int,
    val t: Int,
    val tns: List<String>,
    val v: Int
)
@Parcelize
data class Al(
    val id: Int,
    val name: String,
    val pic: Long,
    val picUrl: String,
    val pic_str: String,
    val tns: List<String>
): Parcelable

@Parcelize
data class Ar(
//    val alias: List<Any>,
    val id: Int,
    val name: String,
//    val tns: List<Any>
): Parcelable

data class H(
    val br: Int,
    val fid: Int,
    val size: Int,
    val vd: Int
)

data class L(
    val br: Int,
    val fid: Int,
    val size: Int,
    val vd: Int
)

data class M(
    val br: Int,
    val fid: Int,
    val size: Int,
    val vd: Int
)