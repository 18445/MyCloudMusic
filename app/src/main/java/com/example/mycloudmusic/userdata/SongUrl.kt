package com.example.mycloudmusic.userdata

/**
 * 歌曲的Url类
 */
data class SongUrl(
    val code: Int,
    val `data`: List<UrlInfo>
)

data class UrlInfo(
    val br: Int,
    val canExtend: Boolean,
    val code: Int,
    val encodeType: Any,
    val expi: Int,
    val fee: Int,
    val flag: Int,
    val freeTimeTrialPrivilege: FreeTimeTrialPrivilege,
    val freeTrialInfo: Any,
    val freeTrialPrivilege: FreeTrialPrivilege,
    val gain: Int,
    val id: Int,
    val level: Any,
    val md5: String,
    val payed: Int,
    val size: Int,
    val type: String,
    val uf: Any,
    val url: String,
    val urlSource: Int
)

data class FreeTimeTrialPrivilege(
    val remainTime: Int,
    val resConsumable: Boolean,
    val type: Int,
    val userConsumable: Boolean
)

data class FreeTrialPrivilege(
    val listenType: Any,
    val resConsumable: Boolean,
    val userConsumable: Boolean
)