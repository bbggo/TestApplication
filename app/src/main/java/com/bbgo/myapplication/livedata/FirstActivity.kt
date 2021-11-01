package com.bbgo.myapplication.livedata

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bbgo.myapplication.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class FirstActivity : AppCompatActivity() {

    private val TAG = "LiveDataActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = findViewById<TextView>(R.id.tv_test)
        tv.text = "FirstActivity"
        tv.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }

        EventBus.getDefault().register(this)

        LiveDataBus.get().with("key_test").value = true
        LiveDataBus.get().with("key_test", Boolean::class.java).observe(this) {
            Log.d(TAG, "this is live data test = $it")
        }

        EventBus.getDefault().post(MessageEvent(true))

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(messageEvent: MessageEvent) {
        Log.d(TAG, "this is eventbus test = ${messageEvent.boolean}")
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}