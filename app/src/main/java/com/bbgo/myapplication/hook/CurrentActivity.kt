package com.bbgo.myapplication.hook

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bbgo.myapplication.R

/**
 * @Description:
 * @Author: wangyuebin
 * @Date: 2021/9/23 5:09 下午
 */
class CurrentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvTextView = findViewById<TextView>(R.id.tv_test)
        tvTextView.text = "CurrentActivity"
        tvTextView.setOnClickListener {
            /**
             * TargetActivity未在AndroidManifest.xml文件中定义
             */
            startActivity((Intent(this, TargetActivity::class.java)))
        }
    }
}