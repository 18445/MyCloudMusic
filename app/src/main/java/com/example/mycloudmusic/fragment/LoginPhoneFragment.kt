package com.example.mycloudmusic.fragment


import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.example.mycloudmusic.base.BaseFragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mycloudmusic.viewmodel.MyViewModel
import com.example.mycloudmusic.R
import com.example.mycloudmusic.activity.HomeActivity
import com.example.mycloudmusic.activity.MYUSER
import com.example.mycloudmusic.userdata.LoginVerification
import com.example.mycloudmusic.userdata.User
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit






/**
 * 登录界面之手机登录
 */
class LoginPhoneFragment : BaseFragment() {
    private lateinit var userModel : MyViewModel
    private lateinit var client: OkHttpClient
    private lateinit var builder: OkHttpClient.Builder
    private lateinit var mImageView: ImageView
    private lateinit var mEtPhone:EditText
    private lateinit var mEtCaptcha:EditText
    private lateinit var mMaterialButton: MaterialButton
    private lateinit var mTextView: TextView
    private lateinit var mPhone:String
    private lateinit var mCaptcha:String
    private var recLen = 30
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loginphone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initClick()
        initNet()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView (){
        userModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        mImageView = requireView().findViewById(R.id.img_login_top1)
        mEtPhone = requireView().findViewById(R.id.et_login_phone)
        mEtCaptcha = requireView().findViewById(R.id.et_login_captcha)
        mMaterialButton = requireView().findViewById(R.id.mbtn_loginPhone_bottom)
        mTextView = requireView().findViewById(R.id.tv_login_send)
    }

    /**
     * 初始化点击效果
     */
    private fun initClick(){
        //回退处理
        mImageView.setOnClickListener {
            activity?.onBackPressed()
        }
        //发送验证码
        mTextView.setOnClickListener {
            mPhone = mEtPhone.text.toString()
            if(mPhone.count() != 11){
                Toast.makeText(activity,"输入11位电话号码",Toast.LENGTH_SHORT).show()
            }else{
                initTime()
                Toast.makeText(activity,"正在发送验证码……",Toast.LENGTH_LONG).show()
                postPhone()
            }
        }
        //验证验证码
        mMaterialButton.setOnClickListener {
            mPhone = mEtPhone.text.toString()
            mCaptcha = mEtCaptcha.text.toString()
            if(mPhone.count() != 11){
                Toast.makeText(activity,"输入11位电话号码",Toast.LENGTH_SHORT).show()
            }else if(mCaptcha.trim().count() == 0){
                Toast.makeText(activity,"验证码不能为空",Toast.LENGTH_SHORT).show()
            }
            else{
                postCaptcha()
            }
        }
    }

    /**
     * 初始化网络请求
     */
    private fun initNet(){
        builder = OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS)

        client = builder.build()
    }

    /**
     * 被点击后初始化TextView的时间
     */
    private fun initTime(){
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    recLen--
                    mTextView.isClickable = false
                    mTextView.text = "发送（$recLen）"
                    if (recLen < 0) {
                        mTextView.text = "发送"
                        mTextView.isClickable = true
                    }
                }
            }
        }
        Timer().schedule(task,1000,1000)
    }
    /**
     * 网络请求发送验证码
     */
    private fun postPhone(){
        val requestBody = FormBody.Builder()
            .add("phone",mPhone)
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/captcha/sent")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: ${e.message}")
                Looper.prepare();
                Toast.makeText(activity,"网络异常，请稍后再试",Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                val string = response.body?.string()
                Log.d(TAG, "正常请求 onResponse: $string")
                //切换到主线程
                activity?.runOnUiThread {
                     Toast.makeText(activity,"发送成功",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    /**
     * 网络请求验证验证码
     */
    private fun postCaptcha(){
        val requestBody = FormBody.Builder()
            .add("phone",mPhone)
            .add("captcha",mCaptcha)
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/cellphone/existence/check")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: ${e.message}")
                Looper.prepare();
                Toast.makeText(activity,"网络请求错误",Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                val mData = response.body?.string()
                Log.d(TAG, "$mData")
                val mGson = Gson()
                val mLoginVerification = mGson.fromJson(mData,LoginVerification::class.java)
                //检查是否被注册过
                if(mLoginVerification.exist == 1){
                    getUser()
                    activity?.runOnUiThread {
                        loginSuccess()
                    }
                }else{
                    activity?.runOnUiThread {
                        initUser()
                    }
                }
            }
        })
    }

    /**
     * 用户未注册时初始化用户的方法
     */
    private fun initUser(){
        val editText = EditText(activity)
        val inputDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
        inputDialog.setTitle("请输入初始化名称").setView(editText)
        inputDialog.setPositiveButton("确定") {
                _, _ ->
            val nickname = editText.text.toString().trim()
            if(nickname.isNotEmpty()){
                initNickName(nickname)
            }else{
                Toast.makeText(activity,"不能为空！", Toast.LENGTH_SHORT).show()
            }
        }.show()
    }

    /**
     * 初始化用户名的方法
     */
    private fun initNickName(nickname:String){
        val requestBody = FormBody.Builder()
            .add("nickname",nickname)
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/activate/init/profile")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: ${e.message}")
                Looper.prepare();
                Toast.makeText(activity,"网络请求错误",Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                val string = response.body?.string()
                Log.d(TAG, "正常请求 onResponse: $string")
                Looper.prepare();
                Toast.makeText(activity,"设置成功",Toast.LENGTH_SHORT).show()
                Looper.loop()
            }
        })
    }

    /**
     * 登录成功调用此方法
     */
    private fun loginSuccess(){
        val intent = Intent(activity,HomeActivity::class.java)
        activity?.onBackPressed()
        activity?.finish()
        startActivity(intent)
    }

    /**
     * 调用登录方法获得用户数据
     */
    private fun getUser(){
        val requestBody = FormBody.Builder()
            .add("phone",mPhone)
            .add("password","")
            .add("captcha",mCaptcha)
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/login/cellphone")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: ${e.message}")
                Looper.prepare();
                Toast.makeText(activity,"网络请求错误",Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                val userData = response.body?.string()
                Log.d(TAG, " onResponse: $userData")
                val mGson = Gson()
                MYUSER = (mGson.fromJson(userData,User::class.java)) //更新ViewModel中User数据
            }
        })
    }
}
