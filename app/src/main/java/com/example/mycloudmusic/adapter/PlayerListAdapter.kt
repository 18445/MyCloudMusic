package com.example.mycloudmusic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mycloudmusic.R
import com.example.mycloudmusic.item.PlayerListItem
import com.bumptech.glide.request.target.SimpleTarget
import androidx.annotation.NonNull

import com.bumptech.glide.RequestBuilder

import android.text.TextUtils

import com.bumptech.glide.ListPreloader
import com.bumptech.glide.ListPreloader.PreloadModelProvider
import java.util.*


class PlayerListAdapter(private val playerListItems : List<PlayerListItem>,private val context: Context) : RecyclerView.Adapter<PlayerListAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_playerlist_type_rv, parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = playerListItems[position]
        Glide.with(context)
            .load(currentItem.imageResource)
            .centerCrop()
            .into(holder.image)
        holder.title.text = currentItem.title
        holder.content.text = currentItem.content
    }

    override fun getItemCount(): Int {
        return playerListItems.size
    }

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val title:TextView = view.findViewById(R.id.tv_playlist_title)
        val content : TextView = view.findViewById(R.id.tv_playlist_content)
        val image :ImageView = view.findViewById(R.id.iv_playlist_cover)
    }
}

/**
 * 自定义 PreloadModelProvider
 * getPreloadItems(int position)，作用是收集并返回一个给定位置的 Model方法的对象
 * getPreloadRequestBuilder取出一个 Model 并生产一个 RequestBuilder，用于预加载给定的 Model 到内存中
 */
class MyPreloadModelProvider(private val context: Context, private val urls: List<PlayerListItem>) :
    PreloadModelProvider<String> {
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