package com.example.mycloudmusic.userdata

/**
 * 个人推荐歌单
 */
data class PersonalizeList(
    val category: Int,
    val code: Int,
    val hasTaste: Boolean,
    val result: List<ListResult>
)

data class ListResult(
    val alg: String,
    val canDislike: Boolean,
    val copywriter: String,
    val highQuality: Boolean,
    val id: Long,
    val name: String,
    val picUrl: String,
    val playCount: Long,
    val trackCount: Int,
    val trackNumberUpdateTime: Long,
    val type: Int
)