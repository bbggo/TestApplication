package com.bbgo.myapplication.permission

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bbgo.myapplication.R


class TestPermissionActivity : AppCompatActivity() {

    private val TAG = "PermissionActivity"

    companion object {
        const val PERMISSION_REQUEST_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.tv_test).text = "TestPermissionActivity"



    }



}