package com.example.mycloudmusic.util

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.request.target.ImageViewTarget

/**
 * 用户glide的放大图片类
 */
class TransformationUtils (private val target:ImageView): ImageViewTarget<Bitmap>(target) {
    override fun setResource(resource: Bitmap?) {
        view.setImageBitmap(resource)
        //获得原图的宽度和高度
        val width = resource?.width
        val height = resource?.height
        //获得ImageView的高度
        val imageViewHeight = target.height
        //获得比例
        val sy = height?.let { imageViewHeight.toFloat().div(it).times(2) }
        //得到计算后的结果
        val currentWidth = width?.times(sy!!)
        val currentHeight = height?.times(sy!!)
        target.updateLayoutParams {
            val params = target.layoutParams
            params.height = currentHeight?.toInt()!!
            params.width = currentWidth?.toInt()!!
        }
    }
}

