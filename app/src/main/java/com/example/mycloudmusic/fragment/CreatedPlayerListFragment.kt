package com.example.mycloudmusic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mycloudmusic.MyViewModel
import com.example.mycloudmusic.R
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.item.PlayerListItem
import com.example.mycloudmusic.userdata.UserPlaylist
import com.bumptech.glide.util.FixedPreloadSizeProvider
import com.example.mycloudmusic.adapter.MyPreloadModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader


class CreatedPlayerListFragment : BaseFragment() {
    private val mUserPlayerList = ViewModelProvider(requireActivity()).get(MyViewModel::class.java).userPlaylist
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mPlayerList : List<PlayerListItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_createdplayerlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initRecyclerView()
        mPlayerList = generatePlayerList(mUserPlayerList)
    }

    /**
     * 初始化RecycleView的炒作
     * 添加PreLoad
     */
    private fun initRecyclerView() {
        val sizeProvider = FixedPreloadSizeProvider<String>(650, 650)
        val preloadModelProvider =  MyPreloadModelProvider(requireContext(), mPlayerList)
        val preload: RecyclerViewPreloader<String> = RecyclerViewPreloader(
            Glide.with(this), preloadModelProvider,
            sizeProvider, 10
        )
        mRecyclerView.addOnScrollListener(preload);
    }

    /**
     * 根据解析出的json，将UserPlayerList生成List<PlayerListItem>
     */
    private fun generatePlayerList(mUserPlayerList: UserPlaylist): List<PlayerListItem> {
        val mList = mutableListOf<PlayerListItem>()
        //添加每一项Item到列表并返回
        for(index in 0..mUserPlayerList.playlist.size){
            val tempItem = PlayerListItem(mUserPlayerList.playlist[index].coverImgUrl
                , mUserPlayerList.playlist[index].name
                , mUserPlayerList.playlist[index].description as String
            )
            mList.add(tempItem)
        }
        return mList
    }

    private fun initView(){
        mRecyclerView = requireView().findViewById(R.id.rv_createdPlayerList)
    }


}