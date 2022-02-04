package com.example.mycloudmusic.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.FixedPreloadSizeProvider
import com.example.mycloudmusic.R
import com.example.mycloudmusic.adapter.PlayerListAdapter
import com.example.mycloudmusic.adapter.PlayerListAdapter2
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.item.PlayerListItem
import com.example.mycloudmusic.userdata.RecommendList
import com.example.mycloudmusic.util.MyPreloadModelProvider
import com.example.mycloudmusic.viewmodel.MyViewModel

/**
 * 推荐歌单项的fragment
 */
class RecommendPlayerListFragment : BaseFragment() {
    private lateinit var mRecommendList: RecommendList
    private lateinit var userModel : MyViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mPlayerList : List<PlayerListItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recommendplaylist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        mPlayerList = generatePlayerList(mRecommendList)
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
        Log.d("size",mPlayerList.count().toString())
        mRecyclerView.adapter = context?.let { PlayerListAdapter2(mPlayerList, it) }
        Log.d("mPlayerList",mPlayerList.toString())
    }

    /**
     * 根据解析出的json，将RecommendList生成List<PlayerListItem>
     */
    private fun generatePlayerList(mRecommendList: RecommendList): List<PlayerListItem> {
        val mList = mutableListOf<PlayerListItem>()
        //添加每一项Item到列表并返回
        for(index in mRecommendList.recommend.indices){
            val tempItem = PlayerListItem(mRecommendList.recommend[index].picUrl
                , mRecommendList.recommend[index].name
                , "${mRecommendList.recommend[index].trackCount}首,by ${mRecommendList.recommend[index].creator.nickname}"
            )
            mList.add(tempItem)
        }
        return mList
    }

    private fun initView(){
        userModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        mRecommendList = userModel.recommendList
        mRecyclerView = requireView().findViewById(R.id.rv_createdPlayerList)
    }
}