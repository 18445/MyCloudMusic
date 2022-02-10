package com.example.mycloudmusic.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.fragment.SongDiskFragment
import com.example.mycloudmusic.fragment.SongLyricFragment

class FragmentPagerAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments: SparseArray<BaseFragment> = SparseArray()

    init {
        fragments.put(PAGE_HOME, SongDiskFragment())
        fragments.put(PAGE_FIND, SongLyricFragment())

    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size()
    }

    companion object {

        const val PAGE_HOME = 0

        const val PAGE_FIND = 1

    }
}