package com.example.mycloudmusic.util

import android.media.AudioAttributes
import android.media.MediaPlayer

import android.media.MediaPlayer.*
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log

import android.widget.SeekBar
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*


/**
 * 音乐播放工具类
 */
class Player(private val skbProgress: SeekBar) : OnBufferingUpdateListener,
    OnCompletionListener, OnPreparedListener {

    var mediaPlayer: MediaPlayer? = null
    private val mTimer: Timer = Timer()

    /**
     * 通过定时器和Handler来更新进度条
     */
    var mTimerTask: TimerTask = object : TimerTask() {
        override fun run() {
            if (mediaPlayer == null) return
            if (mediaPlayer!!.isPlaying && !skbProgress.isPressed) {
                handleProgress.sendEmptyMessage(0)
            }
        }
    }
    var handleProgress: Handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            val position = mediaPlayer!!.currentPosition
            val duration = mediaPlayer!!.duration
            if (duration > 0) {
                val pos = (skbProgress.max * position / duration).toLong()
                skbProgress.progress = pos.toInt()
            }
        }
    }



    fun playUrl(videoUrl: String?) {
        try {
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(videoUrl)
            mediaPlayer!!.prepare() //prepare之后自动播放
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun play() {
        mediaPlayer!!.start()
    }

    fun pause() {
        mediaPlayer!!.pause()
    }

    fun stop() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    /**
     * 通过onPrepared播放
     */
    override fun onPrepared(player: MediaPlayer) {
        player.start()
        Log.e("mediaPlayer", "onPrepared")
    }

    override fun onCompletion(player: MediaPlayer?) {
        Log.e("mediaPlayer", "onCompletion")
    }

    override fun onBufferingUpdate(player: MediaPlayer?, bufferingProgress: Int) {
        skbProgress.secondaryProgress = bufferingProgress
        val currentProgress = skbProgress.max * mediaPlayer!!.currentPosition / mediaPlayer!!.duration
        Log.e("$currentProgress% play", "$bufferingProgress% buffer")
    }

    init {
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
            mediaPlayer!!.setOnBufferingUpdateListener(this)
            mediaPlayer!!.setOnPreparedListener(this)
        } catch (e: Exception) {
            Log.e("mediaPlayer", "error", e)
        }
        mTimer.schedule(mTimerTask, 0, 1000)
    }
}