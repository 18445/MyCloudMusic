package com.example.mycloudmusic.activity

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.mycloudmusic.MyViewModel
import com.example.mycloudmusic.R
import com.example.mycloudmusic.adapter.ViewPagerAdapter
import com.example.mycloudmusic.base.BaseActivity
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.fragment.*
import com.example.mycloudmusic.userdata.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


lateinit var MYUSER: User
class HomeActivity : BaseActivity() {
    private val titles: List<String> = listOf("发现", "播客", "我的", "关注", "云村")
    private val titleIcons: List<Int> = listOf(
        R.drawable.ic_home_find,
        R.drawable.ic_home_podcast,
        R.drawable.ic_home_my,
        R.drawable.ic_home_follow,
        R.drawable.ic_home_village
    )
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager2: ViewPager2
    private lateinit var user: User
    private lateinit var userModel: MyViewModel
    private lateinit var client: OkHttpClient
    private lateinit var builder: OkHttpClient.Builder
    private val mFragments: List<BaseFragment> = listOf(
        HomeFindFragment(),
        HomePodcastFragment(),
        HomeMyFragment(),
        HomeFollowFragment(),
        HomeVillageFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initNet()
        initUser(MYUSER)
        initView()
        initPage()
        initUserLevel()
        initPlayerList()
        initRecentSong()
        initUserLikeList()
        initRecentVideo()
        initPersonalizeList()
        initPersonalizeSong()
        initRecommendList()
        initRecommendSong()
    }

    /**
     * 初始化网络请求
     */
    private fun initNet() {
        builder = OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS)

