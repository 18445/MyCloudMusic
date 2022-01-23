package com.example.mycloudmusic.fragment

import android.os.Bundle
import com.example.mycloudmusic.base.BaseFragment


import android.view.ViewGroup

import android.view.LayoutInflater
import android.view.View
import com.example.mycloudmusic.R


class LoginPhoneFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loginphone, container, false)
    }
}