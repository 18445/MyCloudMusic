package com.example.mycloudmusic.view

import android.content.Context
import android.graphics.Color.*
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout

import android.widget.LinearLayout

import android.widget.TextView

import android.widget.ScrollView


class LyricView : ScrollView {
    //父布局
    var rootView: LinearLayout? = null

    //垂直布局
    var lyricList: LinearLayout? = null

    private var lyricItems = ArrayList<TextView>() //每项的歌词集合
    var lyricTextList = ArrayList<String>() //每行歌词文本集合
    var lyricTimeList = ArrayList<Long>() //每行歌词所对应的时间集合
    var lyricTimeGap = ArrayList<Long>() //每两句歌词之间的间隔
    var lyricItemHeights: ArrayList<Int>? = null //每行歌词TextView所要显示的高度
    //控件高度
    var mHeight = 0
    //控件宽度
    var mWidth = 0

    var prevSelected = 0 //前一个选择的歌词所在的item

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }



    private fun initView() {
        rootView = LinearLayout(context)
        rootView!!.orientation = LinearLayout.VERTICAL
        //创建视图树，会在onLayout执行后立即得到正确的高度等参数
        val vto = rootView!!.viewTreeObserver
        //注册一个观察者来监听视图树，当视图树的布局、视图树的焦点、视图树将要绘制、视图树滚动等发生改变时
        //进行回调来进行相关操作
        vto.addOnGlobalLayoutListener {
            mHeight = height
            mWidth = width
            refreshRootView()
        }
        addView(rootView) //把布局加进去
    }

    /**
     *刷新根布局
     */
    private fun refreshRootView() {
        rootView!!.removeAllViews() //刷新，把之前包含的所有的view清除
        //创建两个空白view
        val blank1 = LinearLayout(context)
        val blank2 = LinearLayout(context)
        //高度平分
        val params = LinearLayout.LayoutParams(width, height / 2)
        rootView!!.addView(blank1, params)
        if (lyricList != null) {
            rootView!!.addView(lyricList) //加入歌词显示布局
            rootView!!.addView(blank2, params)
        }
    }

    /**
     * 设置歌词,
     */
    private fun refreshLyricList() {
        if (lyricList == null) {
            //创建布局
            lyricList = LinearLayout(context)
            //垂直布局
            lyricList!!.orientation = LinearLayout.VERTICAL
            //刷新，重新添加
            lyricList!!.removeAllViews()
            lyricItems.clear()

            lyricItemHeights = ArrayList()
            prevSelected = 0
            //为每行歌词创建一个TextView
            for (i in 0 until lyricTextList.size) {
                val textView = TextView(context)
                textView.textSize = 18f
                textView.text = lyricTextList[i]
                //设置TextView的布局并居中显示
                val params =
                    LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                params.gravity = Gravity.CENTER_HORIZONTAL
                textView.layoutParams = params
                //对高度进行测量
                val vto = textView.viewTreeObserver
                vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        textView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        lyricItemHeights!!.add(i, textView.height) //将高度添加到对应的item位置
                    }
                })
                lyricList!!.addView(textView)
                lyricItems.add(i, textView)
            }
        }
    }

    /**
     * 滚动到index位置
     */
    fun scrollToIndex(index: Int) {
        if (index < 0) {
            scrollTo(0, 0)
        }
        //计算index对应的textview的高度
        if (index < lyricTextList.size) {
            var sum = 0
            for (i in 0 until index) {
                sum += lyricItemHeights!![i]
            }
            //加上index这行高度的一半
            sum += lyricItemHeights!![index] / 2
            scrollTo(0, sum)
        }
    }

    /**
     * 歌词一直滑动，小于歌词总长度
     * 根据长度返回歌词所在的index
     */
    private fun getIndex(length: Int): Int {
        var index = 0
        var sum = 0
        while (sum <= length) {
            if(index == lyricItemHeights!!.size){
                break
            }
            sum += lyricItemHeights!![index]
            index++
        }
        //从1开始，所以得到的是总item，脚标就得减一
        return index - 1
    }

    /**
     * 设置选择的index，选中的颜色
     */
    private fun setSelected(index: Int) {
        //如果和之前选的一样就不变
        if (index == prevSelected) {
            return
        }
        for (i in 0 until lyricItems.size) {
            //设置选中和没选中的的颜色
            if (i == index) {
                lyricItems[i].setTextColor(LTGRAY)
            } else {
                lyricItems[i].setTextColor(DKGRAY)
            }
            prevSelected = index
        }
    }

    /**
     * 设置歌词，并调用之前写的refreshLyricList()方法设置view
     */
    fun setLyricText(textList: ArrayList<String>, timeList: ArrayList<Long>) {
        lyricTextList = textList
        lyricTimeList = timeList
        refreshLyricList()
    }



    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        //滑动时，不往回弹，滑到哪就定位到哪
        setSelected(getIndex(t))
        if (listener != null) {
            listener!!.onLyricScrollChange(getIndex(t), getIndex(oldt))
        }
    }


    var listener: OnLyricScrollChangeListener? = null

    fun setOnLyricScrollChangeListener(onListener: OnLyricScrollChangeListener?) {
        listener = onListener
    }

    /**
     * 向外部提供接口
     */
    interface OnLyricScrollChangeListener {
        fun onLyricScrollChange(index: Int, oldindex: Int)
    }



}