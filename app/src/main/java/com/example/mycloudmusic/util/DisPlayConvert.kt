package com.example.mycloudmusic.util
import android.content.Context
import java.security.AccessController.getContext

/**
 * dp/px/sp之间转换的工具类
 * 具体公式我也不知道怎么来的 :(
 *
 */


 fun dipToPx(context: Context,dip: Float): Int {
    val density:Float = context.resources.displayMetrics.density
    return (dip * density + 0.5f * if (dip >= 0) 1 else -1).toInt()
}

/**
 * 将px转换为与之相等的dp
 */
fun pxToDp(context: Context, pxValue: Float): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}


/**
 * 将dp转换为与之相等的px
 */
fun dpToPx(context: Context, dipValue: Float): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}


/**
 * 将px转换为sp
 */
fun pxToSp(context: Context, pxValue: Float): Int {
    val fontScale: Float = context.resources.displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}


/**

 * 将sp转换为px
 */
fun spToPx(context: Context, spValue: Float): Int {
    val fontScale: Float = context.resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}