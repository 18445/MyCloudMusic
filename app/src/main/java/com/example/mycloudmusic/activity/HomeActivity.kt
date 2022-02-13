package com.example.mycloudmusic.activity

import android.content.ContentValues
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.mycloudmusic.viewmodel.MyViewModel
import com.example.mycloudmusic.R
import com.example.mycloudmusic.adapter.ViewPagerAdapter
import com.example.mycloudmusic.base.BaseActivity
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.fragment.*
import com.example.mycloudmusic.userdata.*
import com.example.mycloudmusic.util.LoggingInterceptor
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import android.app.ProgressDialog
import android.os.Handler


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
    private val mFragments: List<BaseFragment> = listOf(
        HomeFindFragment(),
        HomePodcastFragment(),
        HomeMyFragment(),
        HomeFollowFragment(),
        HomeVillageFragment()
    )
    private lateinit var cookieList:List<String>
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager2: ViewPager2
    private lateinit var user: User
    private lateinit var userModel: MyViewModel
    private lateinit var client: OkHttpClient
    private lateinit var builder: OkHttpClient.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_home)

        initProgress()
        initNet()
        initUser(MYUSER)
        initView()
        initPage()
//
//        initCookie()
//        网络请求部分
        initUserLevel()
        initPlayerList()
//        initUserLikeList()
//        initRecentSong()
//        initRecentVideo()
//        initPersonalizeList()
//        initPersonalizeSong()
        initRecommendList()
//        initRecommendSong()
        super.onCreate(savedInstanceState)
    }

    /**
     * Loading加载框
     */
    private fun initProgress(){
        val waitingDialog = ProgressDialog(this@HomeActivity)
        waitingDialog.setTitle("网络正在加载")
        waitingDialog.setMessage("等待中...")
        waitingDialog.isIndeterminate = true
        waitingDialog.setCancelable(false)
        waitingDialog.show()
        Handler(Looper.myLooper()!!).postDelayed({
             waitingDialog.dismiss()
        },3500)
    }


