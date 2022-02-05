package com.example.mycloudmusic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycloudmusic.R
import com.example.mycloudmusic.item.PlayerListItem
import android.util.Log
import com.example.mycloudmusic.`interface`.RecyclerItemClickListener


/**
 * 与歌单有关的RecycleView适配器
 */

/**
 * 创建歌单类
 */
class PlayerListAdapter(val mOnItemClickListener: RecyclerItemClickListener?, private val playerListItems : List<PlayerListItem>, private val context: Context, private val itemSize:Int) : RecyclerView.Adapter<PlayerListAdapter.Holder>() {
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

        //设置Item点击回调
        mOnItemClickListener?.let {
            holder.itemView.setOnClickListener {
                mOnItemClickListener.onRecyclerViewItemClick(holder.itemView,position)
            }
        }
    }

    override fun getItemCount(): Int {
//        由于playerListItem的长度包括创建的和收藏的两种 所以不能用一下方法
//        return playerListItems.size
        Log.d("itemSize",itemSize.toString())
        return itemSize
    }

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val title:TextView = view.findViewById(R.id.tv_playlist_title)
        val content : TextView = view.findViewById(R.id.tv_playlist_content)
        val image :ImageView = view.findViewById(R.id.iv_playlist_cover)
    }
}

/**
 * 推荐歌单类
 */
class PlayerListAdapter2(private val playerListItems : List<PlayerListItem>,private val context: Context) : RecyclerView.Adapter<PlayerListAdapter2.Holder>() {
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
 * 收藏歌单类
 */
class PlayerListAdapter3(private val playerListItems : List<PlayerListItem>,private val context: Context) : RecyclerView.Adapter<PlayerListAdapter3.Holder>() {
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


