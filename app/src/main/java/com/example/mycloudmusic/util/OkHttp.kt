package com.example.mycloudmusic.util

import com.example.mycloudmusic.data.PostParamToValue
import okhttp3.*
import okio.IOException

fun asyncGet(url : String,failConnect: () -> Unit, successConnect: (String)->Unit) {
    //创建request请求对象
    val request = Request.Builder()
        .url(url)
        .method("GET", null)
        .build()

    //创建call并调用enqueue()方法实现网络请求
    OkHttpClient().newCall(request)
        .enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                failConnect
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = call.execute().body.toString()
                successConnect(responseData)
            }
        })
}


fun asyncPost(url:String, postParamToValues:MutableList<PostParamToValue>, failConnect: () -> Unit, successConnect: (String)->Unit) {
    //添加post请求参数
    val requestBody = FormBody.Builder()
    for (param in postParamToValues){
        requestBody.add(param.url,param.value)
    }
    val paramBody:FormBody = requestBody.build()

    //创建request请求对象
    val request = Request.Builder()
        .url(url)
        .post(paramBody)
        .build()

    //创建call并调用enqueue()方法实现网络请求
    OkHttpClient().newCall(request)
        .enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                failConnect
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = call.execute().body.toString()
                successConnect(responseData)
            }
        })
}