//    /**
//     * Cookie配置
//     */
//    var okHttpClient: OkHttpClient? = OkHttpClient.Builder()
//        .cookieJar(object : CookieJar {
//            override fun saveFromResponse(httpUrl: HttpUrl, list: List<Cookie>) {
//                cookieStore.put(httpUrl.host, list)
//            }
//
//            override fun loadForRequest(httpUrl: HttpUrl): List<Cookie> {
//                val cookies: List<Cookie> = cookieStore.get(httpUrl.host)
//                return cookies ?: ArrayList()
//            }
//        })
//        .build()
    /**
     * 初始化网络请求
     */
    private fun initNet() {
        builder = OkHttpClient.Builder()
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .writeTimeout(20000, TimeUnit.MILLISECONDS)
            .addInterceptor(LoggingInterceptor())

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
        cookieList = userModel.getUser().cookie.split(";")
    }

    /**
     * 网络请求获得用户等级
     */
    private fun initUserLevel() {
        val cookie = userModel.getUser().cookie

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/user/level")
                //添加Cookie操作
//            .apply {
//                val length = cookieList.count()
//                Log.d("cookie",cookieList.toString())
//                Log.d("cookie","cookie length = $length")
//                for(i in 0..85 step 5){
//                    Log.d("cookie {$i:}", cookieList[i])
//                    Log.d("cookie {${i+1}:}", cookieList[i+1])
//                    Log.d("cookie {${i+2}:}", cookieList[i+2])
//                    Log.d("cookie {${i+3}:}", cookieList[i+3])
//                    Log.d("cookie {${i+4}:}", cookieList[i+4])
//                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
//                    Log.d("cookie {$i:}", cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
//                }
//                for(i in 94..length-5 step 5 ){
//                    Log.d("cookie {$i:}", cookieList[i])
//                    Log.d("cookie {${i+1}:}", cookieList[i+1])
//                    Log.d("cookie {${i+2}:}", cookieList[i+2])
//                    Log.d("cookie {${i+3}:}", cookieList[i+3])
//                    Log.d("cookie {${i+4}:}", cookieList[i+4])
//                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
//                    Log.d("cookie {$i:}", cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
//                }
//            }
            .apply {
                val length = cookieList.count()
                for(i in 0..length-5 step 1){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
            }
                //效果如下：(出于不知名的原因？ 无法直接将Cookie字符串加入head之中，需要将cookie手动分部加入QAQ)
//            .addHeader("Cookie","MUSIC_U=9521d3ce8a1738c7d2dc14b579c106ec77d2eada991907026922f5582e5025291e8907c67206e1edd78b6050a17a35e705925a4e6992f61d07c385928f88e8de; Path=/; Secure; Expires=Mon, 14 Feb 2022 16:14:13 GMT;")
//            .addHeader("Cookie","NMTID=00OM0yVHmU05nI3n0F_pGEGvI4730wAAAF-q8P1UQ; Path=/; Secure; Expires=Wed, 28 Jan 2032 16:14:13 GMT;")
//            .addHeader("Cookie","__csrf=2ab772669d65ba4121d43a28c48ca526; Path=/; Secure; Expires=Mon, 14 Feb 2022 16:14:23 GMT;")
//            .addHeader("Cookie","__remember_me=true; Path=/; Secure; Expires=Mon, 14 Feb 2022 16:14:13 GMT;")
//            .addHeader("Cookie","MUSIC_A_T=1404198497000; Path=/api/clientlog; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_A_T=1404198497000; Path=/api/feedback; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_A_T=1404198497000; Path=/eapi/clientlog; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_A_T=1404198497000; Path=/eapi/feedback; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_A_T=1404198497000; Path=/neapi/clientlog; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_A_T=1404198497000; Path=/neapi/feedback; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_A_T=1404198497000; Path=/openapi/clientlog; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_A_T=1404198497000; Path=/wapi/clientlog; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_A_T=1404198497000; Path=/wapi/feedback; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_A_T=1404198497000; Path=/weapi/clientlog; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_A_T=1404198497000; Path=/weapi/feedback; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_R_T=1404198527506; Path=/api/clientlog; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_R_T=1404198527506; Path=/api/feedback; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_R_T=1404198527506; Path=/eapi/clientlog; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_R_T=1404198527506; Path=/eapi/feedback; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_R_T=1404198527506; Path=/neapi/clientlog; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_R_T=1404198527506; Path=/neapi/feedback; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_R_T=1404198527506; Path=/openapi/clientlog; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_R_T=1404198527506; Path=/wapi/clientlog; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_R_T=1404198527506; Path=/wapi/feedback; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_R_T=1404198527506; Path=/weapi/clientlog; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
//            .addHeader("Cookie","MUSIC_R_T=1404198527506; Path=/weapi/feedback; Secure; Expires=Fri, 17 Feb 2090 19:28:20 GMT;")
            .build()

        Log.d(ContentValues.TAG, " onResponseCookie: $cookie")
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Looper.prepare()
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponseUserLevel: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)

                if (mUserLevelInformation.code != 200) {
                    Looper.prepare()
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()
                    Looper.loop()
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
//            .add("limit","1")
            .build()


        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/user/playlist")
            .apply {
                val length = cookieList.count()
//                for(i in 0..60 step 5){
//                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
//                }
//                for(i in 74..length-5 step 5 ){
//                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
//                }
                for(i in 0..length-5 step 1){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
            }
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponsePlayerList: $userData")
                val mGson = Gson()
//                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)
//
//                if (mUserLevelInformation.code != 200) {
//                    Looper.prepare()
//                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
//                        .show()
//                    Looper.loop()
//                } else {

//                    val userPlaylist= mGson.fromJson(userData, UserPlaylist::class.java)
//                    userModel.userPlaylist.add(userPlaylist)
//                    userModel.userPlaylist= mGson.fromJson(userData, UserPlaylist::class.java)
                    val userPlaylist= mGson.fromJson(userData, UserPlaylist::class.java)
                    userModel.userPlaylist = userPlaylist
                    Log.d("vvv",userPlaylist.toString())
//                }
            }
        })
    }

    /**
     * 网络请求获得最近听歌数据
     */
    private fun initRecentSong() {

        val requestBody = FormBody.Builder()
            .add("limit", "20")
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/record/recent/song")
            .apply {
                val length = cookieList.count()
                for(i in 0..85 step 5){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
                for(i in 94..length-5 step 5 ){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
            }
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Looper.prepare()
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
//                val userDataIn  = response.body?.byteStream()
//                val userData = userDataIn?.let { StreamToString(it) }
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponseRecentSong: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)
                if (mUserLevelInformation.code != 200) {
                    Looper.prepare()
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()
                    Looper.loop()
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
            .add("limit", "20")
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/record/recent/video")
            .apply {
                val length = cookieList.count()
                for(i in 0..85 step 5){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
                for(i in 94..length-5 step 5 ){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
            }
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Looper.prepare()
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponseRecentVideo: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)

                if (mUserLevelInformation.code != 200) {
                    Looper.prepare()
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()
                    Looper.loop()
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
            .add("uid", userModel.getUser().profile.userId.toString().trim())
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/likelist")
            .apply {
                val length = cookieList.count()
                for(i in 0..85 step 5){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
                for(i in 94..length-5 step 5 ){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
            }
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponseLikeList: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)
                if (mUserLevelInformation.code != 200) {
                    Looper.prepare()
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()
                    Looper.loop()
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
            .apply {
                val length = cookieList.count()
                for(i in 0..85 step 5){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
                for(i in 94..length-5 step 5 ){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
            }
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
                    Looper.prepare()
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()
                    Looper.loop()
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
            .apply {
                val length = cookieList.count()
                for(i in 0..85 step 5){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
                for(i in 94..length-5 step 5 ){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
            }
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
                    Looper.prepare()
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()
                    Looper.loop()
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
            .url("https://netease-cloud-music-api-18445.vercel.app/personalized/newsong")
            .apply {
                val length = cookieList.count()
                for(i in 0..85 step 5){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
                for(i in 94..length-5 step 5 ){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
            }
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
                    Looper.prepare()
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()
                    Looper.loop()
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
            .url("https://netease-cloud-music-api-18445.vercel.app/recommend/resource")
            .apply {
                val length = cookieList.count()
                for(i in 0..length-5 step 1){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
            }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Toast.makeText(this@HomeActivity, "网络请求错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(ContentValues.TAG, " onResponseRecommendList: $userData")
                val mGson = Gson()
                val mUserLevelInformation = mGson.fromJson(userData, UserLevelFault::class.java)

                if (mUserLevelInformation.code != 200) {
                    Looper.prepare()
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()
                    Looper.loop()

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
            .header("Cookie", userModel.getUser().cookie.trim())
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
                    Looper.prepare()
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()
                    Looper.loop()

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
                    Looper.prepare()
                    Toast.makeText(this@HomeActivity, mUserLevelInformation.msg, Toast.LENGTH_SHORT)
                        .show()
                    Looper.loop()

                } else {
                    userModel.lyric = mGson.fromJson(userData, Lyric::class.java)
                }
            }
        })
    }

    /**
     *登录的签到接口
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
//                val mUserSignIn :UserSignIn= mGson.fromJson(userData, UserSignIn::class.java)
//
//                if (mUserSignIn.code != 200) {
//                    Toast.makeText(this@HomeActivity, mUserSignIn.msg, Toast.LENGTH_SHORT)
//                        .show()
//                } else {
//                    Toast.makeText(this@HomeActivity, "你获得了 ${mUserSignIn.point} 经验", Toast.LENGTH_SHORT)
//                        .show()
//                }
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
            .apply {
                val length = cookieList.count()
                for(i in 0..85 step 5){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
                for(i in 94..length-5 step 5 ){
                    addHeader("Cookie",cookieList[i]+";"+cookieList[i+3]+";"+"Secure;"+cookieList[i+2]+";")
                }
            }
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
//                val mUserSignIn = mGson.fromJson(userData, UserSignIn::class.java)
//                if (mUserSignIn.code != 200) {
//                    Toast.makeText(this@HomeActivity, mUserSignIn.msg, Toast.LENGTH_SHORT)
//                        .show()
//                }
            }
        })
        //更新喜欢列表
        initUserLikeList()
    }
}

