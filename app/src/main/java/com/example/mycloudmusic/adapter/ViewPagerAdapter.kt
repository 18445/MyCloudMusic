package com.example.mycloudmusic.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.example.mycloudmusic.base.BaseFragment


class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, fragments: List<BaseFragment>): FragmentStateAdapter(fragmentManager, lifecycle) {

    private val mList = fragments

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mList[position]
    }

}
