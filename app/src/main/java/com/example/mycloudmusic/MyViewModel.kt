package com.example.mycloudmusic

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.example.mycloudmusic.data.User

class MyViewModel: ViewModel() {
    private lateinit var user:User

    fun initUser(mUser:User){
        user = mUser
    }

    fun getUser():User{
        return user
    }
}