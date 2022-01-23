package com.example.mycloudmusic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.example.mycloudmusic.R
import com.example.mycloudmusic.base.BaseFragment
import com.google.android.material.button.MaterialButton

/**
 * 登录界面之邮箱登录
 */
class LoginEmailFragment : BaseFragment(){
    private lateinit var mImageView:ImageView
    private lateinit var mEtEmail : EditText
    private lateinit var mEtPassword:EditText
    private lateinit var mMaterialButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loginemail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initClick()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView (){
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
            
        }
    }
}