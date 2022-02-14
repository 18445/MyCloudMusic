package com.example.mycloudmusic.fragment

import android.content.Intent
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
import com.example.mycloudmusic.`interface`.RecyclerItemClickListener
import com.example.mycloudmusic.activity.PlayerListActivity
import com.example.mycloudmusic.adapter.PlayerListAdapter2
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.item.PlayerListItem
import com.example.mycloudmusic.userdata.Creator
import com.example.mycloudmusic.userdata.Playlist
import com.example.mycloudmusic.userdata.RecommendList
import com.example.mycloudmusic.userdata.UserPlaylist
import com.example.mycloudmusic.util.MyPreloadModelProvider
import com.example.mycloudmusic.viewmodel.MyViewModel

/**
 * 推荐歌单项的fragment
 */
class RecommendPlayerListFragment : BaseFragment() , RecyclerItemClickListener {
    private lateinit var userModel : MyViewModel
    private lateinit var mRecommendList: RecommendList
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mPlayerList : List<PlayerListItem>
    private lateinit var UID:String


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
        mRecyclerView.adapter = context?.let { PlayerListAdapter2(this,mPlayerList, it) }
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
        UID = userModel.getUser().profile.userId.toString()
    }
    /**
     * Item项的点击回调事件
     * 设置点击跳转加载activity
     * 完成歌单页面的加载
     */
    override fun onRecyclerViewItemClick(view: View, position: Int) {
        Log.d("RecycleViewItemClick","${position}:$view be clicked")
        //序列化利用bundle传递对象
        val intent = Intent(context, PlayerListActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("UserPlayerList",initUserPlayerList())
        intent.putExtras(bundle)
        intent.putExtra("id",UID)
        intent.putExtra("position",position)
        intent.putExtra("cookie",userModel.getUser().cookie)
        startActivity(intent)
    }

    private fun initUserPlayerList(): UserPlaylist {
        val mPlaylist = mutableListOf<Playlist>()
        for (i in mRecommendList.recommend.indices) {
            val currentList = mRecommendList.recommend[i]
            val avatarUrl: String = currentList.creator.avatarUrl
            val backgroundUrl: String = currentList.creator.backgroundUrl
            val gender: Int = currentList.creator.gender
            val nickname: String = currentList.creator.nickname
            val userId: String = currentList.creator.userId

            val coverImgUrl: String = currentList.picUrl
            val creator: Creator = Creator(avatarUrl, backgroundUrl, gender, nickname, userId)
            val description: String? = null
            val id: String = currentList.id
            val name: String = currentList.name
            val trackCount: String = currentList.trackCount.toString()

            val tempList = Playlist(coverImgUrl, creator, description, id, name, trackCount)
            mPlaylist.add(tempList)
        }
        return UserPlaylist(200, true, mPlaylist, "100001")
    }
}