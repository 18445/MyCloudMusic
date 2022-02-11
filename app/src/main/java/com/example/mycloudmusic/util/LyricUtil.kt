package com.example.mycloudmusic.util

import android.util.Log
import java.io.BufferedReader
import java.io.ByteArrayInputStream

class LyricUtil (private val lyric:String)  {
    //格式如下：
    //[00:01.000] 作曲 : XXX
    //[00:12.740]x xxx xxx xxx

    private lateinit var listDetail : List<String>
    //每一行的数据
    private val tempOne = lyric.split("\n")

//    private val bf = BufferedReader(
//        InputStreamReader(
//            ByteArrayInputStream(
//                lyric.toByteArray(Charset.forName("utf8"))
//            ), Charset.forName("utf8")
//        )
//    )


    /**
     * 解析歌词内容
     */
    fun lyricDetail() : List<String>{
        val tempList = mutableListOf<String>()
        for(i in tempOne.indices-1){
            val line = tempOne[i].split("]")
            Log.d("Lyric",line.toString())

            if(line.size > 1 ){
                tempList.add(line[1])
            }
        }

        return tempList    }

    /**
     * 解析时间
     */
    fun lyricTime():List<String>{
        val tempList = mutableListOf<String>()
        for(i in tempOne.indices){
            val line = tempOne[i].split("]")
            val time = line[0].replace("[","")
            tempList.add(time)
        }

        return tempList
    }
}


