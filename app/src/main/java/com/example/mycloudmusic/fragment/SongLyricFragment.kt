package com.example.mycloudmusic.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.userdata.OneSong
import com.example.mycloudmusic.util.LyricUtil
import com.example.mycloudmusic.viewmodel.SongViewModel
import android.view.MotionEvent
import android.view.View.OnTouchListener
import androidx.core.view.isVisible
import com.example.mycloudmusic.R
import com.example.mycloudmusic.util.asyncGet
import com.example.mycloudmusic.view.LyricView
import kotlin.math.abs


/**
 * 歌曲界面歌词类
 */
class SongLyricFragment (private val click:()->Unit,private val mPosition:Int,private val setOnPlayer: (Long)->Double): BaseFragment (){

    var lyricTimeGap = ArrayList<Long>() //每两句歌词之间的间隔
    private lateinit var currentSong : OneSong
    private lateinit var mSongModel : SongViewModel
    private lateinit var lyric:String
    private lateinit var lyricDetail : List<String>
    private lateinit var lyricTime : List<String>
    private lateinit var lyricTimeList : List<Long>
    private lateinit var line : View
    private lateinit var view: LyricView
    private var lrcIndex = 0
    private var currentTime = 0.0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lyric, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSongModel = ViewModelProvider(requireActivity()).get(SongViewModel::class.java)
        Handler(Looper.myLooper()!!).postDelayed(
            {   getLyric()
                initViews()
                toTime()
                initEvents()
            }
            ,3500
        )
    }

    /**
     * 获得当前歌曲的歌词和对应的时间
     */
    private fun getLyric(){
        currentSong = mSongModel.userSongs[mPosition.toString()]!!
        //英文
        lyric = currentSong.lyric?.lrc?.lyric.toString()
        if(lyric == "null"){
            //中文
            lyric = currentSong.lyric?.tlyric?.lyric.toString()
        }

        lyricDetail = LyricUtil(lyric).lyricDetail()
        lyricTime = LyricUtil(lyric).lyricTime()
        Log.d("lyricDetail",lyricDetail.toString())
        Log.d("lyricTime",lyricTime.toString())
    }

    /**
     * 设置每一个歌词的间隔时间
     */
    private fun setTimeGap(){
        for(i in 0 until lyricTimeList.size - 1){
            lyricTimeGap.add(lyricTimeList[i+1]-lyricTimeList[i])
        }
        Log.d("lyricTimeGap",lyricTimeGap.toString())
    }

    /**
     * 将 00：00.000 格式的时间转换为 ms 单位的时间
     */
    private fun toTime(){
        val tempList = mutableListOf<Long>()
        var tempTime : Long
        for(i in lyricTime.indices){
            val line = lyricTime[i].split(":")
            Log.d("line",line.toString())
            if(line.size == 1){
                break
            }
            //分钟
            val temp1 = line[0].toLong().times(60)

            val temp2 = line[1].split(".")
            //秒钟
            val temp3 = temp2[0].toLong()
            val temp4 = temp2[1].toLong().div(1000)
            val temp5 = temp3+temp4
            //转化为毫秒
            tempTime = temp1 + temp5
            tempTime *= 1000
            Log.d("tempList",tempTime.toString())
            tempList.add(tempTime)
        }
        lyricTimeList = tempList
        setTimeGap()
        setNextLyric(0)
//        beginLyric()
        Log.d("lyricTimeList", lyricTimeList.toString())
    }



    private fun initViews() {
        view = requireView().findViewById(R.id.lv_lyric_view)
        line = requireView().findViewById(R.id.line_lyric_view)
        line.isVisible = false
    }

    /**
     * 从index位置处开始歌曲的循环效果
     * lrcIndex : 当前歌词所在的位置
     * index : 期望歌词所在的位置
     */
    val handler = Handler(Looper.myLooper()!!)
    private fun setNextLyric(index : Int){
        //如果开始的位置和当前位置不一样
        if (index < 0 ){
            return
        }
        Log.d("setNextLyric","enter The Fun")
        if(index != lrcIndex){
            Log.d("setNextLyric","resize the index:$index lrcIndex:$lrcIndex")
            lrcIndex = index
//            view.removeCallbacks(mRunnable)
        }
        if(lrcIndex < lyricTimeGap.size){
            Log.d("setNextLyric","enterThePostDelay")
            if(lyricTimeGap.size != 0 ){
//                view.postDelayed({
//                    Log.d("setNextLyric","enterTheRunnable")
//                    mRunnable
//                    },lyricTimeGap[lrcIndex])

                handler.postDelayed({
                    Log.d("setNextLyric","enterTheRunnable")
                    mRunnable
                    },lyricTimeGap[lrcIndex])
            }
        }
    }

    private val mRunnable =  Runnable (){
        Log.d("setNextLyric","entertherunnable $lrcIndex")
        view.scrollToIndex(lrcIndex)
        lrcIndex++
        setNextLyric(lrcIndex)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEvents() {
        view.setLyricText(lyricDetail as ArrayList<String>, lyricTimeList as ArrayList<Long>)
        Handler(Looper.myLooper()!!).postDelayed(
            {
                setNextLyric(0)}
        ,5000
        )

        view.setOnLyricScrollChangeListener(object : LyricView.OnLyricScrollChangeListener {
            override fun onLyricScrollChange(index: Int, oldindex: Int) {
                lrcIndex = index
                println("====$index======")
                currentTime = setOnPlayer(lyricTimeList[lrcIndex+1])
                setNextLyric(lrcIndex)
            }
        })

        //点击事件
        var lastX = 0f
        var lastY = 0f
        var dX = 0f
        var dY = 0f
        view.setOnTouchListener (){ _, ev ->
            val currentX = ev?.x
            val currentY = ev?.y
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    //按下手势
                    if (currentX != null) {
                        lastX = currentX
                    }
                    if (currentY != null) {
                        lastY = currentY
                    }
                    Log.d("MotionEvent","ACTION_DOWN")
                    if(dX < 50 || dY<20){
                        click
                    }
                }
                MotionEvent.ACTION_UP -> {
                    //收起手势
                    if (line.isVisible){
                        line.isVisible = false
                    }
//                    view.performClick()
                    Log.d("MotionEvent","ACTION_UP")
                }
                MotionEvent.ACTION_MOVE ->{
                    //移动时
                    line.isVisible = true
                    Log.d("MotionEvent","ACTION_MOVE")
                    if (currentX != null) {
                        dX = abs(lastX - currentX)
                    }
                    if (currentY != null) {
                        dY = abs(lastY - currentY)
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    if (line.isVisible){
                        line.isVisible = false
                    }
                }
            }
            false
        }

    }
}