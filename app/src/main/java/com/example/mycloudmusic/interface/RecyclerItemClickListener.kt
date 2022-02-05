package com.example.mycloudmusic.`interface`

import android.view.View

/**
 * RecycleViewItem点击事件回调接口
 */
interface RecyclerItemClickListener {
    fun onRecyclerViewItemClick(view: View, position: Int);
}