package com.example.mycloudmusic.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycloudmusic.viewmodel.MyViewModel
import com.example.mycloudmusic.R
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.item.PlayerListItem
import com.example.mycloudmusic.userdata.UserPlaylist
import com.bumptech.glide.util.FixedPreloadSizeProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.example.mycloudmusic.adapter.PlayerListAdapter
import com.example.mycloudmusic.util.MyPreloadModelProvider

/**
 * 创建歌单项的Fragment
 */
class CreatedPlayerListFragment : BaseFragment() {
    private lateinit var mUserPlayerList : UserPlaylist
    private lateinit var userModel : MyViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mPlayerList : List<PlayerListItem>
    private lateinit var UID:String
    var itemSize:Int = 0

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
        mPlayerList = generatePlayerList(mUserPlayerList)
        initRecyclerView()
    }

    /**
     * 初始化RecycleView的操作
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
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = context?.let { PlayerListAdapter(mPlayerList, it, itemSize) }
        Log.d("mPlayerList",mPlayerList.toString())
    }

    /**
     * 根据解析出的json，将UserPlayerList生成List<PlayerListItem>
     */
    private fun generatePlayerList(mUserPlayerList: UserPlaylist): List<PlayerListItem> {
        Log.d("PlayerList:",mUserPlayerList.toString())
        val mList = mutableListOf<PlayerListItem>()
        //添加每一项Item到列表并返回
        for(index in mUserPlayerList.playlist.indices){
        //仅加入创建的歌单
            if (mUserPlayerList.playlist[index].creator.userId != UID){
                itemSize = index
                break
            }
            val tempItem = PlayerListItem(mUserPlayerList.playlist[index].coverImgUrl
                , mUserPlayerList.playlist[index].name
                , "${mUserPlayerList.playlist[index].trackCount}首"
            )
            mList.add(tempItem)
        }
        return mList
    }

    private fun initView(){
        userModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        mUserPlayerList = userModel.userPlaylist
        mRecyclerView = requireView().findViewById(R.id.rv_createdPlayerList)
        UID = userModel.getUser().profile.userId.toString()
    }


}