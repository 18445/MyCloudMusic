package com.example.mycloudmusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycloudmusic.R
import com.example.mycloudmusic.`interface`.RecyclerItemClickListener
import com.example.mycloudmusic.item.SongItem


/**
 * 歌单adapter类
 */
class SongListAdapter(private val mOnItemClickListener:RecyclerItemClickListener?, private val SongItems : List<SongItem>) : RecyclerView.Adapter<SongListAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):Holder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_song_type_rv,parent,false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder:Holder, position: Int) {
        val currentItem : SongItem= SongItems[position]
        holder.position.text = currentItem.position
        holder.detail.text = currentItem.songDetail
        holder.name.text = currentItem.SongName
        //设计点击事件
        mOnItemClickListener?.let {
            holder.itemView.setOnClickListener {
                mOnItemClickListener.onRecyclerViewItemClick(holder.itemView,position)
            }
        }
    }

    override fun getItemCount(): Int {
        return SongItems.size
    }

    class Holder(view:View):RecyclerView.ViewHolder(view){
        val position : TextView = view.findViewById(R.id.tv_song_position)
        val name : TextView = view.findViewById(R.id.tv_song_name)
        val detail : TextView = view.findViewById(R.id.tv_song_detail)
    }


}
