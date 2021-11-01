package com.bbgo.myapplication.hook

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bbgo.myapplication.R

/**
 * @Description:
 * @Author: wangyuebin
 * @Date: 2021/9/23 5:09 下午
 */
class TargetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.tv_test).text = "TargetActivity"
    }
}