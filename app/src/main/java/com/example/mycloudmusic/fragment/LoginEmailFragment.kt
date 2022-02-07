package com.example.mycloudmusic.fragment

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mycloudmusic.viewmodel.MyViewModel
import com.example.mycloudmusic.R
import com.example.mycloudmusic.activity.HomeActivity
import com.example.mycloudmusic.activity.LoginActivity
import com.example.mycloudmusic.activity.MYUSER
import com.example.mycloudmusic.base.BaseFragment
import com.example.mycloudmusic.userdata.LoginFailure
import com.example.mycloudmusic.userdata.UnInitUser
import com.example.mycloudmusic.userdata.User
import com.example.mycloudmusic.util.FinishActivityManager
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 登录界面之邮箱登录
 */
class LoginEmailFragment : BaseFragment(){
    private lateinit var userModel : MyViewModel
    private lateinit var client: OkHttpClient
    private lateinit var builder: OkHttpClient.Builder
    private lateinit var mImageView:ImageView
    private lateinit var mEtEmail : EditText
    private lateinit var mEtPassword:EditText
    private lateinit var mMaterialButton: MaterialButton
    private lateinit var mEmail:String
    private lateinit var mPassword:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loginemail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initNet()
        initView()
        initClick()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView (){
        userModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        mImageView = requireView().findViewById(R.id.img_login_top2)
        mEtEmail = requireView().findViewById(R.id.et_loginEmail_email)
        mEtPassword = requireView().findViewById(R.id.et_loginEmail_password)
        mMaterialButton = requireView().findViewById(R.id.mbtn_loginEmail_bottom)
    }

    private fun initClick(){
        mImageView.setOnClickListener {
            activity?.onBackPressed()
        }

        mMaterialButton.setOnClickListener {
            mEmail = mEtEmail.text.toString()
            mPassword = mEtPassword.text.toString()
            loginIn()
        }
    }


    /**
     * 初始化网络
     */
    private fun initNet(){
        builder = OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS)

        client = builder.build()
    }

    /**
     * 登录功能
     */
    private fun loginIn(){
        val requestBody = FormBody.Builder()
            .add("email",mEmail)
            .add("password",mPassword)
            .build()

        val request = Request.Builder()
            .url("https://netease-cloud-music-api-18445.vercel.app/login")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(ContentValues.TAG, "onFailure: ${e.message}")
                Looper.prepare();
                Toast.makeText(activity,"网络请求错误", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                val string = response.body?.string()
                Log.d(ContentValues.TAG, "正常请求 onResponse: $string")

                val mGson = Gson()
                val loginInformation : LoginFailure = mGson.fromJson(string, LoginFailure::class.java)
                //账务错误
                if(loginInformation.code != 200){
                    Looper.prepare();
                    Toast.makeText(activity,"账号或密码输入有误", Toast.LENGTH_SHORT).show()
                    Looper.loop()
                }else{
                    var user = mGson.fromJson(string, UnInitUser::class.java)
                    if(user.UnusedAccount.uninitialized){
                        Looper.prepare();
                        Toast.makeText(activity,"账号未注册，请用手机登录", Toast.LENGTH_LONG).show()
                        Looper.loop()
                    }else{
                        getUser(string!!)
                        activity?.runOnUiThread {
                            loginSuccess()
                    }
                    }

                }

            }

        })
    }

    /**
     * 登录成功调用此方法
     */
    private fun loginSuccess(){
        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
        activity?.onBackPressed()
//        activity?.finish()
        FinishActivityManager.manager!!.finishAllActivity()
    }


    /**
     * 调用登录方法获得用户数据
     */
    private fun getUser(userData:String){
        Log.d(ContentValues.TAG, " onResponse: $userData")
        val mGson = Gson()
        MYUSER = (mGson.fromJson(userData, User::class.java))//更新ViewModel中User数据
    }
}