package com.example.mycloudmusic.util

import android.content.Context
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.example.mycloudmusic.item.PlayerListItem
import java.util.*

/**
 * 自定义 PreloadModelProvider
 * getPreloadItems(int position)，作用是收集并返回一个给定位置的 Model方法的对象
 * getPreloadRequestBuilder取出一个 Model 并生产一个 RequestBuilder，用于预加载给定的 Model 到内存中
 */
class MyPreloadModelProvider(private val context: Context, private val urls: List<PlayerListItem>) :
    ListPreloader.PreloadModelProvider<String> {
    override fun getPreloadItems(position: Int): List<String> {
        val url = urls[position].imageResource
        return if (TextUtils.isEmpty(url)) {
            Collections.emptyList()
        } else Collections.singletonList(url)
    }

    override fun getPreloadRequestBuilder(item: String): RequestBuilder<*> {
        //返回的 RequestBuilder ，必须与onBindViewHolder 里启动的请求使用完全相同的一组选项 (占位符， 变换等) 和完全相同的尺寸
        return Glide.with(context)
            .load(item)
            .centerCrop()
    }
}