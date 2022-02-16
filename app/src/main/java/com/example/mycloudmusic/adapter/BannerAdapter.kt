package com.example.mycloudmusic.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mycloudmusic.R

class BannerAdapter (private val mImages:List<Drawable>) : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_banner_type_rv,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val index = position%5
        holder.mContainer.background = mImages[index]
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val mContainer : ConstraintLayout = itemView.findViewById(R.id.banner_container)
    }
}