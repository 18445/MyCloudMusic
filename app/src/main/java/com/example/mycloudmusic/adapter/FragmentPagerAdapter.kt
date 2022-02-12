package com.example.mycloudmusic.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.fragment.SongDiskFragment
import com.example.mycloudmusic.fragment.SongLyricFragment

class FragmentPagerAdapter (fragmentActivity: FragmentActivity,mPosition:Int,id:String,private val click:()->Unit,setOnPlayer: (Long)->Double) : FragmentStateAdapter(fragmentActivity) {

    private val fragments: SparseArray<BaseFragment> = SparseArray()

    init {
        fragments.put(PAGE_DISK, SongDiskFragment(mPosition,id,))
        fragments.put(PAGE_LYRIC, SongLyricFragment(click,mPosition,setOnPlayer))
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size()
    }

    companion object {

        const val PAGE_DISK = 0

        const val PAGE_LYRIC = 1

    }
}