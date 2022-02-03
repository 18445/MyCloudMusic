package com.example.mycloudmusic.util


import android.os.Message
import okhttp3.*
import okio.IOException
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit


private const val SUCCESS = 1000
private const val FALL = 1001


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


fun asyncPost(url:String, map:MutableMap<String,String>, failConnect: () -> Unit, successConnect: (String)->Unit) {
    //添加post请求参数
    val requestBody = FormBody.Builder()
    for (param in map){
        requestBody.add(param.key,param.value)
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

fun asyncPost(url:String, map:MutableMap<String,String>){



    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10,TimeUnit.SECONDS)
        .writeTimeout(10,TimeUnit.SECONDS)
        .build()

    val requestBody = FormBody.Builder()
    for (param in map){
        requestBody.add(param.key,param.value)
    }
    val paramBody = requestBody.build()

    val request = Request.Builder()
        .post(paramBody)
        .url(url)
        .build()

    val call = okHttpClient.newCall(request)



    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            TODO("something wrong")
        }

        override fun onResponse(call: Call, response: Response) {
            val string: String = response.body!!.string()
            val msg: Message = Message.obtain()
            msg.obj = string
            msg.what = SUCCESS
        }
    })


}

fun StreamToString(inStream: InputStream): String {
    val sb = StringBuilder() //新建一个StringBuilder
    var oneLine: String? //流转换为字符串的一行
    val reader = BufferedReader(InputStreamReader(inStream)) //
    try {
        while (reader.readLine().also { oneLine = it } != null) { //readLine方法将读取一行
            sb.append(oneLine) //拼接字符串
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            inStream.close() //关闭InputStream
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    println("sb:$sb")
    return sb.toString() //将拼接好的字符串返回出去
}



