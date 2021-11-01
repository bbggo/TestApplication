package com.bbgo.myapplication.livedata

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bbgo.myapplication.R
import com.bbgo.myapplication.RootApplication
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class SecondActivity : AppCompatActivity() {

    private val TAG = "LiveDataActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = findViewById<TextView>(R.id.tv_test)
        tv.text = "SecondActivity"
        tv.setOnClickListener {
            Intent(this, ThirdActivity::class.java)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                RootApplication.getContext().startActivity(this)
            }
        }

        EventBus.getDefault().register(this)

        LiveDataBus.get().with("key_test", Boolean::class.java).observe(this) {
            Log.d(TAG, "this is live data test2 = $it")
        }

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