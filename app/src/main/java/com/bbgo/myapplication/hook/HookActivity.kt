package com.bbgo.myapplication.hook

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.bbgo.myapplication.R

class HookActivity : AppCompatActivity() {

    private val TAG = "HookActivity"

    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val forName = Class.forName("android.app.ActivityThread")
        val atField = forName.getDeclaredField("sCurrentActivityThread")
        atField.isAccessible = true
        val activityThreadInstance = atField.get(forName)
        println("activityThreadInstance = $activityThreadInstance")
        val mHField = forName.getDeclaredField("mH")
        mHField.isAccessible = true
        val hInstance = mHField.get(activityThreadInstance) as Handler
        val handlerClass = hInstance.javaClass.superclass
        println("activityThreadInstance hInstance = $hInstance")
        println("activityThreadInstance handlerClass = $handlerClass")
        // 反射Handler的mCallback属性
        val callBackField = handlerClass?.getDeclaredField("mCallback")
        callBackField?.isAccessible = true
        callBackField?.set(hInstance, HookCallBack())

    }

    /**
     * hook其实就是把系统原有的替换为自定义的
     */
    private class HookCallBack : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            return false
        }
    }

}