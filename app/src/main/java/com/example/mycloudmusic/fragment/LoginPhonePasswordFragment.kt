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
 * 登录界面之手机-密码登录
 */
class LoginPhonePasswordFragment : BaseFragment() {
    private lateinit var mImageView: ImageView
    private lateinit var mEtPhone : EditText
    private lateinit var mEtPassword:EditText
    private lateinit var mMaterialButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loginphonepassword, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initClick()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView (){
        mImageView = requireView().findViewById(R.id.img_login_top3)
        mEtPhone = requireView().findViewById(R.id.et_loginPassword_phone)
        mEtPassword = requireView().findViewById(R.id.et_loginPassword_password)
        mMaterialButton = requireView().findViewById(R.id.mbtn_loginPhonePassword_bottom)
    }

    private fun initClick(){
        mImageView.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}