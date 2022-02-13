package com.example.mycloudmusic.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.fragment.SongInnerFragment
import com.example.mycloudmusic.userdata.TrackId

class FragmentPagerOuterAdapter(fragmentActivity: FragmentActivity, mPosition : Int,ids:List<TrackId>,setOnPlayer: (Long)->Double, isVisibility : (Int) -> Unit,isAllowedMove :((Int) -> Unit)) : FragmentStateAdapter(fragmentActivity) {

    private val fragments: SparseArray<BaseFragment> = SparseArray()

    init {

        fragments.put(PAGE_LAST, SongInnerFragment(mPosition,ids[mPosition].id,setOnPlayer,isVisibility,isAllowedMove))
        fragments.put(PAGE_THIS, SongInnerFragment(mPosition+1,ids[mPosition+1].id,setOnPlayer,isVisibility,isAllowedMove))
        fragments.put(PAGE_NEXT, SongInnerFragment(mPosition+2,ids[mPosition+2].id,setOnPlayer,isVisibility,isAllowedMove))

    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size()
    }

    companion object {

        const val PAGE_LAST = 0

        const val PAGE_THIS = 1

        const val PAGE_NEXT= 2

    }
}