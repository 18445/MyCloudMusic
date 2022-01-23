package com.example.mycloudmusic.fragment

import android.os.Bundle
import com.example.mycloudmusic.base.BaseFragment


import android.view.ViewGroup

import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mycloudmusic.R
import com.google.android.material.button.MaterialButton

/**
 * 登录界面之手机登录
 */
class LoginPhoneFragment : BaseFragment() {
    private lateinit var mImageView: ImageView
    private lateinit var mEtPhone:EditText
    private lateinit var mMaterialButton: MaterialButton
    private lateinit var mPhone:String

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
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView (){
        mImageView = requireView().findViewById(R.id.img_login_top1)
        mEtPhone = requireView().findViewById(R.id.et_login_phone)
        mMaterialButton = requireActivity().findViewById(R.id.mbtn_loginPhone_bottom)
    }

    private fun initClick(){
        mImageView.setOnClickListener {
            activity?.onBackPressed()
        }
        mMaterialButton.setOnClickListener {
            mPhone = mEtPhone.toString().trim()
            if(mPhone.length != 11){
                Toast.makeText(activity,"输入11位电话号码",Toast.LENGTH_SHORT).show()
            }else{

            }
        }
    }

}