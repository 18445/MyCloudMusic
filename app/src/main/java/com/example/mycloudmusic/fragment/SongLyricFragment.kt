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
import androidx.core.view.isVisible
import com.example.mycloudmusic.R
import com.example.mycloudmusic.view.LyricView


/**
 * 歌曲界面歌词类
 */
class SongLyricFragment (private val mPosition:Int,private val setOnPlayer:(Long)->Unit): BaseFragment (){

    var lyricTimeGap = ArrayList<Long>() //每两句歌词之间的间隔
    private lateinit var currentSong : OneSong
    private lateinit var mSongModel : SongViewModel
    private lateinit var lyric:String
    private lateinit var lyricDetail : List<String>
    private lateinit var lyricTime : List<String>
    private lateinit var lyricTimeList : List<Long>
    private lateinit var line : View
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
                toTime()
                initViews()
                initEvents()
            }
            ,4000
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
     * 设置没有每一个歌词的间隔时间
     */
    private fun setTimeGap(){
        for(i in 0 until lyricTimeList.size - 1){
            lyricTimeGap.add(lyricTimeList[i+1]-lyricTimeList[i])
        }
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

//        Log.d("tempList", tempList.toString())
        lyricTimeList = tempList
        setTimeGap()
        Log.d("lyricTimeList", lyricTimeList.toString())
    }

    private lateinit var view: LyricView
    private var lrcIndex = 0

    private fun initViews() {
        view = requireView().findViewById(R.id.lv_lyric_view)
        line = requireView().findViewById(R.id.line_lyric_view)
        line.isVisible = false
    }

    private fun setNextLyric(index : Int){
        //如果开始的位置和当前位置不一样
        if(index != lrcIndex){
            lrcIndex = index
            view.removeCallbacks(runnable)
        }
        if(lrcIndex >= lyricTimeGap.size){
            view.postDelayed({
                runnable
                lrcIndex++
                },lyricTimeGap[lrcIndex])
        }

    }


    private val runnable =  Runnable (){
        view.scrollToIndex(lrcIndex)
        setNextLyric(lrcIndex+1)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEvents() {
        view.setLyricText(lyricDetail as ArrayList<String>, lyricTimeList as ArrayList<Long>)

        setNextLyric(0)

        view.setOnLyricScrollChangeListener(object : LyricView.OnLyricScrollChangeListener {
            override fun onLyricScrollChange(index: Int, oldindex: Int) {
                lrcIndex = index
                println("====$index======")
                setOnPlayer(lyricTimeList[lrcIndex])
                setNextLyric(lrcIndex)
                //滚动handle不能放在这，因为，这是滚动监听事件，滚动到下一次，handle又会发送一次消息，出现意想不到的效果
            }
        })

        //点击事件
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    //按下手势
                    line.isVisible = true
                }
                MotionEvent.ACTION_UP -> {
                    //收起手势
                    line.isVisible = false
                }
                MotionEvent.ACTION_CANCEL -> {}
            }
            false
        }
    }
}