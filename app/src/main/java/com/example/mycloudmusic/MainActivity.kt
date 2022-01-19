package com.example.mycloudmusic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycloudmusic.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}