        client = builder.build()
    }

    /**
     * TabLayout页面配置
     */
    private fun initPage() {
        mViewPager2.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, mFragments)
        TabLayoutMediator(mTabLayout, mViewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = titles[0]
                1 -> tab.text = titles[1]
                2 -> tab.text = titles[2]
                3 -> tab.text = titles[3]
                4 -> tab.text = titles[4]
            }
            when (position) {
                0 -> tab.setIcon(titleIcons[0])
                1 -> tab.setIcon(titleIcons[1])
                2 -> tab.setIcon(titleIcons[2])
                3 -> tab.setIcon(titleIcons[3])
                4 -> tab.setIcon(titleIcons[4])
            }
        }.attach()
    }

    private fun initView() {
        mTabLayout = findViewById(R.id.tly_home_bottom)
        mViewPager2 = findViewById(R.id.vp2_home_top)
    }

    private fun initUser(mUser: User) {
        user = mUser
        userModel = ViewModelProvider(this@HomeActivity).get(MyViewModel::class.java)
        userModel.initUser(user)
    }

    /**
     * 网络请求获得用户等级
     */
    private fun initUserLevel() {
        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/user/level")
            .header("Cookie", userModel.getUser().cookie)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponse: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)

                if (mUserLevelInformation.code != 200) {
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    userModel.userLevel = mGson.fromJson(userData, UserLevel::class.java)
                }
            }
        })
    }

    /**
     * 网络请求获得用户歌单
     */
    private fun initPlayerList() {
        val requestBody = FormBody.Builder()
            .add("uid", userModel.getUser().profile.userId.toString())
            .build()


        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/user/playlist")
            .header("Cookie", userModel.getUser().cookie)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponse: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)

                if (mUserLevelInformation.code != 200) {
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    userModel.userPlaylist = mGson.fromJson(userData, UserPlaylist::class.java)
                }
            }
        })
    }

    /**
     * 网络请求获得最近听歌数据
     */
    private fun initRecentSong() {

        val requestBody = FormBody.Builder()
            .add("limit", "50")
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/record/recent/song")
            .header("Cookie", userModel.getUser().cookie)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponse: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)

                if (mUserLevelInformation.code != 200) {
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    userModel.recentSong = mGson.fromJson(userData, RecentSong::class.java)
                }
            }
        })
    }

    /**
     * 网络请求获得最近的视频播放
     */
    private fun initRecentVideo() {

        val requestBody = FormBody.Builder()
            .add("limit", "50")
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/record/recent/video")
            .header("Cookie", userModel.getUser().cookie)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponse: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)

                if (mUserLevelInformation.code != 200) {
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    userModel.recentVideo = mGson.fromJson(userData, RecentVideo::class.java)
                }
            }
        })
    }

    /**
     * 网络请求获得用户喜欢的音乐列表
     */
    private fun initUserLikeList() {

        val requestBody = FormBody.Builder()
            .add("uid", userModel.getUser().profile.userId.toString())
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/likelist")
            .header("Cookie", userModel.getUser().cookie)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponse: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)

                if (mUserLevelInformation.code != 200) {
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    userModel.userLikeList = mGson.fromJson(userData, UserLikeList::class.java)
                }
            }
        })
    }

    /**
     * 调用此方法获得歌单的详细URL
     * 放回的类型为 SongUrl
     */
    fun getSongUrl(id: String): SongUrl? {

        val requestBody = FormBody.Builder()
            .add("id", id)
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/likelist")
            .header("Cookie", userModel.getUser().cookie)
            .post(requestBody)
            .build()

        var songUrl: SongUrl? = null
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponse: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)

                if (mUserLevelInformation.code != 200) {
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    songUrl = mGson.fromJson(userData, SongUrl::class.java)
                }
            }
        })

        return songUrl
    }


    /**
     * 获得推荐歌单
     */
    fun initPersonalizeList(){

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/personalized")
            .header("Cookie", userModel.getUser().cookie)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponse: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)

                if (mUserLevelInformation.code != 200) {
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    userModel.personalizeList = mGson.fromJson(userData, PersonalizeList::class.java)
                }
            }
        })
    }

    /**
     * 获得推荐新歌曲
     */
    private fun initPersonalizeSong(){

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/personalized")
            .header("Cookie", userModel.getUser().cookie)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponse: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)

                if (mUserLevelInformation.code != 200) {
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    userModel.personalizeSong = mGson.fromJson(userData, PersonalizeSong::class.java)
                }
            }
        })
    }

    /**
     * 调用此方法获得每日推荐歌单
     */
    private fun initRecommendList(  ){

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/comment/music")
            .header("Cookie", userModel.getUser().cookie)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponse: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)

                if (mUserLevelInformation.code != 200) {
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    userModel.recommendList = mGson.fromJson(userData,RecommendList::class.java)
                }
            }
        })
    }

    /**
     * 调用此方法获得每日推荐歌曲
     */
    private fun initRecommendSong(){

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/comment/music")
            .header("Cookie", userModel.getUser().cookie)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponse: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)

                if (mUserLevelInformation.code != 200) {
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    userModel.recommendSong = mGson.fromJson(userData,RecommendSong::class.java)
                }
            }
        })
    }






    /**
     * 网络请求，传入歌曲id
     * 获得歌曲的歌词
     */
    fun getLyric(id: String) {

        val requestBody = FormBody.Builder()
            .add("id", id)
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/likelist")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponse: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)

                if (mUserLevelInformation.code != 200) {
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    userModel.lyric = mGson.fromJson(userData, lyric::class.java)
                }
            }
        })
    }

    /**
     *登录接口
     */
    fun signIn(){

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/likelist")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponse: $userData")
                val mGson = Gson()
                val mUserSignIn = mGson.fromJson(userData, UserSignIn::class.java)

                if (mUserSignIn.code != 200) {
                    Toast.makeText(this@HomeActivity, mUserSignIn.msg, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this@HomeActivity, "你获得了 ${mUserSignIn.point} 经验", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    /**
     * 歌曲评论接口
     * 调用此方法歌曲评论获得歌曲热门评论
     */

    fun getComment(id: String,offset:String) : MusicComment?{
        var musicComment : MusicComment? = null
        val requestBody = FormBody.Builder()
            .add("id", id)
            .add("offset", offset)
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/comment/music")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponse: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)

                if (mUserLevelInformation.code != 200) {
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    musicComment = mGson.fromJson(userData, MusicComment::class.java)
                }
            }
        })
        return  musicComment
    }

    fun likeMusic(id:Int){
        var like = true
        //检查是否已喜欢
        for(ids in userModel.userLikeList.ids){
            if(id == ids){
                like = true
                break
            }
        }

        val requestBody = FormBody.Builder()
            .add("id", id.toString())
            .add("like",like.toString())
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/like")
            .header("Cookie", userModel.getUser().cookie)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponse: $userData")
                val mGson = Gson()
                val mUserSignIn = mGson.fromJson(userData, UserSignIn::class.java)

                if (mUserSignIn.code != 200) {
                    Toast.makeText(this@HomeActivity, mUserSignIn.msg, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        //更新喜欢列表
        initUserLikeList()
    }
}

